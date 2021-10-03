package de.dbaelz.demo.pnpstats.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor() {
    suspend fun getCharacter(): Character {
        // TODO: Dummy data. Fetch from from database
        return Character(1, "The One", 0)
    }
}

data class Character(val id: Int, val name: String, val experience: Int, val notes: String = "")
