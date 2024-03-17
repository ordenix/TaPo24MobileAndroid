package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.ImmunityAdres

@Dao
interface ImmunityAdresDao {
    @Query("SELECT * FROM immunityAdres")
    fun getAll(): List<ImmunityAdres>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<ImmunityAdres>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: ImmunityAdres)

    @Query("DELETE FROM immunityAdres")
    fun deleteAll()
}