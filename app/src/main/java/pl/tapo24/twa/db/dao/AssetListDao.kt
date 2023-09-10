package pl.tapo24.twa.db.dao

import androidx.room.*
import pl.tapo24.twa.db.entity.AssetList

@Dao
interface AssetListDao {

    @Query("SELECT * FROM assetList")
    fun getAll(): List<AssetList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asset: AssetList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(asset: List<AssetList>)

    @Query("DELETE FROM assetList")
    fun nukeTable()

    @Delete
    fun deleteElement(item: AssetList)

    @Query("select (count(assetList.id) > 0) from assetList")
    fun exist(): Boolean

}