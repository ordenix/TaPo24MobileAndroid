package pl.tapo24.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.dbData.entity.Towing


@Dao
interface TowingDao {
    @Query("SELECT * FROM towing")
    fun getAll(): List<Towing>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<Towing>)
}