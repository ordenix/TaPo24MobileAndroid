package pl.tapo24.twa.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImmunityAdres(
    @PrimaryKey(autoGenerate = false)
    var id: Long,

    @ColumnInfo(defaultValue = "")
    var person: String? = null,

    @ColumnInfo(defaultValue = "")
    var supervisor: String? = null,

    @ColumnInfo(defaultValue = "")
    var adres: String? = null,

    @ColumnInfo(defaultValue = "")
    var telephone: String? = null,

    @ColumnInfo(defaultValue = "")
    var fax: String? = null,
)
