package br.com.fiap.rm98825.model

object RiskLevel {
    const val LOW = "Baixo"
    const val MEDIUM = "Médio"
    const val HIGH = "Alto"
    const val CRITICAL = "Crítico"

    val all = listOf(LOW, MEDIUM, HIGH, CRITICAL)

    fun isHigh(risk: String) = risk == HIGH || risk == CRITICAL
}

object AreaStatus {
    const val NORMAL = "Normal"
    const val ATTENTION = "Atenção"
    const val FLOODED = "Alagada"

    val all = listOf(NORMAL, ATTENTION, FLOODED)
}

data class AreaSummary(
    val total: Int = 0,
    val highRisk: Int = 0,
    val flooded: Int = 0
)
