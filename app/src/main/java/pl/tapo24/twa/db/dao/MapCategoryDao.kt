package pl.tapo24.twa.db.dao

import androidx.room.*
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

    @Delete
    fun delete(mapCategory: MapCategory)

    @Query("SELECT COUNT(mapCategory.id) from mapCategory")
    fun getCountOfElements(): Int

    @Query("SELECT * FROM mapCategory WHERE toDelete = '1'")
    fun getAllToDelete(): List<MapCategory>

    @Query("SELECT * FROM mapCategory WHERE dataSynchronized = '0'")
    fun getAllNotSynchronized(): List<MapCategory>
}