package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.CodePointsNew
@Dao

interface CodePointsNewDao {

    @Query("SELECT * FROM codePointsNew")
    fun getAll(): List<CodePointsNew>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<CodePointsNew>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: CodePointsNew)

    @Query("DELETE FROM codePointsNew")
    fun deleteAll()
}