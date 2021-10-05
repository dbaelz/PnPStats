package de.dbaelz.demo.pnpstats.ui.feature.overview

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.dbaelz.demo.pnpstats.ui.feature.LAUNCHED_EFFECT_KEY
import de.dbaelz.demo.pnpstats.ui.feature.common.LoadingIndicator
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
            if (effect is OverviewContract.Effect.Navigation) {
                onNavigation(effect)
            }

            if (effect is OverviewContract.Effect.ErrorLoadingCharacter) {
                // TODO: Show snackbar or a different indicator
            }
        }?.collect()
    }

    when (state) {
        OverviewContract.State.Loading -> LoadingIndicator()
        is OverviewContract.State.CharacterInfo -> CharacterInfo(state.character)
    }
}

@Composable
private fun CharacterInfo(character: de.dbaelz.demo.pnpstats.data.character.Character) {
    // TODO: Only dummy UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Overview screen", style = MaterialTheme.typography.h3)

        Text(
            text = "Character Name: ${character.name}",
            modifier = Modifier.fillMaxWidth()
        )
    }
}