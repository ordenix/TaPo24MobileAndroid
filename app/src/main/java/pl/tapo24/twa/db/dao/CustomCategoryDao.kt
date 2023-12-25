package pl.tapo24.twa.db.dao

import androidx.room.*
import pl.tapo24.twa.db.entity.CustomCategory


@Dao
interface CustomCategoryDao {

    @Query("SELECT * from customCategory")
    fun getAll(): List<CustomCategory>

    @Query("SELECT * from customCategory WHERE toDelete = '0' order by sortOrder ASC")
    fun getAllWithoutDeleted(): List<CustomCategory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(customCategory: CustomCategory)

    @Delete
    fun delete(customCategory: CustomCategory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(it: List<CustomCategory>)

    @Query("DELETE FROM customCategory")
    fun nukeTable()

    @Query("SELECT * FROM customCategory WHERE toDelete = '1'")
    fun getAllToDelete(): List<CustomCategory>

    @Query("SELECT * FROM customCategory WHERE dataSynchronized = '0'")
    fun getAllNotSynchronized(): List<CustomCategory>

    @Query("SELECT COUNT(customCategory.id) from customCategory where toDelete = '0'")
    fun getCountOfElements(): Int

}