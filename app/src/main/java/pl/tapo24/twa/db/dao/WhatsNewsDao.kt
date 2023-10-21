package pl.tapo24.twa.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.db.entity.WhatsNews

@Dao
interface WhatsNewsDao {

    @Query("SELECT * FROM whatsNews")
    fun getAll(): List<WhatsNews>
    @Query("SELECT * FROM whatsNews where id = 2")
    fun get2(): List<WhatsNews>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(whatsNews: WhatsNews)
}