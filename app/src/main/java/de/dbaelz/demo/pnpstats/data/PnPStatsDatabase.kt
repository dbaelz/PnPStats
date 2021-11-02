package de.dbaelz.demo.pnpstats.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.dbaelz.demo.pnpstats.data.character.CharacterDao
import de.dbaelz.demo.pnpstats.data.character.CharacterEntity
import de.dbaelz.demo.pnpstats.data.currency.CurrencyDao
import de.dbaelz.demo.pnpstats.data.currency.CurrencyEntity
import de.dbaelz.demo.pnpstats.data.experience.ExperienceDao
import de.dbaelz.demo.pnpstats.data.experience.ExperienceEntity

@Database(
    entities = [CharacterEntity::class, ExperienceEntity::class, CurrencyEntity::class],
    version = 3
)
abstract class PnPStatsDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao

    abstract fun experienceDao(): ExperienceDao

    abstract fun currencyDao(): CurrencyDao
}