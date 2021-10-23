package de.dbaelz.demo.pnpstats.data.character.usecase

import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.data.character.CharacterRepository
import de.dbaelz.demo.pnpstats.data.common.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdjustCharacterCurrencyUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(characterId: Int, currency: Character.Currency) {
        val character = characterRepository.getCharacter(characterId)

        if (character is ApiResult.Success) {
            val currentCurrency = character.value.currency

            characterRepository.updateCharacterCurrency(
                characterId,
                maxOf(currentCurrency.platinum + currency.platinum, 0),
                maxOf(currentCurrency.gold + currency.gold, 0),
                maxOf(currentCurrency.silver + currency.silver, 0),
                maxOf(currentCurrency.copper + currency.copper, 0)
            )
        }
    }
}