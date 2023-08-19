package pl.tapo24.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.dbData.entity.CodePointsOld
@Dao

interface CodePointsOldDao {

    @Query("SELECT * FROM codePointsOld")
    fun getAll(): List<CodePointsOld>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<CodePointsOld>)
}