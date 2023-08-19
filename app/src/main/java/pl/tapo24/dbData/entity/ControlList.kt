package pl.tapo24.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ControlList(
    @PrimaryKey(autoGenerate = false)
    var id: Long,

    @ColumnInfo(defaultValue = "")
    var titleId: Int? = null,

    @ColumnInfo(defaultValue = "")
    var subTitleDepth1Name: String? = null,

    @ColumnInfo(defaultValue = "")
    var subTitleDepth2Name: String? = null,

    @ColumnInfo(defaultValue = "")
    var position: String? = null,

    @ColumnInfo(defaultValue = "")
    var positionCode: String? = null,

    @ColumnInfo(defaultValue = "")
    var methods: String? = null,

    @ColumnInfo(defaultValue = "")
    var methodsRemarks: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameA1: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsA1: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameA2: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsA2: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameA3: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsA3: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameB1: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsB1: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameB2: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsB2: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameB3: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsB3: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameC1: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsC1: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameC2: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsC2: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameC3: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsC3: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameD1: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsD1: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameD2: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsD2: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameD3: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsD3: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameE1: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsE1: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameE2: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsE2: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameE3: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsE3: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameF1: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsF1: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameF2: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsF2: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameF3: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsF3: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameG1: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsG1: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameG2: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsG2: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameG3: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsG3: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameH1: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsH1: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameH2: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsH2: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameH3: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsH3: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameI1: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsI1: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameI2: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsI2: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameI3: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsI3: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameJ1: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsJ1: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameJ2: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsJ2: Int? = null,

    @ColumnInfo(defaultValue = "")
    var standardNameJ3: String? = null,

    @ColumnInfo(defaultValue = "")
    var standardLevelFaultsJ3: Int? = null
) {
}