package de.dbaelz.demo.pnpstats.ui.feature.currency

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.character.usecase.AdjustCharacterCurrencyUseCase
import de.dbaelz.demo.pnpstats.data.character.usecase.GetLastCharacterUseCase
import de.dbaelz.demo.pnpstats.data.common.ApiResult
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import de.dbaelz.demo.pnpstats.ui.feature.currency.CurrencyContract.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getLastCharacter: GetLastCharacterUseCase,
    private val adjustCharacterCurrency: AdjustCharacterCurrencyUseCase
) : BaseViewModel<State, Event, Effect>() {

    init {
        viewModelScope.launch {
            update()
        }
    }

    private suspend fun update() {
        when (val result = getLastCharacter()) {
            is ApiResult.Success -> {
                updateState {
                    State.CurrencyInfo(result.value.id, result.value.currency)
                }
            }
            is ApiResult.Error -> {
                setEffect { Effect.ErrorLoadingCharacter }
                setEffect { Effect.Navigation.ToCharacters }
            }
        }
    }

    override fun provideInitialState() = State.Loading

    override fun handleEvent(event: Event) {
        if (event is Event.AdjustCurrency) {
            viewModelScope.launch {
                adjustCharacterCurrency(event.characterId, event.currency)

                update()
            }
        }
    }
}
