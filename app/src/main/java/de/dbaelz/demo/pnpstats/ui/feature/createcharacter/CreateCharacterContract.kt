package de.dbaelz.demo.pnpstats.ui.feature.createcharacter

import de.dbaelz.demo.pnpstats.ui.feature.ViewEvent
import de.dbaelz.demo.pnpstats.ui.feature.ViewSideEffect
import de.dbaelz.demo.pnpstats.ui.feature.ViewState

class CreateCharacterContract {
    object InitState : ViewState

    sealed class Event : ViewEvent {
        data class CreateCharacter(val name: String) : Event()
        object Cancel : Event()
    }

    object NavigateToCharactersEffect : ViewSideEffect
}