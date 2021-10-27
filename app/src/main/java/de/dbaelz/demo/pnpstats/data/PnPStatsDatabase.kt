package de.dbaelz.demo.pnpstats.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.dbaelz.demo.pnpstats.data.character.CharacterDao
import de.dbaelz.demo.pnpstats.data.character.CharacterEntity
import de.dbaelz.demo.pnpstats.data.experience.ExperienceDao
import de.dbaelz.demo.pnpstats.data.experience.ExperienceEntity

@Database(entities = [CharacterEntity::class, ExperienceEntity::class], version = 2)
abstract class PnPStatsDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao

    abstract fun experienceDao(): ExperienceDao
}