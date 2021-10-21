package de.dbaelz.demo.pnpstats.ui.feature.characters

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.character.usecase.DeleteCharacterUseCase
import de.dbaelz.demo.pnpstats.data.character.usecase.GetCharactersUseCase
import de.dbaelz.demo.pnpstats.data.character.usecase.SetLastCharacterUseCase
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import de.dbaelz.demo.pnpstats.ui.feature.characters.CharactersContract.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharacters: GetCharactersUseCase,
    private val setLastCharacter: SetLastCharacterUseCase,
    private val deleteCharacter: DeleteCharacterUseCase,
) : BaseViewModel<State, Event, Effect>() {

    init {
        viewModelScope.launch {
            updateCharacters()
        }
    }

    override fun provideInitialState() = State.Loading

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.CharacterSelected -> {
                viewModelScope.launch {
                    setLastCharacter(event.id)

                    setEffect { Effect.Navigation.ToOverview(event.id) }
                }
            }
            is Event.CharacterDeleted -> {
                viewModelScope.launch {
                    deleteCharacter(event.id)

                    updateCharacters()
                }
            }
        }
    }

    private suspend fun updateCharacters() {
        val characters = getCharacters()
        updateState {
            State.Characters(characters)
        }
    }
}
