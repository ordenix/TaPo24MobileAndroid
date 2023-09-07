package pl.tapo24.twa.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LightsFront(
    @PrimaryKey(autoGenerate = false)
    var id: Int,

    @ColumnInfo(defaultValue = "")
    var code: String = "",

    @ColumnInfo(defaultValue = "")
    var name: String = "",

    @ColumnInfo(defaultValue = "null")
    var remarks: String? = null,
) {
}