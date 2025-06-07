package ru.fav.petcare.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.fav.petcare.database.dao.ClientDao
import ru.fav.petcare.database.entity.ClientEntity

@Database(
    entities = [ClientEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao

    companion object {
        const val DB_LOG_KEY = "AppDatabase"
    }
}