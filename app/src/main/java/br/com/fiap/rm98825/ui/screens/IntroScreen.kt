package br.com.fiap.rm98825.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.fiap.rm98825.R

private data class IntroPage(
    @DrawableRes val image: Int,
    val title: String,
    val description: String
)

private val introPages = listOf(
    IntroPage(
        R.drawable.img_intro_monitor,
        "Monitore áreas de risco",
        "Consulte regiões suscetíveis a alagamentos e veja o nível de risco de cada uma."
    ),
    IntroPage(
        R.drawable.img_intro_share,
        "Compartilhe informações",
        "Cadastre e atualize ocorrências da sua cidade para ajudar outras pessoas."
    ),
    IntroPage(
        R.drawable.img_intro_data,
        "Previna-se com dados",
        "Acompanhe a previsão de chuva de cada ponto cadastrado, direto da API Open-Meteo."
    )
)

@Composable
fun IntroScreen(onFinish: (dontShowAgain: Boolean) -> Unit) {
    var page by rememberSaveable { mutableIntStateOf(0) }
    var dontShowAgain by rememberSaveable { mutableStateOf(false) }

    val current = introPages[page]
    val isLastPage = page == introPages.lastIndex

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(current.image),
                contentDescription = current.title,
                modifier = Modifier.size(240.dp)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                current.title,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            Text(
                current.description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            PageIndicator(count = introPages.size, selected = page)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { dontShowAgain = !dontShowAgain },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = dontShowAgain, onCheckedChange = { dontShowAgain = it })
            Text("Não mostrar esta introdução novamente")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = { if (page > 0) page-- }, enabled = page > 0) {
                Text("Voltar")
            }
            Button(onClick = { if (isLastPage) onFinish(dontShowAgain) else page++ }) {
                Text(if (isLastPage) "Começar" else "Avançar")
            }
        }
    }
}

@Composable
private fun PageIndicator(count: Int, selected: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(count) { index ->
            val color = if (index == selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
            Spacer(
                Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
