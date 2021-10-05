package de.dbaelz.demo.pnpstats.ui.feature.overview

import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.ui.feature.ViewEvent
import de.dbaelz.demo.pnpstats.ui.feature.ViewSideEffect
import de.dbaelz.demo.pnpstats.ui.feature.ViewState

class OverviewContract {
    sealed class State : ViewState {
        object Loading : State()
        data class CharacterSelection(val characters: List<Character>) : State()
        data class CharacterInfo(val character: Character) : State()
    }


    sealed class Event : ViewEvent {
        data class CharacterSelected(val id: Int) : Event()
    }


    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            data class ToCharacterOverview(val id: Int) : Navigation()
        }
    }
}