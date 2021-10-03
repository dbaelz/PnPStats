package de.dbaelz.demo.pnpstats.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.dbaelz.demo.pnpstats.data.character.CharacterDao
import de.dbaelz.demo.pnpstats.data.character.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1)
abstract class PnPStatsDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}