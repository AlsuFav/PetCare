package ru.fav.petcare.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import ru.fav.petcare.domain.repository.JwtRepository
import javax.inject.Inject

class JwtRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : JwtRepository {
    
    companion object {
        val JWT_KEY = stringPreferencesKey("jwt")
    }
    
    override suspend fun saveJwt(apiKey: String) {
        dataStore.edit { preferences ->
            preferences[JWT_KEY] = apiKey
        }
    }
    
    override suspend fun getJwt(): String? {
        return dataStore.data
            .map { preferences -> preferences[JWT_KEY] }
            .firstOrNull()
    }
    
    override suspend fun clearJwt() {
        dataStore.edit { preferences ->
            preferences.remove(JWT_KEY)
        }
    }
}
