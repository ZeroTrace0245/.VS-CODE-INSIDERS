package com.example.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONObject

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    private val GITHUB_TOKEN_KEY = stringPreferencesKey("github_token")
    private val AI_ENABLED_KEY = booleanPreferencesKey("ai_enabled")
    private val CARD_COLOR_KEY = longPreferencesKey("card_color")
    private val ALWAYS_SHOW_NAV_KEY = booleanPreferencesKey("always_show_nav")

    val githubToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[GITHUB_TOKEN_KEY]
        }

    val aiEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[AI_ENABLED_KEY] ?: true
        }

    val alwaysShowNav: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ALWAYS_SHOW_NAV_KEY] ?: true
        }

    val cardColor: Flow<Long?> = context.dataStore.data
        .map { preferences ->
            preferences[CARD_COLOR_KEY]
        }

    suspend fun saveGithubToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[GITHUB_TOKEN_KEY] = token
        }
    }

    suspend fun setAiEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AI_ENABLED_KEY] = enabled
        }
    }

    suspend fun setAlwaysShowNav(always: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ALWAYS_SHOW_NAV_KEY] = always
        }
    }

    suspend fun saveCardColor(color: Long) {
        context.dataStore.edit { preferences ->
            preferences[CARD_COLOR_KEY] = color
        }
    }

    fun getFallbackToken(): String? {
        return try {
            val jsonString = context.assets.open("secrets.json").bufferedReader().use { it.readText() }
            JSONObject(jsonString).getString("github_token")
        } catch (e: Exception) {
            null
        }
    }
}
