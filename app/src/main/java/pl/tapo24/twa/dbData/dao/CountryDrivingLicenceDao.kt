package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.CountryDrivingLicence

@Dao
interface CountryDrivingLicenceDao {
    @Query("SELECT * FROM countryDrivingLicence")
    fun getAll(): List<CountryDrivingLicence>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<CountryDrivingLicence>)
}