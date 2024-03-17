package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.AdrNumber

@Dao
interface AdrNumberDao {
    @Query("SELECT * FROM adrNumber")
    fun getAll(): List<AdrNumber>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<AdrNumber>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items:AdrNumber)

    @Query("DELETE FROM adrNumber")
    fun deleteAll()
}