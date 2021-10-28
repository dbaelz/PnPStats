package de.dbaelz.demo.pnpstats.data.character

import de.dbaelz.demo.pnpstats.data.common.ApiResult
import de.dbaelz.demo.pnpstats.data.experience.ExperienceDao
import de.dbaelz.demo.pnpstats.data.experience.ExperienceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(
    private val characterDao: CharacterDao,
    private val experienceDao: ExperienceDao
) {
    suspend fun getCharacter(characterId: Int): ApiResult<Character> {
        var character: Character? = null

        withContext(Dispatchers.IO) {
            val entity = characterDao.selectById(characterId)
            if (entity != null) {
                val experience = experienceDao.getExperienceForCharacter(entity.id)
                character = entity.toCharacter(experience)
            }
        }

        character?.let {
            return ApiResult.Success(it)
        }

        return ApiResult.Error(
            NoSuchElementException("Character with id $characterId doesn't exit")
        )
    }

    suspend fun getCharacters(): List<Character> {
        val characters: MutableList<Character> = mutableListOf()

        withContext(Dispatchers.IO) {
            characterDao.selectAll().forEach {
                val experience = experienceDao.getExperienceForCharacter(it.id)

                characters.add(it.toCharacter(experience))
            }
        }

        return characters
    }

    suspend fun insertCharacter(character: Character) {
        withContext(Dispatchers.IO) {
            characterDao.insert(character.toEntity())
        }
    }

    suspend fun deleteCharacter(characterId: Int) {
        withContext(Dispatchers.IO) {
            characterDao.delete(characterId)
        }
    }

    suspend fun addExperienceForCharacter(characterId: Int, experience: Int, reason: String = "") {
        withContext(Dispatchers.IO) {
            experienceDao.insert(
                ExperienceEntity(
                    characterId = characterId,
                    experience = experience,
                    reason = reason
                )
            )
        }
    }

    suspend fun updateCharacterCurrency(
        characterId: Int,
        platinum: Int,
        gold: Int,
        silver: Int,
        copper: Int
    ) {
        withContext(Dispatchers.IO) {
            characterDao.updateCurrencyForCharacter(
                characterId = characterId,
                platinum = platinum,
                gold = gold,
                silver = silver,
                copper = copper
            )
        }
    }

    private fun CharacterEntity.toCharacter(experience: Int): Character {
        return Character(
            id,
            name,
            experience,
            Character.Currency(currency.platinum, currency.gold, currency.silver, currency.copper),
            notes
        )
    }

    private fun Character.toEntity(): CharacterEntity {
        return CharacterEntity(
            name = name,
            currency = CharacterEntity.Currency(
                currency.platinum,
                currency.gold,
                currency.silver,
                currency.copper
            ),
            notes = notes
        )
    }
}
