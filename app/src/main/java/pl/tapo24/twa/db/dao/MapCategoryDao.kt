package pl.tapo24.twa.db.dao

import androidx.room.Dao
import androidx.room.Query
import pl.tapo24.twa.db.entity.MapCategory


@Dao
interface MapCategoryDao {

    @Query("SELECT * FROM mapCategory")
    fun getAll(): List<MapCategory>
}