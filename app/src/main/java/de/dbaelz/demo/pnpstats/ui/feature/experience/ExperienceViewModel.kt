package de.dbaelz.demo.pnpstats.ui.feature.experience

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.character.usecase.GetLastCharacterUseCase
import de.dbaelz.demo.pnpstats.data.common.ApiResult
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import de.dbaelz.demo.pnpstats.ui.feature.experience.ExperienceContract.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExperienceViewModel @Inject constructor(
    private val getLastCharacter: GetLastCharacterUseCase
) : BaseViewModel<State, Event, Effect>() {

    init {
        viewModelScope.launch {
            when (val result = getLastCharacter()) {
                is ApiResult.Success -> {
                    updateState {
                        State.ExperienceInfo(result.value.experience)
                    }
                }
                is ApiResult.Error -> {
                    setEffect { Effect.ErrorLoadingCharacter }
                    setEffect { Effect.Navigation.ToCharacters }
                }
            }
        }
    }

    override fun provideInitialState() = State.Loading

    override fun handleEvent(event: Event) {
        if (event is Event.ExperienceAdded) {
            viewModelScope.launch {
                // TODO: Persist the value and update the state afterwards
                updateState {
                    State.ExperienceInfo(event.value)
                }
            }
        }
    }
}
