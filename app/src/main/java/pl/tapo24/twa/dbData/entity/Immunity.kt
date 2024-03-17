package pl.tapo24.twa.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Immunity(

    @PrimaryKey(autoGenerate = false)
    var id: Long? = null,

    @ColumnInfo(defaultValue = "")
    var person: String? = null,

    @ColumnInfo(defaultValue = "")
    var type: String? = null,

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
)
