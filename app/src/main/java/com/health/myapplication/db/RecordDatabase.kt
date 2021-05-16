package com.health.myapplication.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.health.myapplication.dao.RecordDao
import com.health.myapplication.entity.record.Record

@Database(entities = [Record::class], version = 3)
abstract class RecordDatabase() : RoomDatabase() {
    abstract fun recordDao(): RecordDao

    companion object {
        private var INSTANCE: RecordDatabase? = null
        private val sLock = Any()

        @VisibleForTesting
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL("CREATE TABLE exerciseNote_new (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "exercisename TEXT NOT NULL, "+
                                "rep INTEGER NOT NULL, "+
                                "settime INTEGER NOT NULL, "+
                                "weight DOUBLE NOT NULL, "+
                                "dateid INTEGER)")

                // Copy the data
                database.execSQL("INSERT INTO exerciseNote_new (_id, exercisename,rep,settime,weight,dateid) "
                        + "SELECT _id, exercisename,rep,settime,weight,dateid "
                        + "FROM exerciseNote")

                // Remove the old table
                database.execSQL("DROP TABLE exerciseNote")
                database.execSQL("ALTER TABLE exerciseNote_new RENAME TO exerciseNote");
            }
        }

        fun getInstance(context: Context): RecordDatabase? {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            RecordDatabase::class.java, "exercisenote.db")
                            .addMigrations(MIGRATION_2_3)
                            .build()
                }
                return INSTANCE
            }
        }
    }
}