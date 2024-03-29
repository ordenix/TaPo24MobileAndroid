package pl.tapo24.twa.db.dao

import androidx.room.*
import pl.tapo24.twa.db.entity.Setting

@Dao
interface SettingDao {

    @Query("SELECT * FROM setting")
    fun getAll(): List<Setting>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(setting: Setting)

    @Query("SELECT * from setting where name = :name limit 1")
    fun getSettingByName(name: String): Setting?

    @Delete
    fun deleteElement(item: Setting)


}