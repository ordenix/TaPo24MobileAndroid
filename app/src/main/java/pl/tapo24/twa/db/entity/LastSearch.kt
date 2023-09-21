package pl.tapo24.twa.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LastSearch(

    @PrimaryKey(autoGenerate = false)
    val query: String,

    @ColumnInfo(defaultValue = "0")
    var lastTime: Long,

    @ColumnInfo(defaultValue = "false")
    val isHistory: Boolean,

    @ColumnInfo(defaultValue = "0")
    var count: Int,

    @ColumnInfo(defaultValue = "")
    var listDocId: String,
)
