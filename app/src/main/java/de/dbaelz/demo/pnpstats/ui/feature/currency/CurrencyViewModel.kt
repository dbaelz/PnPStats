package de.dbaelz.demo.pnpstats.ui.feature.currency

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.PreferenceRepository
import de.dbaelz.demo.pnpstats.data.character.CharacterRepository
import de.dbaelz.demo.pnpstats.data.common.ApiResult
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val characterRepository: CharacterRepository
) : BaseViewModel<CurrencyContract.State, CurrencyContract.Event, CurrencyContract.Effect>() {

    init {
        viewModelScope.launch {
            getCharacter()
        }
    }

    override fun provideInitialState() = CurrencyContract.State.Loading

    override fun handleEvent(event: CurrencyContract.Event) {}

    private suspend fun getCharacter() {
        // TODO: Move into UseCase?
        val characterId = preferenceRepository.getLastCharacterId().first()
        when (val result = characterRepository.getCharacter(characterId)) {
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
