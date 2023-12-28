package pl.tapo24.twa.db.dao

import androidx.room.*
import pl.tapo24.twa.db.entity.Tariff



@Dao
interface TariffDao {
//limit 10

    @Query("SELECT * FROM tariff ORDER BY sortOrder ASC")
    fun getAll(): List<Tariff>

    @Query("SELECT * from tariff where enginesType = 'New' AND recidive = '1' ORDER BY sortOrder")
    fun getAllRecidivism(): List<Tariff>
    @Query("SELECT * FROM tariff  where enginesType =:engineType ORDER BY sortOrder ASC")
    fun getAllByEngine(engineType: String): List<Tariff>
    // TODO remeber if develop premium version change this sort to sort order !
    @Query("SELECT * FROM tariff  where enginesType =:engineType AND favorites = '1' ORDER BY sortOrderFav ASC")
    fun getFavByEngine(engineType: String): List<Tariff>

    @Query("SELECT * FROM tariff  where enginesType =:engineType AND category like :category ORDER BY sortOrder ASC")
    fun getAllByEngineAndCategory(engineType: String, category: String): List<Tariff>
    @Query("SELECT * FROM tariff where id = :tariffId")
    fun getByTariffId(tariffId: String): Tariff
    @Query("SELECT * FROM tariff  where id =:id")
    fun getByDocId(id: String): Tariff
    @Query("SELECT * FROM tariff  where sortOrder =:id and enginesType = 'old'")
    fun getBySortOrderOld(id: Int): Tariff

    @Query("SELECT * FROM tariff where enginesType = :engine and maxSpeed >= :speed and minSpeed <= :speed")
    fun getBySpeedAndEngine(speed: Int, engine: String): Tariff
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tariff: Tariff)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(tariff: List<Tariff>)

    @Query("DELETE FROM tariff")
    fun nukeTable()

    @Delete
    fun deleteElement(item: Tariff)
}