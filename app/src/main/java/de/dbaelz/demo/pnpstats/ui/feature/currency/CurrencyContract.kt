package de.dbaelz.demo.pnpstats.ui.feature.currency

import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.ui.feature.ViewEvent
import de.dbaelz.demo.pnpstats.ui.feature.ViewSideEffect
import de.dbaelz.demo.pnpstats.ui.feature.ViewState

class CurrencyContract {
    sealed class State : ViewState {
        object Loading : State()
        data class CurrencyInfo(val currency: Character.Currency) : State()
    }

    object Event : ViewEvent

    sealed class Effect : ViewSideEffect {
        object ErrorLoadingCharacter : Effect()

        sealed class Navigation : Effect() {
            object ToCharacters : Navigation()
            data class ToOverview(val id: Int) : Navigation()
            data class ToExperience(val id: Int) : Navigation()
        }
    }
}