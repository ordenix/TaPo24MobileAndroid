package pl.tapo24.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CodeLimitsDrivingLicence(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,


    @ColumnInfo(defaultValue = "")
    var code: String = "",


    @ColumnInfo(defaultValue = "")
    var description: String = "",


    @ColumnInfo(defaultValue = "true")
    var chapter: Boolean = true
) {
}