package de.dbaelz.demo.pnpstats.ui.feature.experience

import de.dbaelz.demo.pnpstats.ui.feature.ViewEvent
import de.dbaelz.demo.pnpstats.ui.feature.ViewSideEffect
import de.dbaelz.demo.pnpstats.ui.feature.ViewState

class ExperienceContract {
    sealed class State : ViewState {
        object Loading : State()
        data class ExperienceInfo(val experience: Int) : State()
    }

    object Event : ViewEvent

    sealed class Effect : ViewSideEffect {
        object ErrorLoadingCharacter : Effect()

        sealed class Navigation : Effect() {
            object ToCharacters : Navigation()
        }
    }
}