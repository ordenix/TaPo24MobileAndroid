package pl.tapo24.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HoldingDocuments(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,

    @ColumnInfo(defaultValue = "")
    var type: String = "",

    @ColumnInfo(defaultValue = "")
    var law: String = "",

    @ColumnInfo(defaultValue = "")
    var lawText: String = "",

    @ColumnInfo(defaultValue = "")
    var description: String = "",

    @ColumnInfo(defaultValue = "")
    var time: String = "",

    @ColumnInfo(defaultValue = "")
    var remarks: String = "",

    @ColumnInfo(defaultValue = "")
    var comment: String = "",

    @ColumnInfo(defaultValue = "")
    var otherCountry: Boolean = false,
)
