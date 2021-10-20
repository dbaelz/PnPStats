package de.dbaelz.demo.pnpstats.ui.feature.characters

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.character.usecase.DeleteCharacterUseCase
import de.dbaelz.demo.pnpstats.data.character.usecase.GetCharactersUseCase
import de.dbaelz.demo.pnpstats.data.character.usecase.SetLastCharacterUseCase
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharacters: GetCharactersUseCase,
    private val setLastCharacter: SetLastCharacterUseCase,
    private val deleteCharacter: DeleteCharacterUseCase,
) : BaseViewModel<CharactersContract.State, CharactersContract.Event, CharactersContract.Effect>() {

    init {
        viewModelScope.launch {
            updateCharacters()
        }
    }

    override fun provideInitialState() = CharactersContract.State.Loading

    override fun handleEvent(event: CharactersContract.Event) {
        when (event) {
            is CharactersContract.Event.CharacterSelected -> {
                viewModelScope.launch {
                    setLastCharacter(event.id)

                    setEffect { CharactersContract.Effect.Navigation.ToOverview(event.id) }
                }
            }
            is CharactersContract.Event.CharacterDeleted -> {
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
            CharactersContract.State.Characters(characters)
        }
    }
}
