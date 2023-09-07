package pl.tapo24.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LastSearch(

    @PrimaryKey(autoGenerate = false)
    val query: String,

    @ColumnInfo(defaultValue = "0")
    val lastTime: Int,

    @ColumnInfo(defaultValue = "false")
    val isHistory: Boolean
)