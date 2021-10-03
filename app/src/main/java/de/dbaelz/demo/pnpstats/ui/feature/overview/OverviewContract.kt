package de.dbaelz.demo.pnpstats.ui.feature.overview

import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.ui.feature.ViewEvent
import de.dbaelz.demo.pnpstats.ui.feature.ViewSideEffect
import de.dbaelz.demo.pnpstats.ui.feature.ViewState

class OverviewContract {
    // TODO: Only dummy events
    sealed class Event : ViewEvent {
        object ExperienceSelection : Event()
        object CurrencySelection : Event()
    }

    sealed class State : ViewState {
        object Loading : State()
        data class CharacterSelection(val characters: List<Character>) : State()
        data class CharacterInfo(val character: Character) : State()
    }


    // TODO: Only dummy effect
    sealed class Effect : ViewSideEffect {
        object LoadData : Effect()
    }
}