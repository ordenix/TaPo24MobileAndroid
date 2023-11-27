package pl.tapo24.twa.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class CustomCategory(

    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,

    @ColumnInfo(defaultValue = "")
    var categoryName: String = "",

    @ColumnInfo(defaultValue = "0")
    var sortOrder: Int = 0,

    @ColumnInfo(defaultValue = "true")
    var synchronized: Boolean = true,

    @ColumnInfo(defaultValue = "false")
    var toDelete: Boolean = false
)