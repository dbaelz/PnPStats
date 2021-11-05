package de.dbaelz.demo.pnpstats.data.currency.usecase

import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.data.character.CharacterRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddCharacterCurrencyUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(characterId: Int, currency: Character.Currency, reason: String) {
        characterRepository.addCurrencyForCharacter(
            characterId,
            currency.platinum,
            currency.gold,
            currency.silver,
            currency.copper,
            reason
        )
    }
}