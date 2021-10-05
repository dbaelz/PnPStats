package de.dbaelz.demo.pnpstats.data.character

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(private val characterDao: CharacterDao) {
    /**
     * Returns the character with the given id.
     *
     * @param characterId The character id of character to select
     * @throws NoSuchElementException When no character was found
     */
    suspend fun getCharacter(characterId: Int): Character {
        var character: Character

        withContext(Dispatchers.IO) {
            val entity = characterDao.selectById(characterId)
            character = entity?.toCharacter() ?: throw NoSuchElementException()
        }

        return character
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

    suspend fun addCharacter(name: String) {
        withContext(Dispatchers.IO) {
            characterDao.insert(CharacterEntity(name = name))
        }
    }

    private fun CharacterEntity.toCharacter(): Character {
        return Character(id, name, experience, notes)
    }
}

data class Character(val id: Int, val name: String, val experience: Int, val notes: String = "")
