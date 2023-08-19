package pl.tapo24.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CodePointsOld(
    @PrimaryKey(autoGenerate = false)
    var id: String,

    @ColumnInfo(defaultValue = "")
    var description: String? = null,

    @ColumnInfo(defaultValue = "0")
    var points: Int? = null
) {
}