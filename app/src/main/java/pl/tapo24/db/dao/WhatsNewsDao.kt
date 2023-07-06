package pl.tapo24.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.db.entity.WhatsNews

@Dao
interface WhatsNewsDao {

    @Query("SELECT * FROM whatsNews")
    fun getAll(): List<WhatsNews>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(whatsNews: WhatsNews)
}