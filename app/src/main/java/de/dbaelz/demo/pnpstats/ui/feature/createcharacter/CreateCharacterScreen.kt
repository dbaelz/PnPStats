package de.dbaelz.demo.pnpstats.ui.feature.createcharacter

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    // TODO: UI to crate character
    Text("Create Character")
}