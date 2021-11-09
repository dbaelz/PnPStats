package de.dbaelz.demo.pnpstats.data.currency.usecase

import de.dbaelz.demo.pnpstats.data.character.Character
import de.dbaelz.demo.pnpstats.data.character.CharacterRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCharacterCurrencyDetailsUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(characterId: Int): List<Pair<Character.Currency, String>> {
        return characterRepository.getCurrencyDetailsForCharacter(characterId)
    }
}