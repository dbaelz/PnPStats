package de.dbaelz.demo.pnpstats.ui.feature.currency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.dbaelz.demo.pnpstats.R
import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.data.character.toFormattedString
import de.dbaelz.demo.pnpstats.ui.feature.LAUNCHED_EFFECT_KEY
import de.dbaelz.demo.pnpstats.ui.feature.common.CurrencyRectangle
import de.dbaelz.demo.pnpstats.ui.feature.common.LoadingIndicator
import de.dbaelz.demo.pnpstats.ui.feature.currency.CurrencyContract.*
import de.dbaelz.demo.pnpstats.ui.feature.currency.CurrencyContract.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.util.*

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
        is State.CurrencyInfo -> {
            CurrencyInfo(
                info = { CurrencyRectangle(state.currency) },
                input = {
                    val platinumState = rememberTextFieldState()
                    val goldState = rememberTextFieldState()
                    val silverState = rememberTextFieldState()
                    val copperState = rememberTextFieldState()
                    val reasonState = rememberTextFieldState(validator = { true })

                    CurrencyInput(platinumState, goldState, silverState, copperState, reasonState) {
                        val states =
                            listOf(platinumState, goldState, silverState, copperState, reasonState)

                        states.forEach { it.validate() }

                        if (states.none { it.error }) {
                            onEvent(
                                Event.AddCurrency(
                                    state.characterId, Character.Currency(
                                        platinum = platinumState.value.toIntOrNull() ?: 0,
                                        gold = goldState.value.toIntOrNull() ?: 0,
                                        silver = silverState.value.toIntOrNull() ?: 0,
                                        copper = copperState.value.toIntOrNull() ?: 0,
                                    ),
                                    reasonState.value
                                )
                            )

                            states.forEach { it.clear() }
                        }
                    }
                },
                details = {
                    if (state.currencyDetails.isNotEmpty()) {
                        item {
                            Text(
                                text = stringResource(id = R.string.currency_log_title),
                                style = MaterialTheme.typography.h5,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.paddingFromBaseline(bottom = 16.dp)
                            )
                        }

                        items(items = state.currencyDetails) { item ->
                            Divider(Modifier.padding(vertical = 4.dp))
                            CurrencyDetailItem(item)
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun CurrencyInfo(
    info: @Composable () -> Unit,
    input: @Composable () -> Unit,
    details: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            info()
            Spacer(Modifier.height(24.dp))
        }

        item {
            input()
            Spacer(Modifier.height(24.dp))
        }

        details()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CurrencyInput(
    platinumState: TextFieldState,
    goldState: TextFieldState,
    silverState: TextFieldState,
    copperState: TextFieldState,
    reasonState: TextFieldState,
    onButtonClicked: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Text(
        text = stringResource(R.string.currency_input_title),
        style = MaterialTheme.typography.h5
    )

    Spacer(Modifier.height(8.dp))

    TextField(labelText = stringResource(R.string.currency_platinum), state = platinumState)
    TextField(labelText = stringResource(R.string.currency_gold), state = goldState)
    TextField(labelText = stringResource(R.string.currency_silver), state = silverState)
    TextField(labelText = stringResource(R.string.currency_copper), state = copperState)

    TextField(
        labelText = stringResource(R.string.currency_reason_input_label),
        state = reasonState,
        keyboardType = KeyboardType.Text,
        nextKeyboardAction = {
            keyboardController?.hide()
        }
    )

    Spacer(Modifier.height(8.dp))

    Button(
        onClick = onButtonClicked,
        modifier = Modifier
            .height(TextFieldDefaults.MinHeight)
            .fillMaxWidth()
    ) {
        Text(stringResource(R.string.currency_button_text))
    }
}

@Composable
private fun CurrencyDetailItem(item: Pair<Character.Currency, String>) {
    if (item.second.isNotEmpty()) {
        Row(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(0.25f)) {
                Text(text = "${item.first.platinum} pp")
                Text(text = "${item.first.gold} gp")
                Text(text = "${item.first.silver} sp")
                Text(text = "${item.first.copper} cp")
            }

            Text(text = item.second, modifier = Modifier.weight(0.75f))
        }
    } else {
        Text(
            text = item.first.toFormattedString(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun rememberTextFieldState(
    initialValue: String = "",
    validator: TextFieldState.() -> Boolean = currencyTextFieldValidator
): TextFieldState {
    return rememberSaveable(saver = TextFieldState.Saver(validator)) {
        TextFieldState(initialValue, validator)
    }
}

@Composable
private fun TextField(
    modifier: Modifier = Modifier,
    labelText: String,
    state: TextFieldState,
    keyboardType: KeyboardType = KeyboardType.Number,
    nextKeyboardAction: KeyboardActionScope.() -> Unit = {
        defaultKeyboardAction(ImeAction.Next)
    }
) {
    OutlinedTextField(
        value = state.value,
        onValueChange = { state.updateValue(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
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
            .testTag("TEST_${labelText.uppercase(Locale.getDefault())}_TEXTFIELD")
    )
}

private class TextFieldState(
    val initialValue: String,
    val validator: TextFieldState.() -> Boolean
) {
    var value: String by mutableStateOf(initialValue)
        private set

    var error: Boolean by mutableStateOf(false)
        private set

    fun updateValue(newValue: String) {
        value = newValue
        error = false
    }

    fun clear() {
        updateValue("")
        error = false
    }

    fun validate() {
        error = !validator()
    }

    companion object {
        fun Saver(validator: TextFieldState.() -> Boolean) = Saver<TextFieldState, String>(
            save = { it.value },
            restore = { TextFieldState(it, validator) }
        )
    }
}

private val currencyTextFieldValidator: TextFieldState.() -> Boolean = {
    value.isEmpty() || value.toIntOrNull() != null
}