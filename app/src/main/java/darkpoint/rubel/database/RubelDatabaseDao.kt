package darkpoint.rubel.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import darkpoint.rubel.model.Rate

@Dao
interface RubelDatabaseDao {

    @Insert
    fun insert(rate: Rate)

    @Update
    fun update(rate: Rate)

    @Query(value = "SELECT * FROM rubel_table WHERE cur_id = :key")
    fun get(key: Int): Rate?
}