package pl.tapo24.twa.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import pl.tapo24.twa.db.entity.Mourning


@Dao
interface MourningDao {

    @Query("SELECT * FROM mourning")
    fun getAll(): List<Mourning>


    @Query("SELECT * FROM mourning order by id ASC limit 1")
    fun getLast(): Mourning
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(item: List<Mourning>)

    @Query("DELETE FROM mourning")
    fun nukeTable()
}