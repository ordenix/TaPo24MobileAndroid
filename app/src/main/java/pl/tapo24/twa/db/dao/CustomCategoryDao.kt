package pl.tapo24.twa.db.dao

import androidx.room.Dao
import androidx.room.Query
import pl.tapo24.twa.db.entity.CustomCategory


@Dao
interface CustomCategoryDao {

    @Query("SELECT * from customCategory")
    fun getAll(): List<CustomCategory>

}