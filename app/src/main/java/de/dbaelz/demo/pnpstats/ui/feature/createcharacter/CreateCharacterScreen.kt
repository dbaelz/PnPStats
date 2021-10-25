package de.dbaelz.demo.pnpstats.ui.feature.createcharacter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import de.dbaelz.demo.pnpstats.ui.feature.LAUNCHED_EFFECT_KEY
import de.dbaelz.demo.pnpstats.ui.feature.createcharacter.CreateCharacterContract.Event
import de.dbaelz.demo.pnpstats.ui.feature.createcharacter.CreateCharacterContract.NavigateToCharactersEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun CreateCharacterScreen(
    effectFlow: Flow<NavigateToCharactersEffect>?,
    onEvent: (event: Event) -> Unit,
    onNavigation: (navigationEffect: NavigateToCharactersEffect) -> Unit
) {
    LaunchedEffect(LAUNCHED_EFFECT_KEY) {
        effectFlow?.onEach {
            onNavigation(it)
        }?.collect()
    }

    var textFieldValue by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                onEvent(Event.CreateCharacter(textFieldValue.text))
            },
            modifier = Modifier
                .height(TextFieldDefaults.MinHeight)
                .fillMaxWidth()
        ) {
            Text("Create Character")
        }
    }
}
