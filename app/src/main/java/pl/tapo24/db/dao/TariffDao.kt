package pl.tapo24.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.tapo24.db.entity.Tariff



@Dao
interface TariffDao {
//limit 10
    @Query("SELECT * FROM tariff ORDER BY id ASC")
    fun getAll(): List<Tariff>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tariff: Tariff)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(tariff: List<Tariff>)

    @Query("DELETE FROM tariff")
    fun nukeTable()

    @Delete
    fun deleteElement(item: Tariff)
}