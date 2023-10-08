package pl.tapo24.twa.dbData.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Spb(
    @PrimaryKey(autoGenerate = false)
    var id: Int,

    @ColumnInfo(defaultValue = "")
    var type:String? =null,

    @ColumnInfo(defaultValue = "")
    var title1:String? =null,
    @ColumnInfo(defaultValue = "")
    var list1:String? =null,

    @ColumnInfo(defaultValue = "")
    var title2:String? =null,
    @ColumnInfo(defaultValue = "")
    var list2:String? =null,

    @ColumnInfo(defaultValue = "")
    var title3:String? =null,
    @ColumnInfo(defaultValue = "")
    var list3:String? =null,

    @ColumnInfo(defaultValue = "")
    var title4:String? =null,
    @ColumnInfo(defaultValue = "")
    var list4:String? =null,

    @ColumnInfo(defaultValue = "")
    var title5:String? =null,
    @ColumnInfo(defaultValue = "")
    var list5:String? =null,

    @ColumnInfo(defaultValue = "false")
    var isSpb: Boolean? = null,

    @ColumnInfo(defaultValue = "false")
    var listsHaveBullet: Boolean? = null,

    @ColumnInfo(defaultValue = "false")
    var listsHaveInnerDivider: Boolean? = null,

    @ColumnInfo(defaultValue = "")
    var path:String? =null,

    @ColumnInfo(defaultValue = "")
    var path2:String? =null,

)
