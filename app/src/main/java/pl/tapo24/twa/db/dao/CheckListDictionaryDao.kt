package pl.tapo24.twa.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.db.entity.CheckListDictionary


@Dao
interface CheckListDictionaryDao {

    @Query("SELECT * FROM checkListDictionary")
    fun getAll(): List<CheckListDictionary>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(checkListDictionary: List<CheckListDictionary>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(checkListDictionary: CheckListDictionary)

    @Query("DELETE FROM checkListDictionary")
    fun nukeTable()

    @Query("DELETE FROM checkListDictionary")
    fun deleteAll()
}