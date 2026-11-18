package br.com.fiap.rm98825.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.rm98825.data.repository.AreaRepository
import br.com.fiap.rm98825.model.AreaEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AreaRepository.create(application)

    private val _state = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val state: StateFlow<WeatherUiState> = _state.asStateFlow()

    private var job: Job? = null

    fun load(area: AreaEntity) {
        job?.cancel()
        job = viewModelScope.launch {
            _state.value = WeatherUiState.Loading(area.id)
            _state.value = try {
                val info = repository.weather(area.latitude, area.longitude)
                WeatherUiState.Success(area.id, area.name, info)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                WeatherUiState.Error(
                    area.id,
                    "Não foi possível carregar os dados. Verifique sua conexão e tente novamente."
                )
            }
        }
    }
}
