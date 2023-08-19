package pl.tapo24.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Status(

    @PrimaryKey(autoGenerate = false)
    var id: Int,

    @ColumnInfo(defaultValue = "")
    var status: String = "",
    @ColumnInfo(defaultValue = "")
    var stateDescription: String = "",
    @ColumnInfo(defaultValue = "null")
    var remarks: String? = null,
    @ColumnInfo(defaultValue = "false")
    var expand: Boolean = false,
) {
}