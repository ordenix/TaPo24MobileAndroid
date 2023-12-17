package pl.tapo24.twa.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CodeColors(

    @PrimaryKey(autoGenerate = false)
    var id: Long,

    @ColumnInfo(defaultValue = "")
    var codeName: String,

    @ColumnInfo(defaultValue = "")
    var code: String,

    @ColumnInfo(defaultValue = "")
    var mean: String,

    @ColumnInfo(defaultValue = "")
    var toDo: String,

    @ColumnInfo(defaultValue = "")
    var remarks: String,
)
