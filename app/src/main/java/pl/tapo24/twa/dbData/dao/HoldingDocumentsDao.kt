package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.HoldingDocuments


@Dao

interface HoldingDocumentsDao {

    @Query("SELECT * FROM holdingDocuments")
    fun getAll(): List<HoldingDocuments>

    @Query("SELECT * FROM holdingDocuments where type = :type AND otherCountry = :otherCountry")
    fun getAllByTypeAndCountry(type: String, otherCountry: Boolean): List<HoldingDocuments>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<HoldingDocuments>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: HoldingDocuments)

    @Query("DELETE FROM holdingDocuments")
    fun deleteAll()
}