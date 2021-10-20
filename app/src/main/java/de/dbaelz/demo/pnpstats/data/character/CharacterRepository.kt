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

data class Character(
    val id: Int = 0,
    val name: String,
    val experience: Int = 0,
    val currency: Currency = Currency(),
    val notes: String = ""
) {
    data class Currency(
        val platinum: Int = 0,
        val gold: Int = 0,
        val silver: Int = 0,
        val copper: Int = 0
    )
}

fun Character.Currency.toFormattedString(): String {
    return "$platinum pp  • $gold gp • $silver sp  • $copper cp"
}
