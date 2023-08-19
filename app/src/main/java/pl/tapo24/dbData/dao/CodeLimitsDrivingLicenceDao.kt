package pl.tapo24.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.dbData.entity.CodeLimitsDrivingLicence
@Dao

interface CodeLimitsDrivingLicenceDao {

    @Query("SELECT * FROM codeLimitsDrivingLicence")
    fun getAll(): List<CodeLimitsDrivingLicence>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<CodeLimitsDrivingLicence>)
}