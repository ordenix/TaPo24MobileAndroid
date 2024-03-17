package pl.tapo24.twa.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Company(
    @PrimaryKey(autoGenerate = false)
    var id: Long,

    @ColumnInfo(defaultValue = "")
    var name: String? = null,

    @ColumnInfo(defaultValue = "")
    var nipNumber: String? = null,

    @ColumnInfo(defaultValue = "")
    var regonNumber: String? = null,

    @ColumnInfo(defaultValue = "")
    var adress: String? = null,
)
