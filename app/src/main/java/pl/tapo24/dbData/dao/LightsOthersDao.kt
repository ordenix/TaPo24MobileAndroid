package pl.tapo24.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.dbData.entity.LightsOthers

@Dao
interface LightsOthersDao {
    @Query("SELECT * FROM lightsOthers")
    fun getAll(): List<LightsOthers>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<LightsOthers>)
}