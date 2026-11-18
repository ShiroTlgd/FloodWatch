package br.com.fiap.rm98825.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.fiap.rm98825.R
import br.com.fiap.rm98825.ui.components.AreaSelectCard
import br.com.fiap.rm98825.ui.components.EmptyState
import br.com.fiap.rm98825.ui.components.MainScaffold
import br.com.fiap.rm98825.ui.components.WeatherPanel
import br.com.fiap.rm98825.viewmodel.AreaViewModel
import br.com.fiap.rm98825.viewmodel.WeatherUiState
import br.com.fiap.rm98825.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch

@Composable
fun WeatherScreen(
    areaVm: AreaViewModel,
    weatherVm: WeatherViewModel,
    nav: NavHostController
) {
    val areas by areaVm.allAreas.collectAsState()
    val rawState by weatherVm.state.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val selectedId = rawState.areaId
    val state = if (selectedId != null && areas.none { it.id == selectedId }) {
        WeatherUiState.Idle
    } else {
        rawState
    }

    MainScaffold(title = "Previsão meteorológica", nav = nav) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    "Escolha uma área para consultar a chuva",
                    style = MaterialTheme.typography.titleLarge
                )
                Text("A consulta usa a latitude e a longitude cadastradas na área.")
            }

            if (areas.isEmpty()) {
                item {
                    EmptyState(
                        image = R.drawable.img_empty,
                        title = "Nenhuma área cadastrada",
                        message = "Cadastre uma área na tela inicial para consultar o clima dela."
                    )
                }
            } else {
                item {
                    WeatherPanel(
                        state = state,
                        onRetry = {
                            areas.firstOrNull { it.id == state.areaId }?.let(weatherVm::load)
                        }
                    )
                }
                item {
                    Text("Áreas cadastradas", style = MaterialTheme.typography.titleMedium)
                }
                items(areas, key = { it.id }) { area ->
                    AreaSelectCard(
                        area = area,
                        selected = area.id == state.areaId,
                        onClick = {
                            weatherVm.load(area)
                            scope.launch { listState.animateScrollToItem(0) }
                        }
                    )
                }
            }
        }
    }
}
