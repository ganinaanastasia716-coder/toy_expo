package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserExhibitionEntity::class,
        ExhibitionTrackingEntity::class,
        ExclusiveDropEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ToyDatabase : RoomDatabase() {
    abstract fun toyDao(): ToyDao

    companion object {
        @Volatile
        private var INSTANCE: ToyDatabase? = null

        fun getDatabase(context: Context): ToyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToyDatabase::class.java,
                    "toy_expo_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
