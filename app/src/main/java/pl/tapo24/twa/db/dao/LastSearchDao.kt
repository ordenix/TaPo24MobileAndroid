package pl.tapo24.twa.db.dao

import androidx.room.*
import pl.tapo24.twa.db.entity.LastSearch


@Dao
interface LastSearchDao {

    @Query("SELECT * FROM lastSearch")
    fun getAll(): List<LastSearch>

    @Delete
    fun deleteElement(item: LastSearch)
    @Query("SELECT * FROM lastSearch order by count DESC limit 10")
    fun getAllTop6ByTimes(): List<LastSearch>

    @Query("SELECT * FROM lastSearch where query like '%' || :q || '%' order by count DESC limit 10")
    fun getAllTop6ByTimesAndQuery(q:String): List<LastSearch>

    @Query("SELECT * FROM lastSearch where query =:q")
    fun getByQuery(q: String): LastSearch?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(lastSearch: LastSearch)
}