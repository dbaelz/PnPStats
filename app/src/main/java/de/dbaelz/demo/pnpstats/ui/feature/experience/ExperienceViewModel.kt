package de.dbaelz.demo.pnpstats.ui.feature.experience

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.character.usecase.GetLastCharacterUseCase
import de.dbaelz.demo.pnpstats.data.character.usecase.AddCharacterExperienceUseCase
import de.dbaelz.demo.pnpstats.data.common.ApiResult
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import de.dbaelz.demo.pnpstats.ui.feature.experience.ExperienceContract.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExperienceViewModel @Inject constructor(
    private val getLastCharacter: GetLastCharacterUseCase,
    private val addCharacterExperience: AddCharacterExperienceUseCase
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
                    State.ExperienceInfo(result.value.id, result.value.experience)
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
        if (event is Event.AddExperience) {
            viewModelScope.launch {
                addCharacterExperience(event.characterId, event.experience)

                update()
            }
        }
    }
}
