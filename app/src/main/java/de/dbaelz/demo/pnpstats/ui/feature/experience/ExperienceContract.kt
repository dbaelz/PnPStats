package de.dbaelz.demo.pnpstats.ui.feature.experience

import de.dbaelz.demo.pnpstats.ui.feature.ViewEvent
import de.dbaelz.demo.pnpstats.ui.feature.ViewSideEffect
import de.dbaelz.demo.pnpstats.ui.feature.ViewState

class ExperienceContract {
    sealed class State : ViewState {
        object Loading : State()
        data class ExperienceInfo(val characterId: Int, val experience: Int) : State()
    }

    sealed class Event : ViewEvent {
        data class AddExperience(val characterId: Int, val experience: Int) : Event()
    }

    sealed class Effect : ViewSideEffect {
        object ErrorLoadingCharacter : Effect()

        sealed class Navigation : Effect() {
            object ToCharacters : Navigation()
        }
    }
}