package br.com.fiap.rm98825.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "areas")
data class AreaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rainfallMm: Double,
    val recurrenceCount: Int,
    val waterDepthCm: Double,
    val risk: String,
    val status: String,
    val isFavorite: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)
