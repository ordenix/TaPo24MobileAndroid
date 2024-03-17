package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.NoiseLevel

@Dao
interface NoiseLevelDao {
    @Query("SELECT * FROM noiseLevel")
    fun getAll(): List<NoiseLevel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<NoiseLevel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: NoiseLevel)

    @Query("DELETE FROM noiseLevel")
    fun deleteAll()
}