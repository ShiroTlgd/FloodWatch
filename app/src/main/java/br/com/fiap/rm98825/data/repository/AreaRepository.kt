package br.com.fiap.rm98825.data.repository

import android.content.Context
import br.com.fiap.rm98825.data.local.AreaDao
import br.com.fiap.rm98825.data.local.FloodDatabase
import br.com.fiap.rm98825.data.remote.WeatherApi
import br.com.fiap.rm98825.model.AreaEntity
import br.com.fiap.rm98825.model.HourForecast
import br.com.fiap.rm98825.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

class AreaRepository(
    private val dao: AreaDao,
    private val weatherApi: WeatherApi
) {
    val areas: Flow<List<AreaEntity>> = dao.observeAll()

    fun observeArea(id: Int): Flow<AreaEntity?> = dao.observeById(id)

    suspend fun getArea(id: Int): AreaEntity? = dao.getById(id)

    suspend fun save(area: AreaEntity) {
        val stamped = area.copy(updatedAt = System.currentTimeMillis())
        if (stamped.id == 0) dao.insert(stamped) else dao.update(stamped)
    }

    suspend fun delete(area: AreaEntity) = dao.delete(area)

    suspend fun setFavorite(id: Int, favorite: Boolean) = dao.setFavorite(id, favorite)

    suspend fun weather(latitude: Double, longitude: Double): WeatherInfo {
        val response = weatherApi.getWeather(latitude, longitude)

        val times = response.hourly?.time.orEmpty()
        val probabilities = response.hourly?.precipitationProbability.orEmpty()
        val amounts = response.hourly?.precipitation.orEmpty()

        val now = response.current?.time
        val start = if (now == null) 0 else times.indexOfLast { it <= now }.coerceAtLeast(0)

        val hours = times.indices.drop(start).take(6).map { index ->
            HourForecast(
                hour = times[index].substringAfter('T'),
                probability = probabilities.getOrNull(index) ?: 0,
                precipitationMm = amounts.getOrNull(index) ?: 0.0
            )
        }

        if (response.current == null && hours.isEmpty()) {
            throw IllegalStateException("A API não retornou dados para esta coordenada.")
        }

        return WeatherInfo(
            precipitationMm = response.current?.precipitation ?: 0.0,
            rainMm = response.current?.rain ?: 0.0,
            hours = hours
        )
    }

    companion object {
        fun create(context: Context): AreaRepository =
            AreaRepository(FloodDatabase.get(context).areaDao(), WeatherApi.create())
    }
}
