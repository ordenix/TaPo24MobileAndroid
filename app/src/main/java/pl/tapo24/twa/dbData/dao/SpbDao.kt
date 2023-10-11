package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.Spb

@Dao
interface SpbDao {

    @Query("SELECT * from spb where isSpb = 1")
    fun getAllSpb(): List<Spb>


    @Query("SELECT * from spb where type = :type")
    fun getSpbByType(type: String): Spb
    @Query("SELECT * from spb where isSpb = 0")
    fun getAllNotSpb(): List<Spb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<Spb>)

    @Query("DELETE FROM spb")
    fun nukeTable()
}