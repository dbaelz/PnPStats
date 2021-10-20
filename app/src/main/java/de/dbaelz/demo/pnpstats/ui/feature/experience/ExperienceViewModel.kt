package de.dbaelz.demo.pnpstats.ui.feature.experience

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.character.usecase.GetLastCharacterUseCase
import de.dbaelz.demo.pnpstats.data.common.ApiResult
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExperienceViewModel @Inject constructor(
    private val getLastCharacter: GetLastCharacterUseCase
) : BaseViewModel<ExperienceContract.State, ExperienceContract.Event, ExperienceContract.Effect>() {

    init {
        viewModelScope.launch {
            when (val result = getLastCharacter()) {
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

    override fun provideInitialState() = ExperienceContract.State.Loading

    override fun handleEvent(event: ExperienceContract.Event) {}
}
