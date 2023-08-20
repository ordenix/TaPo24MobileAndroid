package pl.tapo24.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Uto(
    @PrimaryKey(autoGenerate = false)
    var id: Int,

    @ColumnInfo(defaultValue = "")
    var name: String? = null,

    @ColumnInfo(defaultValue = "")
    var definition: String? = null,

    @ColumnInfo(defaultValue = "")
    var rights: String? = null,

    @ColumnInfo(defaultValue = "")
    var whereAllowed: String? = null,

    @ColumnInfo(defaultValue = "")
    var speed: String? = null,

    @ColumnInfo(defaultValue = "")
    var prohibition: String? = null,

    @ColumnInfo(defaultValue = "")
    var behavior: String? = null,

    @ColumnInfo(defaultValue = "")
    var stop: String? = null

)
