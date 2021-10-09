package de.dbaelz.demo.pnpstats.ui.feature.currency

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.ui.feature.LAUNCHED_EFFECT_KEY
import de.dbaelz.demo.pnpstats.ui.feature.common.LoadingIndicator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun CurrencyScreen(
    state: CurrencyContract.State,
    effectFlow: Flow<CurrencyContract.Effect>?,
    onEvent: (event: CurrencyContract.Event) -> Unit,
    onNavigation: (navigationEffect: CurrencyContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(LAUNCHED_EFFECT_KEY) {
        effectFlow?.onEach { effect ->
            if (effect is CurrencyContract.Effect.Navigation) {
                onNavigation(effect)
            }

            if (effect is CurrencyContract.Effect.ErrorLoadingCharacter) {
                // TODO: Show snackbar or a different indicator
            }
        }?.collect()
    }

    when (state) {
        CurrencyContract.State.Loading -> LoadingIndicator()
        is CurrencyContract.State.CurrencyInfo -> CurrencyInfo(state.currency)
    }
}

@Composable
private fun CurrencyInfo(currency: Character.Currency) {
    // TODO: Only dummy UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = currency.toString(),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.fillMaxWidth()
        )
    }
}