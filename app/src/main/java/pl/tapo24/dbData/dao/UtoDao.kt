package pl.tapo24.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import pl.tapo24.dbData.entity.Uto

@Dao
interface UtoDao {

    @Query("SELECT * FROM uto where id = :id")
    fun getAllById(id: Int): List<Uto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<Uto>)
}