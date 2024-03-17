package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.Immunity

@Dao
interface ImmunityDao {
    @Query("SELECT * FROM immunity")
    fun getAll(): List<Immunity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<Immunity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: Immunity)

    @Query("DELETE FROM immunity")
    fun deleteAll()
}