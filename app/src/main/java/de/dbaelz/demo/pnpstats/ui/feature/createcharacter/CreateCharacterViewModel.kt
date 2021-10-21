package de.dbaelz.demo.pnpstats.ui.feature.createcharacter

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.data.character.usecase.CreateCharacterUseCase
import de.dbaelz.demo.pnpstats.ui.feature.BaseViewModel
import de.dbaelz.demo.pnpstats.ui.feature.createcharacter.CreateCharacterContract.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCharacterViewModel @Inject constructor(
    private val createCharacter: CreateCharacterUseCase
) : BaseViewModel<InitState, Event, NavigateToCharactersEffect>() {

    override fun provideInitialState() = InitState

    override fun handleEvent(event: Event) {
        when (event) {
            Event.Cancel -> {
                setEffect { NavigateToCharactersEffect }
            }
            is Event.CreateCharacter -> {
                viewModelScope.launch {
                    createCharacter(Character(name = event.name))

                    setEffect { NavigateToCharactersEffect }
                }
            }
        }
    }
}
