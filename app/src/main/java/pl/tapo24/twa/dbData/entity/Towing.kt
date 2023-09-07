package pl.tapo24.twa.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Towing(

    @PrimaryKey(autoGenerate = false)
    var id: Int,

    @ColumnInfo(defaultValue = "")
    var name: String = "",
    @ColumnInfo(defaultValue = "")
    var law: String = "",
    @ColumnInfo(defaultValue = "")
    var type: String = "",
    @ColumnInfo(defaultValue = "null")
    var necessity: String? = null,
    @ColumnInfo(defaultValue = "")
    var cost: String = "",
    @ColumnInfo(defaultValue = "true")
    var permission: Boolean? = true,
)
