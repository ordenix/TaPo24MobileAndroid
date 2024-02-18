package pl.tapo24.twa.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Law (

    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,

    @ColumnInfo(defaultValue = "")
    var version: Int? = null,

    @ColumnInfo(defaultValue = "")
    var name: String? = null,

    @ColumnInfo(defaultValue = "")
    var url: String? = null,

    @ColumnInfo(defaultValue = "")
    var fileName: String? = null,

    @ColumnInfo(defaultValue = "")
    var icon: String? = null,

    @ColumnInfo(defaultValue = "")
    var shortName: String? = null,

    @ColumnInfo(defaultValue = "")
    var alias: String? = null,

    @ColumnInfo(defaultValue = "")
    var type: String? = null,

    @ColumnInfo(defaultValue = "false")
    var isPremium: Boolean? = null,
) {
}