package pl.tapo24.twa.dbData.dao

import androidx.room.*
import pl.tapo24.twa.dbData.entity.Law


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

    @Delete
    fun deleteElement(item: Law)
}