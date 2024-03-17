package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.Company

@Dao
interface CompanyDao {
    @Query("SELECT * FROM company")
    fun getAll(): List<Company>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<Company>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: Company)

    @Query("DELETE FROM company")
    fun deleteAll()
}