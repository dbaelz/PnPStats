package de.dbaelz.demo.pnpstats.ui.feature.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.dbaelz.demo.pnpstats.ui.feature.LAUNCHED_EFFECT_KEY
import de.dbaelz.demo.pnpstats.ui.feature.common.CurrencyRectangle
import de.dbaelz.demo.pnpstats.ui.feature.common.ExperienceCircle
import de.dbaelz.demo.pnpstats.ui.feature.common.LoadingIndicator
import de.dbaelz.demo.pnpstats.ui.feature.overview.OverviewContract.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun OverviewScreen(
    state: State,
    effectFlow: Flow<Effect>?,
    onEvent: (event: Event) -> Unit,
    onNavigation: (navigationEffect: Effect.Navigation) -> Unit
) {
    LaunchedEffect(LAUNCHED_EFFECT_KEY) {
        effectFlow?.onEach { effect ->
            if (effect is Effect.Navigation) {
                onNavigation(effect)
            }

            if (effect is Effect.ErrorLoadingCharacter) {
                // TODO: Show snackbar or a different indicator
            }
        }?.collect()
    }

    when (state) {
        State.Loading -> LoadingIndicator()
        is State.CharacterInfo -> CharacterInfo(
            character = state.character,
            onExperienceSelected = { onEvent(Event.ExperienceSelected(it)) },
            onCurrencySelected = { onEvent(Event.CurrencySelected(it)) })
    }
}

@Composable
private fun CharacterInfo(
    character: de.dbaelz.demo.pnpstats.data.character.Character,
    onExperienceSelected: (Int) -> Unit = {},
    onCurrencySelected: (Int) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = character.name,
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Box(Modifier.clickable { onExperienceSelected(character.id) }) {
            ExperienceCircle(character.experience)
        }

        Spacer(Modifier.height(24.dp))

        Box(Modifier.clickable { onCurrencySelected(character.id) }) {
            CurrencyRectangle(character.currency)
        }

        Spacer(Modifier.height(24.dp))

        CharacterDetail(Icons.Default.Email, character.notes)
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