package ru.fav.petcare.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.fav.petcare.database.entity.ClientEntity

@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveClient(participant: ClientEntity)

    @Query("SELECT * FROM clients LIMIT 1")
    suspend fun getClient(): ClientEntity?

    @Query("DELETE FROM clients")
    suspend fun deleteClient()
}