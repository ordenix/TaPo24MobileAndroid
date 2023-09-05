package pl.tapo24.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.tapo24.data.EnginesType

@Entity
data class Tariff(

    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(defaultValue = "")
    val category: String?,

    @ColumnInfo(defaultValue = "")
    val code: String?,

    @ColumnInfo(defaultValue = "")
    val law: String?,

    @ColumnInfo(defaultValue = "0")
    val maxSpeed: Int?,

    @ColumnInfo(defaultValue = "0")
    val minSpeed: Int?,

    @ColumnInfo(defaultValue = "")
    val name: String?,

    @ColumnInfo(defaultValue = "")
    val paragraph: String?,

    @ColumnInfo(defaultValue = "")
    val path: String?,

    @ColumnInfo(defaultValue = "0")
    val points: String?,

    @ColumnInfo(defaultValue = "")
    val subName: String?,

    @ColumnInfo(defaultValue = "")
    val tax: String?,

    @ColumnInfo(defaultValue = "")
    val taxRecidive: String?,

    @ColumnInfo(defaultValue = "false")
    val recidive: Boolean?,

    @ColumnInfo(defaultValue = "")
    val text: String?,

    @ColumnInfo(defaultValue = "1")
    val enginesType: EnginesType = EnginesType.New,

    @ColumnInfo(defaultValue = "false")
    val favorites: Boolean = false,

    @ColumnInfo(defaultValue = "")
    val color: String = "",

    @ColumnInfo(defaultValue = "")
    val customCategory: String = "",
)