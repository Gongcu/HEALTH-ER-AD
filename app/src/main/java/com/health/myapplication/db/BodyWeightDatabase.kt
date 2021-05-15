package com.health.myapplication.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.health.myapplication.dao.BodyWeightDao
import com.health.myapplication.model.body_weight.BodyWeight

@Database(entities = [BodyWeight::class], version = 2)
abstract class BodyWeightDatabase() : RoomDatabase() {
    abstract fun BodyWeightDao(): BodyWeightDao

    companion object {
        private var INSTANCE: BodyWeightDatabase? = null
        private val sLock = Any()

        @VisibleForTesting
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL((
                        "CREATE TABLE weightTable_new (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "weight DOUBLE NOT NULL, " +
                                "date TEXT DEFAULT ((datetime('now','localtime'))))"))
                // Copy the data
                database.execSQL(("INSERT INTO weightTable_new (_id, weight, date) "
                        + "SELECT _id, weight, date "
                        + "FROM weightTable"))
                // Remove the old table
                database.execSQL("DROP TABLE weightTable")
                database.execSQL("ALTER TABLE weightTable_new RENAME TO weightTable");
            }
        }

        fun getInstance(context: Context): BodyWeightDatabase? {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            BodyWeightDatabase::class.java, "weight.db")
                            .addMigrations(MIGRATION_1_2)
                            .build()
                }
                return INSTANCE
            }
        }
    }
}