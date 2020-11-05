package com.health.myapplication.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.health.myapplication.dao.CustomProgramDao
import com.health.myapplication.model.CustomProgram

@Database(entities = [CustomProgram::class], version = 3)
abstract class CustomProgramDatabase() : RoomDatabase() {
    abstract fun customProgramDao(): CustomProgramDao

    companion object {
        private var INSTANCE: CustomProgramDatabase? = null
        private val sLock = Any()

        @VisibleForTesting
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL(
                        "CREATE TABLE program_new (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "activity TEXT NOT NULL )")
                // Copy the data
                database.execSQL("INSERT INTO program_new (_id, activity) "
                        + "SELECT _id, activity "
                        + "FROM program")
                // Remove the old table
                database.execSQL("DROP TABLE program")
                database.execSQL("ALTER TABLE program_new RENAME TO program");
            }
        }

        fun getInstance(context: Context): CustomProgramDatabase? {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            CustomProgramDatabase::class.java, "programItem.db")
                            .addMigrations(MIGRATION_2_3)
                            .build()
                }
                return INSTANCE
            }
        }
    }
}