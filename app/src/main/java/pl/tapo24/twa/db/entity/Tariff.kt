package pl.tapo24.twa.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.tapo24.twa.data.EnginesType

@Entity
data class Tariff(

    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(defaultValue = "")
    var category: String?,

    @ColumnInfo(defaultValue = "")
    var code: String?,

    @ColumnInfo(defaultValue = "")
    var law: String?,

    @ColumnInfo(defaultValue = "0")
    var maxSpeed: Int?,

    @ColumnInfo(defaultValue = "0")
    var minSpeed: Int?,

    @ColumnInfo(defaultValue = "")
    var name: String?,

    @ColumnInfo(defaultValue = "")
    var paragraph: String?,

    @ColumnInfo(defaultValue = "")
    var path: String?,

    @ColumnInfo(defaultValue = "0")
    var points: String?,

    @ColumnInfo(defaultValue = "")
    var subName: String?,

    @ColumnInfo(defaultValue = "")
    var tax: String?,

    @ColumnInfo(defaultValue = "")
    var taxRecidive: String?,

    @ColumnInfo(defaultValue = "false")
    var recidive: Boolean?,

    @ColumnInfo(defaultValue = "")
    val text: String?,

    @ColumnInfo(defaultValue = "New")
    var enginesType: EnginesType = EnginesType.New,

    @ColumnInfo(defaultValue = "false")
    var favorites: Boolean = false,

    @ColumnInfo(defaultValue = "")
    val color: String = "",

    @ColumnInfo(defaultValue = "")
    val customCategory: String = "",

    @ColumnInfo(defaultValue = "0")
    val sortOrder: Int = 0,

    @ColumnInfo(defaultValue = "0")
    var sortOrderFav: Int = 0,

    @ColumnInfo(defaultValue = "false")
    var court: Boolean = false,

    @ColumnInfo(defaultValue = "")
    var remarks: String?,

    @ColumnInfo(defaultValue = "false")
    var premiumIcon: Boolean = false,

)