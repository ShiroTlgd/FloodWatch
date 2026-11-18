package br.com.fiap.rm98825.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun FloodWatchTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Blue700,
            secondary = Teal700,
            primaryContainer = BlueContainer,
            onPrimaryContainer = OnBlueContainer
        ),
        content = content
    )
}
