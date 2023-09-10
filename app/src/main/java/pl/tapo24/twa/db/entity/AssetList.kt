package pl.tapo24.twa.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AssetList(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(defaultValue = "")
    val path: String,

    @ColumnInfo(defaultValue = "0")
    val version: Int,

    @ColumnInfo(defaultValue = "")
    val name: String,
) {
}