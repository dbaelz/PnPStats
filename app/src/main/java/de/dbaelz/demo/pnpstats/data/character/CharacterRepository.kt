package de.dbaelz.demo.pnpstats.data.character

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(private val characterDao: CharacterDao) {
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
}

data class Character(val name: String, val experience: Int, val notes: String = "")
