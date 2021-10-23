package de.dbaelz.demo.pnpstats.data.character

import de.dbaelz.demo.pnpstats.data.common.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(private val characterDao: CharacterDao) {
    suspend fun getCharacter(characterId: Int): ApiResult<Character> {
        var character: Character? = null

        withContext(Dispatchers.IO) {
            val entity = characterDao.selectById(characterId)
            if (entity != null) character = entity.toCharacter()
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
            characterDao.select().forEach {
                characters.add(it.toCharacter())
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

    suspend fun addCharacterExperience(characterId: Int, experience: Int) {
        withContext(Dispatchers.IO) {
            characterDao.addExperienceForCharacter(characterId, experience)
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

    private fun CharacterEntity.toCharacter(): Character {
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
            experience = experience,
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
