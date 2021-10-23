package de.dbaelz.demo.pnpstats.data.character.usecase

import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.data.character.CharacterRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateCharacterCurrencyUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(characterId: Int, currency: Character.Currency) {
        // TODO: Check if currencies are >= 0 after change?
        characterRepository.updateCharacterCurrency(characterId, currency)
    }
}