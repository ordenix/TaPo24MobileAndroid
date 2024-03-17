package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.TechnicalConditions

@Dao
interface TechnicalConditionsDao {
    @Query("SELECT * FROM technicalConditions")
    fun getAll(): List<TechnicalConditions>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<TechnicalConditions>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: TechnicalConditions)

    @Query("DELETE FROM technicalConditions")
    fun deleteAll()
}