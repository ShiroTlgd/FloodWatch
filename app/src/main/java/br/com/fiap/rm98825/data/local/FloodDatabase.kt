package br.com.fiap.rm98825.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.rm98825.model.AreaEntity

@Database(entities = [AreaEntity::class], version = 2, exportSchema = false)
abstract class FloodDatabase : RoomDatabase() {
    abstract fun areaDao(): AreaDao

    companion object {
        @Volatile
        private var INSTANCE: FloodDatabase? = null

        fun get(context: Context): FloodDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FloodDatabase::class.java,
                    "floodwatch.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
