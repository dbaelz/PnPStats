package de.dbaelz.demo.pnpstats.ui.feature.overview

import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.ui.feature.ViewEvent
import de.dbaelz.demo.pnpstats.ui.feature.ViewSideEffect
import de.dbaelz.demo.pnpstats.ui.feature.ViewState

class OverviewContract {
    sealed class State : ViewState {
        object Loading : State()
        data class CharacterInfo(val character: Character) : State()
    }

    sealed class Event : ViewEvent {
        data class ExperienceSelected(val characterId: Int) : Event()
        data class CurrencySelected(val characterId: Int) : Event()
    }

    sealed class Effect : ViewSideEffect {
        object ErrorLoadingCharacter : Effect()

        sealed class Navigation : Effect() {
            object ToCharacters : Navigation()
            data class ToExperience(val id: Int) : Navigation()
            data class ToCurrency(val id: Int) : Navigation()
        }
    }
}