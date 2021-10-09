package de.dbaelz.demo.pnpstats.ui.feature.experience

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
class ExperienceViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val characterRepository: CharacterRepository
) : BaseViewModel<ExperienceContract.State, ExperienceContract.Event, ExperienceContract.Effect>() {

    init {
        viewModelScope.launch {
            getCharacter()
        }
    }

    override fun provideInitialState() = ExperienceContract.State.Loading

    override fun handleEvent(event: ExperienceContract.Event) {}

    private suspend fun getCharacter() {
        // TODO: Move into UseCase?
        val characterId = preferenceRepository.getLastCharacterId().first()
        when (val result = characterRepository.getCharacter(characterId)) {
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
