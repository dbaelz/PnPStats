package de.dbaelz.demo.pnpstats.ui.feature.characters

import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.ui.feature.ViewEvent
import de.dbaelz.demo.pnpstats.ui.feature.ViewSideEffect
import de.dbaelz.demo.pnpstats.ui.feature.ViewState

class CharactersContract {
    sealed class State : ViewState {
        object Loading : State()
        data class Characters(val characters: List<Character>) : State()
    }


    sealed class Event : ViewEvent {
        data class CharacterSelected(val id: Int) : Event()
        data class CharacterDeleted(val id: Int) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            data class ToOverview(val id: Int) : Navigation()
        }
    }
}