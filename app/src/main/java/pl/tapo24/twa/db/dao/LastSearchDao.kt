package pl.tapo24.twa.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.db.entity.LastSearch


@Dao
interface LastSearchDao {

    @Query("SELECT * FROM lastSearch")
    fun getAll(): List<LastSearch>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(lastSearch: LastSearch)
}