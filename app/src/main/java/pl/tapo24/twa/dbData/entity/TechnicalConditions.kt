package pl.tapo24.twa.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TechnicalConditions(
    @PrimaryKey(autoGenerate = false)
    var id: Long,

    @ColumnInfo(defaultValue = "")
    var type: String? = null,

    @ColumnInfo(defaultValue = "")
    var title: String? = null,

    @ColumnInfo(defaultValue = "")
    var requirements: String? = null,

    @ColumnInfo(defaultValue = "")
    var remarks: String? = null,
)
