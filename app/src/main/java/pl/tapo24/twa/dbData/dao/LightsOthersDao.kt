package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.LightsOthers

@Dao
interface LightsOthersDao {
    @Query("SELECT * FROM lightsOthers")
    fun getAll(): List<LightsOthers>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<LightsOthers>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: LightsOthers)

    @Query("DELETE FROM lightsOthers")
    fun deleteAll()
}