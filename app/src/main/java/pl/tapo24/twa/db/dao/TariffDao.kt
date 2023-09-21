package pl.tapo24.twa.db.dao

import androidx.room.*
import pl.tapo24.twa.db.entity.Tariff



@Dao
interface TariffDao {
//limit 10

    @Query("SELECT * FROM tariff ORDER BY sortOrder ASC")
    fun getAll(): List<Tariff>

    @Query("SELECT * FROM tariff  where enginesType =:engineType ORDER BY sortOrder ASC")
    fun getAllByEngine(engineType: String): List<Tariff>

    @Query("SELECT * FROM tariff  where enginesType =:engineType AND favorites = true ORDER BY sortOrder ASC")
    fun getFavByEngine(engineType: String): List<Tariff>

    @Query("SELECT * FROM tariff  where id =:id")
    fun getByDocId(id: String): Tariff
    @Query("SELECT * FROM tariff  where sortOrder =:id and enginesType = 'old'")
    fun getBySortOrderOld(id: Int): Tariff

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tariff: Tariff)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(tariff: List<Tariff>)

    @Query("DELETE FROM tariff")
    fun nukeTable()

    @Delete
    fun deleteElement(item: Tariff)
}