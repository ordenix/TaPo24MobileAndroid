package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import pl.tapo24.twa.dbData.entity.Uto

@Dao
interface UtoDao {

    @Query("SELECT * FROM uto where id = :id")
    fun getById(id: Int): Uto

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<Uto>)
}