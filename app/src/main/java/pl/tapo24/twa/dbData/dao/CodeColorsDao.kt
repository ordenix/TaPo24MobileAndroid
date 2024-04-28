package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.CodeColors

@Dao
interface  CodeColorsDao  {


    @Query("SELECT * FROM codeColors")
    fun getAll(): List<CodeColors>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<CodeColors>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items:CodeColors)

    @Query("DELETE FROM codeColors")
    fun deleteAll()


    @Query("SELECT * FROM codeColors WHERE code = :code")
    fun getByCode(code: String): List<CodeColors>
}