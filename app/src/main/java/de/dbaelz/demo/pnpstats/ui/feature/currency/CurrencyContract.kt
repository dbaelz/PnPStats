package de.dbaelz.demo.pnpstats.ui.feature.currency

import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.ui.feature.ViewEvent
import de.dbaelz.demo.pnpstats.ui.feature.ViewSideEffect
import de.dbaelz.demo.pnpstats.ui.feature.ViewState

class CurrencyContract {
    sealed class State : ViewState {
        object Loading : State()
        data class CurrencyInfo(val characterId: Int, val currency: Character.Currency) : State()
    }

    sealed class Event : ViewEvent {
        data class UpdateCurrencyAmounts(val characterId: Int, val currency: Character.Currency) : Event()
    }

    sealed class Effect : ViewSideEffect {
        object ErrorLoadingCharacter : Effect()

        sealed class Navigation : Effect() {
            object ToCharacters : Navigation()
        }
    }
}