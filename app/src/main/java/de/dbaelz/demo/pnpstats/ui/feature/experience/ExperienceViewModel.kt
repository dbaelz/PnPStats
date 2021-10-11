package de.dbaelz.demo.pnpstats.ui.feature.experience

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.character.GetCharacterUseCase
import de.dbaelz.demo.pnpstats.data.common.ApiResult
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExperienceViewModel @Inject constructor(
    private val getCharacterUseCase: GetCharacterUseCase
) : BaseViewModel<ExperienceContract.State, ExperienceContract.Event, ExperienceContract.Effect>() {

    init {
        viewModelScope.launch {
            getCharacter()
        }
    }

    override fun provideInitialState() = ExperienceContract.State.Loading

    override fun handleEvent(event: ExperienceContract.Event) {}

    private suspend fun getCharacter() {
        when (val result = getCharacterUseCase.execute()) {
            is ApiResult.Success -> {
                updateState {
                    ExperienceContract.State.ExperienceInfo(result.value.experience)
                }
            }
            is ApiResult.Error -> {
                setEffect { ExperienceContract.Effect.ErrorLoadingCharacter }
                setEffect { ExperienceContract.Effect.Navigation.ToCharacters }
            }
        }
    }
}
