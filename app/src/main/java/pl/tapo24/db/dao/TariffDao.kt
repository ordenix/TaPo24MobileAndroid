package pl.tapo24.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.tapo24.db.entity.Tariff



@Dao
interface TariffDao {

    @Query("SELECT * FROM tariff limit 10")
    fun getAll(): List<Tariff>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tariff: Tariff)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(tariff: List<Tariff>)

    @Query("DELETE FROM tariff")
    fun nukeTable()
}