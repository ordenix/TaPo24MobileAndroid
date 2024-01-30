package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.CodeLimitsDrivingLicence
@Dao

interface CodeLimitsDrivingLicenceDao {

    @Query("SELECT * FROM codeLimitsDrivingLicence")
    fun getAll(): List<CodeLimitsDrivingLicence>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<CodeLimitsDrivingLicence>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: CodeLimitsDrivingLicence)
    @Query("DELETE FROM codeLimitsDrivingLicence")
    fun deleteAll()
}