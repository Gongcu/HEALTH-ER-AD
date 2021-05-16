package com.health.myapplication.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.health.myapplication.dao.CustomProgramExerciseDao
import com.health.myapplication.entity.custom_program.CustomProgramExercise

@Database(entities = [CustomProgramExercise::class], version = 3)
abstract class CustomProgramExerciseDatabase() : RoomDatabase() {
    abstract fun customProgramExerciseDao(): CustomProgramExerciseDao

    companion object {
        private var INSTANCE: CustomProgramExerciseDatabase? = null
        private val sLock = Any()

        @VisibleForTesting
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL(
                        "CREATE TABLE program_new (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "activity INTEGER NOT NULL, "+
                                "divide INTEGER NOT NULL, "+
                                "part TEXT NOT NULL, "+
                                "exercise TEXT NOT NULL, "+
                                "itemorder INTEGER NOT NULL, "+
                                "settime INTEGER NOT NULL, "+
                                "rep INTEGER NOT NULL )")
                // Copy the data
                database.execSQL("INSERT INTO program_new (_id, activity, divide, part, exercise, itemorder, settime, rep) "
                        + "SELECT _id, activity, divide, part, exercise, itemorder, settime, rep "
                        + "FROM program")
                // Remove the old table
                database.execSQL("DROP TABLE program")
                database.execSQL("ALTER TABLE program_new RENAME TO program");
            }
        }

        fun getInstance(context: Context): CustomProgramExerciseDatabase? {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            CustomProgramExerciseDatabase::class.java, "programTable.db")
                            .addMigrations(MIGRATION_2_3)
                            .build()
                }
                return INSTANCE
            }
        }
    }
}