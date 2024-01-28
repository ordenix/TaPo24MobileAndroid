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


    @Query ("SELECT * FROM checkListMap WHERE checkListType = :type AND isDeleted = '0' ORDER BY sortOrder ASC")
    fun getByTypeNonDeleted(type: Int): List<CheckListMap>
//    @Query("UPDATE checkListMap SET sortOrder= :checkListMap.sortOrder WHERE id = :checkListMap.id")
//    fun updateAndSetVisible(checkLis
    //    tMap: CheckListMap)
    @Query("SELECT * FROM checkListMap WHERE id = :id")
    fun getById(id: Int): CheckListMap

    @Query("UPDATE checkListMap SET isSelected = '0' WHERE checkListType = :idList")
    fun unSelectAll(idList: Int)

    @Query("UPDATE checkListMap SET isDeleted = '0' WHERE checkListType = :idList")
    fun restoreList(idList: Int)
}