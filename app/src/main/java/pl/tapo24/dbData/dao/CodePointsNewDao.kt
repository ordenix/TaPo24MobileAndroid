package pl.tapo24.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.dbData.entity.CodePointsNew
@Dao

interface CodePointsNewDao {

    @Query("SELECT * FROM codePointsNew")
    fun getAll(): List<CodePointsNew>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<CodePointsNew>)
}