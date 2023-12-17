package pl.tapo24.twa.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MapCategory(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,

    @ColumnInfo(defaultValue = "0")
    var customCategoryId: Int = 0,

    @ColumnInfo(defaultValue = "")
    var tariffId: String = "",

    @ColumnInfo(defaultValue = "true")
    var dataSynchronized: Boolean = true,

    @ColumnInfo(defaultValue = "false")
    var toDelete: Boolean = false
)
