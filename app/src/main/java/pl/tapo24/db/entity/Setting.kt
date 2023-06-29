package pl.tapo24.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Setting (
    @PrimaryKey(autoGenerate = false)
    var name: String,
    var value: String = "",
    var count: Int = 0,
    @ColumnInfo(defaultValue = "false")
    var state: Boolean = false
)