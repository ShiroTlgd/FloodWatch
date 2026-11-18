package br.com.fiap.rm98825.viewmodel

import br.com.fiap.rm98825.model.WeatherInfo

sealed interface WeatherUiState {
    val areaId: Int?

    data object Idle : WeatherUiState {
        override val areaId: Int? = null
    }

    data class Loading(override val areaId: Int) : WeatherUiState

    data class Success(
        override val areaId: Int,
        val areaName: String,
        val info: WeatherInfo
    ) : WeatherUiState

    data class Error(override val areaId: Int, val message: String) : WeatherUiState
}
