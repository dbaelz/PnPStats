package de.dbaelz.demo.pnpstats.ui.feature.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
    Card(
        elevation = 12.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = character.experience.toString(),
                style = MaterialTheme.typography.h5,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = character.currency.toString(),
                style = MaterialTheme.typography.h5,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = character.notes,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}