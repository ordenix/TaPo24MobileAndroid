package pl.tapo24.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.dbData.entity.Law


@Dao
interface LawDao {

    @Query("SELECT * FROM law")
    fun getAll(): List<Law>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<Law>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Law)


    @Query("SELECT * FROM law where type = :type")
    fun getAllByType(type: String): List<Law>
}