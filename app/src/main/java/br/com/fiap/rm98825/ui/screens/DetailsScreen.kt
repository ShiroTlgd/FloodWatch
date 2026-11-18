package br.com.fiap.rm98825.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.fiap.rm98825.model.AreaEntity
import br.com.fiap.rm98825.navigation.Routes
import br.com.fiap.rm98825.ui.components.DetailRow
import br.com.fiap.rm98825.ui.components.DetailScaffold
import br.com.fiap.rm98825.ui.components.FavoriteButton
import br.com.fiap.rm98825.ui.components.WeatherPanel
import br.com.fiap.rm98825.ui.theme.riskColor
import br.com.fiap.rm98825.viewmodel.AreaViewModel
import br.com.fiap.rm98825.viewmodel.WeatherUiState
import br.com.fiap.rm98825.viewmodel.WeatherViewModel

@Composable
fun DetailsScreen(
    areaVm: AreaViewModel,
    weatherVm: WeatherViewModel,
    nav: NavHostController,
    areaId: Int
) {
    val areaFlow = remember(areaId) { areaVm.observeArea(areaId) }
    val area by areaFlow.collectAsState(initial = null)
    val weatherState by weatherVm.state.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    val current = area

    LaunchedEffect(current?.id, current?.latitude, current?.longitude) {
        if (current != null) weatherVm.load(current)
    }

    DetailScaffold(
        title = "Detalhes da área",
        nav = nav,
        actions = {
            if (current != null) {
                FavoriteButton(
                    isFavorite = current.isFavorite,
                    onClick = { areaVm.toggleFavorite(current) }
                )
            }
        }
    ) { padding ->
        if (current == null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val shownWeather = if (weatherState.areaId == current.id) {
                weatherState
            } else {
                WeatherUiState.Loading(current.id)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RiskHeader(current)
                Text(current.name, style = MaterialTheme.typography.headlineSmall)
                Text(current.address)

                Card(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DetailRow("Volume de chuva", "${current.rainfallMm} mm")
                        DetailRow("Reincidências", current.recurrenceCount.toString())
                        DetailRow("Profundidade estimada", "${current.waterDepthCm} cm")
                        DetailRow("Coordenadas", "${current.latitude}, ${current.longitude}")
                    }
                }

                Text("Clima neste ponto", style = MaterialTheme.typography.titleMedium)
                WeatherPanel(state = shownWeather, onRetry = { weatherVm.load(current) })

                Button(
                    onClick = { nav.navigate(Routes.edit(current.id)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Atualizar informações")
                }
                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Excluir área")
                }
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Excluir área?") },
                    text = { Text("A área \"${current.name}\" será removida permanentemente.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDeleteDialog = false
                                areaVm.delete(current)
                                nav.popBackStack()
                            }
                        ) { Text("Excluir") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
                    }
                )
            }
        }
    }
}

@Composable
private fun RiskHeader(area: AreaEntity) {
    val color = riskColor(area.risk)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(40.dp)
            )
            Column {
                Text(
                    "Risco ${area.risk}",
                    style = MaterialTheme.typography.titleLarge,
                    color = color
                )
                Text("Status: ${area.status}")
            }
        }
    }
}
