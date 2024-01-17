package pl.tapo24.twa.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.db.entity.CheckListType


@Dao
interface CheckListTypeDao {
    @Query("SELECT * FROM checkListType")
    fun getAll(): List<CheckListType>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(checkListType: List<CheckListType>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(checkListType: CheckListType)
}