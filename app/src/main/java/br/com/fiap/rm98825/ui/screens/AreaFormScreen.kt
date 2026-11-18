package br.com.fiap.rm98825.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.fiap.rm98825.domain.AreaForm
import br.com.fiap.rm98825.model.AreaEntity
import br.com.fiap.rm98825.model.AreaStatus
import br.com.fiap.rm98825.model.RiskLevel
import br.com.fiap.rm98825.ui.components.ChipSelector
import br.com.fiap.rm98825.ui.components.DetailScaffold
import br.com.fiap.rm98825.ui.components.FormTextField
import br.com.fiap.rm98825.viewmodel.AreaViewModel

@Composable
fun AreaFormScreen(vm: AreaViewModel, nav: NavHostController, areaId: Int?) {
    val isEditing = areaId != null
    var existing by remember { mutableStateOf<AreaEntity?>(null) }
    var ready by remember { mutableStateOf(!isEditing) }

    LaunchedEffect(areaId) {
        if (areaId != null) {
            existing = vm.getArea(areaId)
            ready = true
        }
    }

    DetailScaffold(
        title = if (isEditing) "Atualizar área" else "Cadastrar área",
        nav = nav
    ) { padding ->
        if (!ready) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            AreaFormContent(
                initial = AreaForm.from(existing),
                onSubmit = { form -> vm.submit(areaId, form) },
                onSaved = { nav.popBackStack() },
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun AreaFormContent(
    initial: AreaForm,
    onSubmit: (AreaForm) -> String?,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier
) {
    var form by remember { mutableStateOf(initial) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FormTextField("Nome da região", form.name, { form = form.copy(name = it) })
        FormTextField("Endereço ou referência", form.address, { form = form.copy(address = it) })
        FormTextField(
            label = "Latitude",
            value = form.latitude,
            onValueChange = { form = form.copy(latitude = it) },
            hint = "Ex.: -23.5505"
        )
        FormTextField(
            label = "Longitude",
            value = form.longitude,
            onValueChange = { form = form.copy(longitude = it) },
            hint = "Ex.: -46.6333"
        )
        FormTextField(
            label = "Volume de chuva (mm)",
            value = form.rainfall,
            onValueChange = { form = form.copy(rainfall = it) },
            keyboardType = KeyboardType.Decimal
        )
        FormTextField(
            label = "Quantidade de reincidências",
            value = form.recurrences,
            onValueChange = { form = form.copy(recurrences = it) },
            keyboardType = KeyboardType.Number
        )
        FormTextField(
            label = "Profundidade estimada (cm)",
            value = form.waterDepth,
            onValueChange = { form = form.copy(waterDepth = it) },
            keyboardType = KeyboardType.Decimal
        )

        Text("Nível de risco")
        ChipSelector(
            options = RiskLevel.all,
            selected = form.risk,
            onSelect = { form = form.copy(risk = it) }
        )

        Text("Status atual")
        ChipSelector(
            options = AreaStatus.all,
            selected = form.status,
            onSelect = { form = form.copy(status = it) }
        )

        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Button(
            onClick = {
                error = onSubmit(form)
                if (error == null) onSaved()
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Salvar área") }
    }
}
