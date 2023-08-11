package pl.tapo24.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.db.entity.Tariff


@Dao
interface TariffDao {

    @Query("SELECT * FROM tariff")
    fun getAll(): List<Tariff>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tariff: Tariff)
}