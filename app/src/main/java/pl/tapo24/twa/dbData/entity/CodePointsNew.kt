package pl.tapo24.twa.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CodePointsNew(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",

    @ColumnInfo(defaultValue = "")
    var description: String? = null,

    @ColumnInfo(defaultValue = "0")
    var points: Int? = null,

    @ColumnInfo(defaultValue = "")
    var remarks: String? = null,

    @ColumnInfo(defaultValue = "")
    var type: String? = null,

    @ColumnInfo(defaultValue = "false")
    var addToCalc: Boolean? = null,
) {
}