package pl.tapo24.twa.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Mourning(
    @PrimaryKey(autoGenerate = false)
    var id: Long,

    @ColumnInfo(defaultValue = "0")
    var dataFrom: Long,

    @ColumnInfo(defaultValue = "0")
    var dataTo: Long,
)
