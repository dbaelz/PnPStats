package de.dbaelz.demo.pnpstats.ui.feature.createcharacter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import de.dbaelz.demo.pnpstats.ui.feature.LAUNCHED_EFFECT_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun CreateCharacterScreen(
    effectFlow: Flow<CreateCharacterContract.NavigateToCharactersEffect>?,
    onEvent: (event: CreateCharacterContract.Event) -> Unit,
    onNavigation: (navigationEffect: CreateCharacterContract.NavigateToCharactersEffect) -> Unit
) {
    LaunchedEffect(LAUNCHED_EFFECT_KEY) {
        effectFlow?.onEach {
            onNavigation(it)
        }?.collect()
    }

    var textFieldValue by remember { mutableStateOf(TextFieldValue()) }

    // TODO: Dummy UI to create character
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = textFieldValue, onValueChange = { textFieldValue = it })

        Button(onClick = {
            onEvent(CreateCharacterContract.Event.CreateCharacter(textFieldValue.text))
        }) {
            Text("Create Character")
        }
    }
}
