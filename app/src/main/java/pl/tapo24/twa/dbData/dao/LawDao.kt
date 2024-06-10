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

    @Query("SELECT * FROM law where type = :type and isSelectedToDownload = '1'")
    fun getAllByTypeAndSelectedToDownload(type: String): List<Law>

    @Delete
    fun deleteElement(item: Law)

    @Query("SELECT EXISTS(SELECT * FROM law where isPremium = '1')")
    fun isExistsVip(): Boolean

    @Query("DELETE FROM law WHERE isPremium = '1'")
    fun deleteAllVip()
}