package pl.tapo24.twa.dbData.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.tapo24.twa.dbData.entity.Story
@Dao

interface StoryDao {

    @Query("SELECT * FROM story where type = :type")
    fun getAllByType(type: String): List<Story>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(items: List<Story>)
}