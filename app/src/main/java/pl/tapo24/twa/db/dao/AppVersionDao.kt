package pl.tapo24.twa.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.db.entity.AppVersion


@Dao
interface AppVersionDao {

    @Query("SELECT * FROM appVersion")
    fun getAll(): List<AppVersion>

    @Query("SELECT * FROM appVersion order by versionCode DESC limit 1")
    fun getLast(): AppVersion

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: AppVersion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(item: List<AppVersion>)
}