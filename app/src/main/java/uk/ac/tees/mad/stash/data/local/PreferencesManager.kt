package uk.ac.tees.mad.stash.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "stash_preferences")

class PreferencesManager(private val context: Context) {

    companion object {
        private val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        private val LAST_ACTIVE_TIMESTAMP = longPreferencesKey("last_active_timestamp")
    }

    // Biometric Preference
    val biometricEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[BIOMETRIC_ENABLED] ?: false
        }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[BIOMETRIC_ENABLED] = enabled
        }
    }

    // Last Active Timestamp
    val lastActiveTimestamp: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[LAST_ACTIVE_TIMESTAMP] ?: 0L
        }

    suspend fun setLastActiveTimestamp(timestamp: Long) {
        context.dataStore.edit { preferences ->
            preferences[LAST_ACTIVE_TIMESTAMP] = timestamp
        }
    }
}
