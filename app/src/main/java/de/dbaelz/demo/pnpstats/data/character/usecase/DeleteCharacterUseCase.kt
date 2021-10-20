package de.dbaelz.demo.pnpstats.data.character.usecase

import de.dbaelz.demo.pnpstats.data.character.CharacterRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteCharacterUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(id: Int) {
        return characterRepository.deleteCharacter(id)
    }
}