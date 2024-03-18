package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.HinNumber

@Dao
interface HinNumberDao {
    @Query("SELECT * FROM hinNumber")
    fun getAll(): List<HinNumber>

    @Query("SELECT * FROM hinNumber where id = :hinNumber")
    fun getByHinNumber(hinNumber: String): HinNumber?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<HinNumber>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items:HinNumber)

    @Query("DELETE FROM hinNumber")
    fun deleteAll()
}