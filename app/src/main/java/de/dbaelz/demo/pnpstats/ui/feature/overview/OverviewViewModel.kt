package de.dbaelz.demo.pnpstats.ui.feature.overview

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.PreferenceRepository
import de.dbaelz.demo.pnpstats.data.character.CharacterRepository
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val characterRepository: CharacterRepository
) :
    BaseViewModel<OverviewContract.Event, OverviewContract.State, OverviewContract.Effect>() {

    init {
        viewModelScope.launch {
            // TODO: Move into UseCase?
            val characterId = preferenceRepository.getLastCharacterId().first()
            try {
                val character = characterRepository.getCharacter(characterId)
                updateState {
                    OverviewContract.State.CharacterInfo(character)
                }
            } catch (exception: NoSuchElementException) {
                val characters = characterRepository.getCharacters()
                updateState {
                    OverviewContract.State.CharacterSelection(characters)
                }
            }
        }
    }

    override fun provideInitialState() = OverviewContract.State.Loading
}
