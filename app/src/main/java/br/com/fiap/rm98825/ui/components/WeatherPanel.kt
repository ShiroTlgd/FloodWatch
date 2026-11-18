package br.com.fiap.rm98825.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.fiap.rm98825.viewmodel.WeatherUiState

@Composable
fun WeatherPanel(
    state: WeatherUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        WeatherUiState.Idle -> Text(
            "Nenhuma consulta realizada. Toque em uma área para buscar os dados.",
            modifier = modifier,
            style = MaterialTheme.typography.bodyMedium
        )

        is WeatherUiState.Loading -> Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(Modifier.size(28.dp))
            Text("Carregando dados meteorológicos...")
        }

        is WeatherUiState.Error -> Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(state.message, color = MaterialTheme.colorScheme.onErrorContainer)
                OutlinedButton(onClick = onRetry) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Text("Tentar novamente", Modifier.padding(start = 8.dp))
                }
            }
        }

        is WeatherUiState.Success -> Card(modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Dados atuais — ${state.areaName}",
                    style = MaterialTheme.typography.titleMedium
                )
                DetailRow("Precipitação", "${state.info.precipitationMm} mm")
                DetailRow("Chuva", "${state.info.rainMm} mm")
                Text(state.info.alertMessage, fontWeight = FontWeight.Bold)
                Text(
                    "Probabilidade de chuva nas próximas horas",
                    style = MaterialTheme.typography.labelLarge
                )
                state.info.hours.forEach { hour ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(hour.hour, Modifier.width(52.dp))
                        LinearProgressIndicator(
                            progress = { hour.probability / 100f },
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "${hour.probability}%",
                            modifier = Modifier.width(48.dp),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}
