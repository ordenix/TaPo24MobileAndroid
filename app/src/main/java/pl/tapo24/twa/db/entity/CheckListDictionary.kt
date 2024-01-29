package pl.tapo24.twa.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CheckListDictionary(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(defaultValue = "")
    val description: String,

    @ColumnInfo(defaultValue = "")
    val pathToPdf: String

)
