package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.LightsCodeCountry

@Dao
interface LightsCodeCountryDao {
    @Query("SELECT * FROM lightsCodeCountry")
    fun getAll(): List<LightsCodeCountry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<LightsCodeCountry>)
}