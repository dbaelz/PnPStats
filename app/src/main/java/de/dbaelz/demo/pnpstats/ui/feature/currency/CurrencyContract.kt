package de.dbaelz.demo.pnpstats.ui.feature.currency

import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.ui.feature.ViewEvent
import de.dbaelz.demo.pnpstats.ui.feature.ViewSideEffect
import de.dbaelz.demo.pnpstats.ui.feature.ViewState

class CurrencyContract {
    sealed class State : ViewState {
        object Loading : State()
        data class CurrencyInfo(
            val characterId: Int,
            val currency: Character.Currency,
            val currencyDetails: List<Pair<Character.Currency, String>>
        ) : State()
    }

    sealed class Event : ViewEvent {
        data class AddCurrency(
            val characterId: Int,
            val currency: Character.Currency,
            val reason: String
        ) : Event()
    }

    sealed class Effect : ViewSideEffect {
        object ErrorLoadingCharacter : Effect()

        sealed class Navigation : Effect() {
            object ToCharacters : Navigation()
        }
    }
}