package pl.tapo24.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.dbData.entity.HoldingDocuments


@Dao

interface HoldingDocumentsDao {

    @Query("SELECT * FROM holdingDocuments")
    fun getAll(): List<HoldingDocuments>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<HoldingDocuments>)
}