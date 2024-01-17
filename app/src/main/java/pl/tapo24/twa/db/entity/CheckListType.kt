package pl.tapo24.twa.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CheckListType(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(defaultValue = "")
    val name: String
)
