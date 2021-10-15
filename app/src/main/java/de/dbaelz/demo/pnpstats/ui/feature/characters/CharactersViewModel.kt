package de.dbaelz.demo.pnpstats.ui.feature.characters

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.PreferenceRepository
import de.dbaelz.demo.pnpstats.data.character.CharacterRepository
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val characterRepository: CharacterRepository
) : BaseViewModel<CharactersContract.State, CharactersContract.Event, CharactersContract.Effect>() {

    init {
        viewModelScope.launch {
            getCharacters()
        }
    }

    override fun provideInitialState() = CharactersContract.State.Loading

    override fun handleEvent(event: CharactersContract.Event) {
        when (event) {
            is CharactersContract.Event.CharacterSelected -> {
                viewModelScope.launch {
                    preferenceRepository.setLastCharacterId(event.id)

                    setEffect { CharactersContract.Effect.Navigation.ToOverview(event.id) }
                }
            }
            is CharactersContract.Event.CharacterDeleted -> {
                viewModelScope.launch {
                    characterRepository.deleteCharacter(event.id)

                    getCharacters()
                }
            }
        }
    }

    private suspend fun getCharacters() {
        val characters = characterRepository.getCharacters()
        updateState {
            CharactersContract.State.Characters(characters)
        }
    }
}
