package br.com.fiap.rm98825.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.rm98825.model.AreaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AreaDao {
    @Query("SELECT * FROM areas ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<AreaEntity>>

    @Query("SELECT * FROM areas WHERE id = :id")
    fun observeById(id: Int): Flow<AreaEntity?>

    @Query("SELECT * FROM areas WHERE id = :id")
    suspend fun getById(id: Int): AreaEntity?

    @Insert
    suspend fun insert(area: AreaEntity)

    @Update
    suspend fun update(area: AreaEntity)

    @Delete
    suspend fun delete(area: AreaEntity)

    @Query("UPDATE areas SET isFavorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: Int, favorite: Boolean)
}
