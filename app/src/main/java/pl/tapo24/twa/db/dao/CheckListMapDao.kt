package pl.tapo24.twa.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.db.entity.CheckListMap


@Dao
interface CheckListMapDao {

    @Query("SELECT * FROM checkListMap")
    fun getAll(): List<CheckListMap>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(checkListMap: List<CheckListMap>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(checkListMap: CheckListMap)

    @Query("DELETE FROM checkListMap")
    fun nukeTable()

//    @Query("UPDATE checkListMap SET sortOrder= :checkListMap.sortOrder WHERE id = :checkListMap.id")
//    fun updateAndSetVisible(checkListMap: CheckListMap)


}