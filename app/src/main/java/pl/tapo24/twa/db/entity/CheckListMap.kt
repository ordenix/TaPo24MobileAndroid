package pl.tapo24.twa.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class CheckListMap(

    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(defaultValue = "0")
    var sortOrder: Int,

    @ColumnInfo(defaultValue = "0")
    val checkListType: Int,

    @ColumnInfo(defaultValue = "0")
    val checkListDictionary: Int,


    @ColumnInfo(defaultValue = "false")
    var isSelected: Boolean = false,

    @ColumnInfo(defaultValue = "false")
    var isDeleted: Boolean = false


) {
}