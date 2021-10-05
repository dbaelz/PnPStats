package de.dbaelz.demo.pnpstats.ui.feature.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.dbaelz.demo.pnpstats.ui.feature.LAUNCHED_EFFECT_KEY
import de.dbaelz.demo.pnpstats.ui.feature.characterselection.CharacterSelection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun OverviewScreen(
    state: OverviewContract.State,
    effectFlow: Flow<OverviewContract.Effect>?,
    onEvent: (event: OverviewContract.Event) -> Unit,
    onNavigation: (navigationEffect: OverviewContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(LAUNCHED_EFFECT_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is OverviewContract.Effect.Navigation.ToCharacterOverview -> onNavigation(effect)
            }
        }?.collect()
    }

    when (state) {
        OverviewContract.State.Loading -> Loading()
        is OverviewContract.State.CharacterInfo -> CharacterInfo(state.character)
        is OverviewContract.State.CharacterSelection -> CharacterSelection(state.characters) {
            onEvent(OverviewContract.Event.CharacterSelected(it))
        }
    }
}

@Composable
private fun Loading() {
    // TODO: Only dummy UI
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Loading...", style = MaterialTheme.typography.h3)
    }
}

@Composable
private fun CharacterInfo(character: de.dbaelz.demo.pnpstats.data.character.Character) {
    // TODO: Only dummy UI
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Overview screen", style = MaterialTheme.typography.h3)

        Text("Character Name: ${character.name}")
    }
}