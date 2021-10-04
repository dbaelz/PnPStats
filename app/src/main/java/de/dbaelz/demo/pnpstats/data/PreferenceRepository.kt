package de.dbaelz.demo.pnpstats.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PreferenceRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.dataStore by preferencesDataStore("preferences")

    fun getLastCharacterId(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[LAST_CHARACTER_ID] ?: -1
        }
    }

    suspend fun setLastCharacterId(id: Int) {
        context.dataStore.edit { settings ->
            settings[LAST_CHARACTER_ID] = id
        }
    }

    companion object {
        private val LAST_CHARACTER_ID = intPreferencesKey("last_character_id")
    }
}