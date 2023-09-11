package pl.tapo24.twa.db.dao

import androidx.room.*
import pl.tapo24.twa.db.entity.Tariff



@Dao
interface TariffDao {
//limit 10
    @Query("SELECT * FROM tariff  where engineType =: engineType ORDER BY id ASC")
    fun getAllByEngine(engineType: String): List<Tariff>

    @Query("SELECT * FROM tariff  where id =: id ORDER BY id ASC")
    fun getByDocId(id: String): List<Tariff>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tariff: Tariff)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(tariff: List<Tariff>)

    @Query("DELETE FROM tariff")
    fun nukeTable()

    @Delete
    fun deleteElement(item: Tariff)
}