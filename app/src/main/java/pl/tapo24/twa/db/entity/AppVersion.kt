package pl.tapo24.twa.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class AppVersion (

    @PrimaryKey(autoGenerate = false)
    var id: Long,

    @ColumnInfo(defaultValue = "0")
    var versionCode: Int,
    @ColumnInfo(defaultValue = "")
    var versionName: String,
    @ColumnInfo(defaultValue = "false")
    var critical: Boolean,
    @ColumnInfo(defaultValue = "")
    var whatChange: String,
    @ColumnInfo(defaultValue = "false")
    var dismissed: Boolean
)