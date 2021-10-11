package de.dbaelz.demo.pnpstats.ui.feature.overview

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.character.GetCharacterUseCase
import de.dbaelz.demo.pnpstats.data.common.ApiResult
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val getCharacterUseCase: GetCharacterUseCase
) : BaseViewModel<OverviewContract.State, OverviewContract.Event, OverviewContract.Effect>() {

    init {
        viewModelScope.launch {
            getCharacter()
        }
    }

    override fun provideInitialState() = OverviewContract.State.Loading

    override fun handleEvent(event: OverviewContract.Event) {}

    private suspend fun getCharacter() {
        when (val result = getCharacterUseCase.execute()) {
            is ApiResult.Success -> {
                updateState {
                    OverviewContract.State.CharacterInfo(result.value)
                }
            }
            is ApiResult.Error -> {
                setEffect { OverviewContract.Effect.ErrorLoadingCharacter }
                setEffect { OverviewContract.Effect.Navigation.ToCharacters }
            }
        }
    }
}
