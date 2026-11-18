package br.com.fiap.rm98825.ui.theme

import androidx.compose.ui.graphics.Color
import br.com.fiap.rm98825.model.AreaStatus
import br.com.fiap.rm98825.model.RiskLevel

val Blue700 = Color(0xFF075985)
val Teal700 = Color(0xFF0F766E)
val BlueContainer = Color(0xFFD6EEFB)
val OnBlueContainer = Color(0xFF042F49)

val RiskLow = Color(0xFF15803D)
val RiskMedium = Color(0xFFB45309)
val RiskHigh = Color(0xFFC2410C)
val RiskCritical = Color(0xFFB91C1C)

fun riskColor(risk: String): Color = when (risk) {
    RiskLevel.LOW -> RiskLow
    RiskLevel.MEDIUM -> RiskMedium
    RiskLevel.HIGH -> RiskHigh
    RiskLevel.CRITICAL -> RiskCritical
    else -> Color.Gray
}

fun statusColor(status: String): Color = when (status) {
    AreaStatus.NORMAL -> RiskLow
    AreaStatus.ATTENTION -> RiskMedium
    AreaStatus.FLOODED -> RiskCritical
    else -> Color.Gray
}
