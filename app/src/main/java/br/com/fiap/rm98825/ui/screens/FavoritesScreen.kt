package br.com.fiap.rm98825.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.fiap.rm98825.R
import br.com.fiap.rm98825.navigation.Routes
import br.com.fiap.rm98825.ui.components.AreaCard
import br.com.fiap.rm98825.ui.components.EmptyState
import br.com.fiap.rm98825.ui.components.MainScaffold
import br.com.fiap.rm98825.viewmodel.AreaViewModel

@Composable
fun FavoritesScreen(vm: AreaViewModel, nav: NavHostController) {
    val favorites by vm.favorites.collectAsState()

    MainScaffold(title = "Áreas favoritas", nav = nav) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (favorites.isEmpty()) {
                item {
                    EmptyState(
                        image = R.drawable.img_empty,
                        title = "Nenhum favorito ainda",
                        message = "Toque no coração de uma área para acompanhá-la aqui."
                    )
                }
            } else {
                items(favorites, key = { it.id }) { area ->
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
