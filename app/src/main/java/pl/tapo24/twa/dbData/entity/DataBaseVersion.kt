package pl.tapo24.twa.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DataBaseVersion(
    @PrimaryKey(autoGenerate = false)
    var id: String,

    @ColumnInfo(defaultValue = "0")
    var version: Int = 0
)