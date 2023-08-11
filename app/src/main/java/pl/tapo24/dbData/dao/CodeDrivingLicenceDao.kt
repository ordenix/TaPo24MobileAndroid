package pl.tapo24.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.dbData.entity.CodeDrivingLicence
import pl.tapo24.dbData.entity.DataBaseVersion

@Dao
interface CodeDrivingLicenceDao {

    @Query("SELECT * FROM codeDrivingLicence")
    fun getAll(): List<CodeDrivingLicence>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<CodeDrivingLicence>)
}