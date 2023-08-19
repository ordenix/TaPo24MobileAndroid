package pl.tapo24.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.dbData.entity.ControlList
@Dao

interface ControlListDao {

    @Query("SELECT * FROM controlList")
    fun getAll(): List<ControlList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<ControlList>)
}