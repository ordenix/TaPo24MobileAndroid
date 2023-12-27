package pl.tapo24.twa.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.db.entity.MapCategory


@Dao
interface MapCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(it: List<MapCategory>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(it: MapCategory)

    @Query("SELECT * FROM mapCategory")
    fun getAll(): List<MapCategory>


    @Query("SELECT * FROM mapCategory WHERE toDelete = '0'")
    fun getAllWithoutDeleted(): List<MapCategory>

    @Query("DELETE FROM mapCategory")
    fun nukeTable()
    @Query("select (count(mapCategory.id) > 0) from mapCategory where tariffId = :tariffId and customCategoryId = :categoryId and toDelete = '0'")
    fun existByTariffIdAndCategoryId(tariffId: String, categoryId: Int): Boolean


    @Query("select * from mapCategory where tariffId = :tariffId and customCategoryId = :categoryId")
    fun getByTariffIdAndCategoryId(tariffId: String, categoryId: Int): MapCategory
}