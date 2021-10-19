package de.dbaelz.demo.pnpstats.ui.feature.experience

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.dbaelz.demo.pnpstats.ui.feature.LAUNCHED_EFFECT_KEY
import de.dbaelz.demo.pnpstats.ui.feature.common.LoadingIndicator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ExperienceScreen(
    state: ExperienceContract.State,
    effectFlow: Flow<ExperienceContract.Effect>?,
    onEvent: (event: ExperienceContract.Event) -> Unit,
    onNavigation: (navigationEffect: ExperienceContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(LAUNCHED_EFFECT_KEY) {
        effectFlow?.onEach { effect ->
            if (effect is ExperienceContract.Effect.Navigation) {
                onNavigation(effect)
            }

            if (effect is ExperienceContract.Effect.ErrorLoadingCharacter) {
                // TODO: Show snackbar or a different indicator
            }
        }?.collect()
    }

    when (state) {
        ExperienceContract.State.Loading -> LoadingIndicator()
        is ExperienceContract.State.ExperienceInfo -> ExperienceInfo(state.experience)
    }
}

@Composable
private fun ExperienceInfo(experience: Int) {
    // TODO: Only dummy UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val icon = Icons.Default.Info
        Icon(
            imageVector = icon,
            contentDescription = icon.name,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(48.dp)
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "$experience XP",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}