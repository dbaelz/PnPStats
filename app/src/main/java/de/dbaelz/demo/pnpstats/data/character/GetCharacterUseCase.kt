package de.dbaelz.demo.pnpstats.data.character

import de.dbaelz.demo.pnpstats.data.PreferenceRepository
import de.dbaelz.demo.pnpstats.data.common.ApiResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCharacterUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(): ApiResult<Character> {
        val characterId = preferenceRepository.getLastCharacterId().first()
        return characterRepository.getCharacter(characterId)
    }
}