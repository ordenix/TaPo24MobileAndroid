package pl.tapo24.twa.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EnginePollution(
    @PrimaryKey(autoGenerate = false)
    var id: Long,

    @ColumnInfo(defaultValue = "")
    var vechicle: String? = null,

    @ColumnInfo(defaultValue = "")
    var p1: String? = null,

    @ColumnInfo(defaultValue = "")
    var p2: String? = null,

    @ColumnInfo(defaultValue = "")
    var p3: String? = null,

    @ColumnInfo(defaultValue = "")
    var p4: String? = null,

    @ColumnInfo(defaultValue = "")
    var p5: String? = null,

    @ColumnInfo(defaultValue = "")
    var p6: String? = null,

    @ColumnInfo(defaultValue = "")
    var p7: String? = null,
)
