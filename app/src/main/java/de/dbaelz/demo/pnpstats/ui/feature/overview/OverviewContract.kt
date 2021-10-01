package de.dbaelz.demo.pnpstats.ui.feature.overview

import de.dbaelz.demo.pnpstats.ui.feature.ViewEvent
import de.dbaelz.demo.pnpstats.ui.feature.ViewSideEffect
import de.dbaelz.demo.pnpstats.ui.feature.ViewState

class OverviewContract {
    // TODO: Only dummy events
    sealed class Event : ViewEvent {
        object ExperienceSelection : Event()
        object CurrencySelection : Event()
    }

    // TODO: Only dummy state
    data class State(val characterName: String) : ViewState

    // TODO: Only dummy effect
    sealed class Effect : ViewSideEffect {
        object LoadData : Effect()
    }
}