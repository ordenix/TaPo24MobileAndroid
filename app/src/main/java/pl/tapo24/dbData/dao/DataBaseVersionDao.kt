package pl.tapo24.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.dbData.entity.DataBaseVersion

@Dao
interface DataBaseVersionDao {
    @Query("SELECT * FROM dataBaseVersion")
    fun getAll(): List<DataBaseVersion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<DataBaseVersion>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: DataBaseVersion)


}