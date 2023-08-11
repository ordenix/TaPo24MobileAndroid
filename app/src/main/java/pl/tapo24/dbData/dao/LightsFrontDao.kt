package pl.tapo24.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.dbData.entity.LightsFront

@Dao
interface LightsFrontDao {
    @Query("SELECT * FROM lightsFront")
    fun getAll(): List<LightsFront>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<LightsFront>)
}