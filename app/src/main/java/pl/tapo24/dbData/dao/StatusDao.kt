package pl.tapo24.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.dbData.entity.Status

@Dao
interface StatusDao {
    @Query("SELECT * FROM status")
    fun getAll(): List<Status>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<Status>)
}