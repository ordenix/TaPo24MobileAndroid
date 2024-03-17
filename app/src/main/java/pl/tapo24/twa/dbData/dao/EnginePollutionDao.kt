package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.EnginePollution

@Dao
interface EnginePollutionDao {
    @Query("SELECT * FROM enginePollution")
    fun getAll(): List<EnginePollution>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<EnginePollution>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items:EnginePollution)

    @Query("DELETE FROM enginePollution")
    fun deleteAll()
}