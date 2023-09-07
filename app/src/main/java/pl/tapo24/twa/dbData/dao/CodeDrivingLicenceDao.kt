package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.CodeDrivingLicence

@Dao
interface CodeDrivingLicenceDao {

    @Query("SELECT * FROM codeDrivingLicence")
    fun getAll(): List<CodeDrivingLicence>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<CodeDrivingLicence>)
}