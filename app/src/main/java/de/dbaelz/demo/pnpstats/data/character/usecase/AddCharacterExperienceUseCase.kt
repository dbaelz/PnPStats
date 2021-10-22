package de.dbaelz.demo.pnpstats.data.character.usecase

import de.dbaelz.demo.pnpstats.data.character.CharacterRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddCharacterExperienceUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(characterId: Int, experience: Int) {
        characterRepository.updateCharacterExperience(characterId, experience)
    }
}