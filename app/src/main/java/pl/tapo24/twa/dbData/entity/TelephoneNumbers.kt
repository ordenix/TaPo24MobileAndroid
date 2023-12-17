package pl.tapo24.twa.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TelephoneNumbers(

    @PrimaryKey(autoGenerate = false)
    var id: Long,

    @ColumnInfo(defaultValue = "")
    var name: String,

    @ColumnInfo(defaultValue = "")
    var phoneNumber: String,

    @ColumnInfo(defaultValue = "")
    var workingTime: String,

    @ColumnInfo(defaultValue = "")
    var branchName: String,

    @ColumnInfo(defaultValue = "false")
    var isBorderGuard: Boolean,

    @ColumnInfo(defaultValue = "false")
    var isRoadInspection: Boolean,

)
