package de.dbaelz.demo.pnpstats.ui.feature.createcharacter

import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.ui.feature.ViewEvent
import de.dbaelz.demo.pnpstats.ui.feature.ViewSideEffect
import de.dbaelz.demo.pnpstats.ui.feature.ViewState

class CreateCharacterContract {
    object InitState : ViewState


    sealed class Event : ViewEvent {
        data class SaveCharacter(val character: Character) : Event()
        object Cancel : Event()
    }

    object NavigateToCharactersEffect : ViewSideEffect
}