package pl.tapo24.twa.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoiseLevel(
    @PrimaryKey(autoGenerate = false)
    var id: Long? = null,

    @ColumnInfo(defaultValue = "")
    var vechicle: String? = null,

    @ColumnInfo(defaultValue = "")
    var gasolineEngine: String? = null,

    @ColumnInfo(defaultValue = "")
    var dieselEngine: String? = null,
)
