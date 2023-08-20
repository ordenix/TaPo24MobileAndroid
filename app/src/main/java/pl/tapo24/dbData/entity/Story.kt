package pl.tapo24.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Story(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,

    @ColumnInfo(defaultValue = "0")
    var link: Int = 0,

    @ColumnInfo(defaultValue = "")
    var type: String = "",

    @ColumnInfo(defaultValue = "")
    var stageTitle: String = "",

    @ColumnInfo(defaultValue = "")
    var stageDescription: String = "",

    @ColumnInfo(defaultValue = "")
    var stageDescriptionEnum: String = "",

    @ColumnInfo(defaultValue = "")
    var optionStage1: String? = null,

    @ColumnInfo(defaultValue = "")
    var optionStage2: String? = null,

    @ColumnInfo(defaultValue = "0")
    var linkOption1: Int? = null,
    @ColumnInfo(defaultValue = "0")
    var linkOption2: Int? = null,

    @ColumnInfo(defaultValue = "false")
    var finalStep: Boolean = false

) {
}