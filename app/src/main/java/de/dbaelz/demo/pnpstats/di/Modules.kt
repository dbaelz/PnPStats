package de.dbaelz.demo.pnpstats.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.dbaelz.demo.pnpstats.data.PnPStatsDatabase
import de.dbaelz.demo.pnpstats.data.character.CharacterDao
import de.dbaelz.demo.pnpstats.data.experience.ExperienceDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun providePnPStatsDatabase(@ApplicationContext appContext: Context): PnPStatsDatabase {
        return Room
            .databaseBuilder(
                appContext,
                PnPStatsDatabase::class.java,
                "database"
            )
            .fallbackToDestructiveMigration() // TODO: Remove when schema is stabilized
            .build()
    }

    @Provides
    fun provideCharacterDao(database: PnPStatsDatabase): CharacterDao {
        return database.characterDao()
    }

    @Provides
    fun provideExperienceDao(database: PnPStatsDatabase): ExperienceDao {
        return database.experienceDao()
    }
}