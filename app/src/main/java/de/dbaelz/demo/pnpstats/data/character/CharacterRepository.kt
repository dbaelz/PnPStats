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
            character = if (entity != null) {
                Character(entity.name, entity.experience)
            } else {
                throw NoSuchElementException()
            }
        }

        return character
    }

    suspend fun getCharacters(): List<Character> {
        val characters: MutableList<Character> = mutableListOf()

        withContext(Dispatchers.IO) {
            characterDao.select().forEach {
                characters.add(
                    Character(it.name, it.experience, it.notes)
                )
            }
        }

        return characters
    }
}

data class Character(val name: String, val experience: Int, val notes: String = "")
