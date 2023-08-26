package pl.tapo24.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.dbData.entity.Sign

@Dao
interface SignDao {

    @Query("SELECT * FROM sign where signCategory = :signCategory")
    fun getAllBySignCategory(signCategory: String): List<Sign>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<Sign>)
}