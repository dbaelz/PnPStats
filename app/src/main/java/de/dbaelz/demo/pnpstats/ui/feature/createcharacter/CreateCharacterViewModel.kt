package de.dbaelz.demo.pnpstats.ui.feature.createcharacter

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.character.CharacterRepository
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : BaseViewModel<CreateCharacterContract.InitState, CreateCharacterContract.Event, CreateCharacterContract.NavigateToCharactersEffect>() {

    override fun provideInitialState() = CreateCharacterContract.InitState

    override fun handleEvent(event: CreateCharacterContract.Event) {
        when (event) {
            CreateCharacterContract.Event.Cancel -> {
                setEffect { CreateCharacterContract.NavigateToCharactersEffect }
            }
            is CreateCharacterContract.Event.SaveCharacter -> {
                viewModelScope.launch {
                    characterRepository.storeCharacter(event.character)

                    setEffect { CreateCharacterContract.NavigateToCharactersEffect }
                }
            }
        }
    }
}
