package br.com.fiap.rm98825.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.fiap.rm98825.R
import br.com.fiap.rm98825.model.RiskLevel
import br.com.fiap.rm98825.navigation.Routes
import br.com.fiap.rm98825.ui.components.AreaCard
import br.com.fiap.rm98825.ui.components.ChipSelector
import br.com.fiap.rm98825.ui.components.EmptyState
import br.com.fiap.rm98825.ui.components.MainScaffold
import br.com.fiap.rm98825.ui.components.MetricCard
import br.com.fiap.rm98825.viewmodel.AreaViewModel

private const val ALL_RISKS = "Todos"

@Composable
fun HomeScreen(vm: AreaViewModel, nav: NavHostController) {
    val areas by vm.visibleAreas.collectAsState()
    val summary by vm.summary.collectAsState()
    val query by vm.query.collectAsState()
    val riskFilter by vm.riskFilter.collectAsState()

    MainScaffold(
        title = "FloodWatch",
        nav = nav,
        floatingActionButton = {
            FloatingActionButton(onClick = { nav.navigate(Routes.FORM) }) {
                Icon(Icons.Default.Add, contentDescription = "Cadastrar área")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Situação das áreas", style = MaterialTheme.typography.headlineSmall)
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MetricCard("Áreas", summary.total.toString(), Modifier.weight(1f))
                    MetricCard("Alto risco", summary.highRisk.toString(), Modifier.weight(1f))
                    MetricCard("Alagadas", summary.flooded.toString(), Modifier.weight(1f))
                }
            }
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = vm::onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Buscar por nome ou endereço") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { vm.onQueryChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpar busca")
                            }
                        }
                    }
                )
            }
            item {
                ChipSelector(
                    options = listOf(ALL_RISKS) + RiskLevel.all,
                    selected = riskFilter ?: ALL_RISKS,
                    onSelect = { vm.onRiskFilterChange(if (it == ALL_RISKS) null else it) }
                )
            }
            if (areas.isEmpty()) {
                item {
                    if (summary.total == 0) {
                        EmptyState(
                            image = R.drawable.img_empty,
                            title = "Nenhuma área cadastrada",
                            message = "Use o botão + para registrar a primeira área."
                        )
                    } else {
                        EmptyState(
                            image = R.drawable.img_empty,
                            title = "Nenhuma área encontrada",
                            message = "Ajuste a busca ou o filtro de risco."
                        )
                    }
                }
            } else {
                items(areas, key = { it.id }) { area ->
                    AreaCard(
                        area = area,
                        onClick = { nav.navigate(Routes.details(area.id)) },
                        onFavoriteClick = { vm.toggleFavorite(area) }
                    )
                }
            }
        }
    }
}
