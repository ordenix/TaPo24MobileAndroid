package pl.tapo24.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.dbData.entity.Story
@Dao

interface StoryDao {

    @Query("SELECT * FROM story")
    fun getAll(): List<Story>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<Story>)
}