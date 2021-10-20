package de.dbaelz.demo.pnpstats.data.character.usecase

import de.dbaelz.demo.pnpstats.data.PreferenceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetLastCharacterUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) {
    suspend operator fun invoke(id: Int) {
        preferenceRepository.setLastCharacterId(id)
    }
}