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

    suspend fun storeCharacter(character: Character) {
        withContext(Dispatchers.IO) {
            characterDao.insert(character.toEntity())
        }
    }

    private fun CharacterEntity.toCharacter(): Character {
        return Character(id, name, experience, notes)
    }

    private fun Character.toEntity(): CharacterEntity {
        return CharacterEntity(
            name = name,
            experience = experience,
            currency = CharacterEntity.Currency(),
            notes = notes
        )
    }
}

data class Character(val id: Int, val name: String, val experience: Int, val notes: String = "")
