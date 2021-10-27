package de.dbaelz.demo.pnpstats.data.experience

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExperienceDao {
    @Insert
    fun insert(vararg experienceEntity: ExperienceEntity)

    @Query("SELECT SUM(experience) FROM ExperienceEntity WHERE characterId = :characterId")
    fun getExperienceForCharacter(characterId: Int): Int
}