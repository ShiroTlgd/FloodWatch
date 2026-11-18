package br.com.fiap.rm98825.domain

import br.com.fiap.rm98825.model.AreaEntity
import br.com.fiap.rm98825.model.AreaStatus
import br.com.fiap.rm98825.model.RiskLevel

data class AreaForm(
    val name: String = "",
    val address: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val rainfall: String = "0",
    val recurrences: String = "0",
    val waterDepth: String = "0",
    val risk: String = RiskLevel.MEDIUM,
    val status: String = AreaStatus.ATTENTION
) {
    companion object {
        fun from(area: AreaEntity?): AreaForm =
            if (area == null) {
                AreaForm(latitude = "-23.5505", longitude = "-46.6333")
            } else {
                AreaForm(
                    name = area.name,
                    address = area.address,
                    latitude = area.latitude.toString(),
                    longitude = area.longitude.toString(),
                    rainfall = area.rainfallMm.toString(),
                    recurrences = area.recurrenceCount.toString(),
                    waterDepth = area.waterDepthCm.toString(),
                    risk = area.risk,
                    status = area.status
                )
            }
    }
}

sealed interface FormResult {
    data class Valid(val area: AreaEntity) : FormResult
    data class Invalid(val message: String) : FormResult
}

object AreaValidator {
    fun validate(form: AreaForm): FormResult {
        if (form.name.isBlank()) return FormResult.Invalid("Informe o nome da região.")
        if (form.address.isBlank()) return FormResult.Invalid("Informe o endereço ou referência.")

        val latitude = form.latitude.toDecimal()
        if (latitude == null || latitude !in -90.0..90.0) {
            return FormResult.Invalid("Latitude inválida (use um valor entre -90 e 90).")
        }

        val longitude = form.longitude.toDecimal()
        if (longitude == null || longitude !in -180.0..180.0) {
            return FormResult.Invalid("Longitude inválida (use um valor entre -180 e 180).")
        }

        val rainfall = form.rainfall.toDecimal()
        if (rainfall == null || rainfall < 0) {
            return FormResult.Invalid("Volume de chuva inválido.")
        }

        val recurrences = form.recurrences.trim().toIntOrNull()
        if (recurrences == null || recurrences < 0) {
            return FormResult.Invalid("Quantidade de reincidências inválida.")
        }

        val depth = form.waterDepth.toDecimal()
        if (depth == null || depth < 0) {
            return FormResult.Invalid("Profundidade inválida.")
        }

        return FormResult.Valid(
            AreaEntity(
                name = form.name.trim(),
                address = form.address.trim(),
                latitude = latitude,
                longitude = longitude,
                rainfallMm = rainfall,
                recurrenceCount = recurrences,
                waterDepthCm = depth,
                risk = form.risk,
                status = form.status
            )
        )
    }

    private fun String.toDecimal(): Double? = trim().replace(',', '.').toDoubleOrNull()
}
