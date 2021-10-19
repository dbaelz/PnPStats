package de.dbaelz.demo.pnpstats.ui.feature.overview

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.dbaelz.demo.pnpstats.data.character.toFormattedString
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
                style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            CharacterDetail(Icons.Default.Info, "${character.experience} XP")

            Spacer(Modifier.height(24.dp))

            CharacterDetail(
                Icons.Default.AccountBox,
                character.currency.toFormattedString()
            )

            Spacer(Modifier.height(24.dp))

            CharacterDetail(Icons.Default.Email, character.notes)
        }
    }
}

@Composable
private fun CharacterDetail(icon: ImageVector, text: String) {
    if (text.isNotEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = icon.name,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(48.dp)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}