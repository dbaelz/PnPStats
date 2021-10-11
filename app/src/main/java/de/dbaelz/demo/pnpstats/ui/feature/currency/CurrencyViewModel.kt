package de.dbaelz.demo.pnpstats.ui.feature.currency

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.character.GetCharacterUseCase
import de.dbaelz.demo.pnpstats.data.common.ApiResult
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getCharacterUseCase: GetCharacterUseCase
) : BaseViewModel<CurrencyContract.State, CurrencyContract.Event, CurrencyContract.Effect>() {

    init {
        viewModelScope.launch {
            getCharacter()
        }
    }

    override fun provideInitialState() = CurrencyContract.State.Loading

    override fun handleEvent(event: CurrencyContract.Event) {}

    private suspend fun getCharacter() {
        when (val result = getCharacterUseCase.execute()) {
            is ApiResult.Success -> {
                updateState {
                    CurrencyContract.State.CurrencyInfo(result.value.currency)
                }
            }
            is ApiResult.Error -> {
                setEffect { CurrencyContract.Effect.ErrorLoadingCharacter }
                setEffect { CurrencyContract.Effect.Navigation.ToCharacters }
            }
        }
    }
}
