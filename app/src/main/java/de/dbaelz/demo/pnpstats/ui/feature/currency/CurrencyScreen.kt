package de.dbaelz.demo.pnpstats.ui.feature.currency

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.data.character.toFormattedString
import de.dbaelz.demo.pnpstats.ui.feature.LAUNCHED_EFFECT_KEY
import de.dbaelz.demo.pnpstats.ui.feature.common.LoadingIndicator
import de.dbaelz.demo.pnpstats.ui.feature.currency.CurrencyContract.*
import de.dbaelz.demo.pnpstats.ui.feature.currency.CurrencyContract.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun CurrencyScreen(
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
        is State.CurrencyInfo -> CurrencyInfo(state.characterId, state.currency, onEvent)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CurrencyInfo(
    characterId: Int,
    currency: Character.Currency,
    onEvent: (event: Event) -> Unit
) {
    val platinumState = rememberCurrencyTextFieldState()
    val goldState = rememberCurrencyTextFieldState()
    val silverState = rememberCurrencyTextFieldState()
    val copperState = rememberCurrencyTextFieldState()

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = currency.toFormattedString(),
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .border(8.dp, MaterialTheme.colors.primary, RoundedCornerShape(16.dp))
                .padding(32.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(text = "Add/subtract amounts", style = MaterialTheme.typography.h5)

        Spacer(Modifier.height(8.dp))

        CurrencyTextField(labelText = "Platinum", state = platinumState)
        CurrencyTextField(labelText = "Gold", state = goldState)
        CurrencyTextField(labelText = "Silver", state = silverState)
        CurrencyTextField(labelText = "Copper", state = copperState, nextKeyboardAction = {
            keyboardController?.hide()
        })

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                val states = listOf(platinumState, goldState, silverState, copperState)

                states.forEach { it.validate() }

                if (states.none { it.error }) {
                    onEvent(
                        Event.AdjustCurrency(
                            characterId, Character.Currency(
                                platinum = platinumState.value.toIntOrNull() ?: 0,
                                gold = goldState.value.toIntOrNull() ?: 0,
                                silver = silverState.value.toIntOrNull() ?: 0,
                                copper = copperState.value.toIntOrNull() ?: 0,
                            )
                        )
                    )

                    states.forEach { it.reset() }
                }
            },
            modifier = Modifier
                .height(TextFieldDefaults.MinHeight)
                .fillMaxWidth()
        ) {
            Text("Adjust amounts")
        }
    }
}

@Composable
private fun rememberCurrencyTextFieldState(initialValue: String = ""): CurrencyTextFieldState {
    return rememberSaveable(saver = CurrencyTextFieldState.Saver()) {
        CurrencyTextFieldState(initialValue)
    }
}

@Composable
private fun CurrencyTextField(
    modifier: Modifier = Modifier,
    labelText: String,
    state: CurrencyTextFieldState,
    nextKeyboardAction: KeyboardActionScope.() -> Unit = {
        defaultKeyboardAction(ImeAction.Next)
    }
) {
    OutlinedTextField(
        value = state.value,
        onValueChange = { state.updateValue(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = nextKeyboardAction
        ),
        label = { Text(labelText) },
        isError = state.error,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}

private class CurrencyTextFieldState(val initialValue: String) {
    var value: String by mutableStateOf(initialValue)
        private set

    var error: Boolean by mutableStateOf(false)
        private set

    fun updateValue(newValue: String) {
        value = newValue
        error = false
    }

    fun reset() {
        updateValue(initialValue)
        error = false
    }

    fun validate() {
        error = if (value.isEmpty()) {
            false
        } else {
            value.toIntOrNull() == null
        }
    }

    companion object {
        fun Saver() = androidx.compose.runtime.saveable.Saver<CurrencyTextFieldState, String>(
            save = { it.value },
            restore = { CurrencyTextFieldState(it) }
        )
    }
}