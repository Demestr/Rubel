package darkpoint.rubel.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import darkpoint.rubel.model.Rate

@Database(entities = [Rate::class], version = 1, exportSchema = false)
abstract class RubelDatabase : RoomDatabase() {

    abstract val rubelDatabaseDao: RubelDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: RubelDatabase? = null

        fun getInstance(context: Context) : RubelDatabase{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            RubelDatabase::class.java,
                            "rubel_database"
                    )
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}