package pl.tapo24.twa.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HinNumber(
    @PrimaryKey(autoGenerate = false)
    var id: String? = null,

    @ColumnInfo(defaultValue = "")
    var description: String? = null,
)
