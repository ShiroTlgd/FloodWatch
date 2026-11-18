package br.com.fiap.rm98825.model

data class HourForecast(
    val hour: String,
    val probability: Int,
    val precipitationMm: Double
)

data class WeatherInfo(
    val precipitationMm: Double,
    val rainMm: Double,
    val hours: List<HourForecast>
) {
    val maxProbability: Int
        get() = hours.maxOfOrNull { it.probability } ?: 0

    val alertMessage: String
        get() = when {
            precipitationMm > 0 -> "Está chovendo neste ponto agora."
            maxProbability >= 70 -> "Alta probabilidade de chuva nas próximas horas."
            maxProbability >= 40 -> "Possibilidade de chuva. Fique atento."
            else -> "Baixa probabilidade de chuva nas próximas horas."
        }
}
