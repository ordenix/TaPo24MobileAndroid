package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.Status

@Dao
interface StatusDao {
    @Query("SELECT * FROM status")
    fun getAll(): List<Status>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<Status>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Status)

    @Query("DELETE FROM status")
    fun nukeTable()

    @Query("DELETE FROM status")
    fun deleteAll()
}