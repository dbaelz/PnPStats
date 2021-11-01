package de.dbaelz.demo.pnpstats.ui.feature.experience

import de.dbaelz.demo.pnpstats.ui.feature.ViewEvent
import de.dbaelz.demo.pnpstats.ui.feature.ViewSideEffect
import de.dbaelz.demo.pnpstats.ui.feature.ViewState

class ExperienceContract {
    sealed class State : ViewState {
        object Loading : State()
        data class ExperienceInfo(
            val characterId: Int,
            val experience: Int,
            val experienceDetails: List<Pair<Int, String>>
        ) : State()
    }

    sealed class Event : ViewEvent {
        data class AddExperience(
            val characterId: Int,
            val experience: Int,
            val reason: String = ""
        ) : Event()
    }

    sealed class Effect : ViewSideEffect {
        object ErrorLoadingCharacter : Effect()

        sealed class Navigation : Effect() {
            object ToCharacters : Navigation()
        }
    }
}