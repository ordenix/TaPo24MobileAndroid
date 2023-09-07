package pl.tapo24.twa.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WhatsNews(

    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(defaultValue = "0")
    val date: Int,
    @ColumnInfo(defaultValue = "")
    val title: String,
    @ColumnInfo(defaultValue = "")
    val p1: String,
    @ColumnInfo(defaultValue = "")
    val p2: String,
    @ColumnInfo(defaultValue = "")
    val p3: String,
    @ColumnInfo(defaultValue = "")
    val p4: String,
    @ColumnInfo(defaultValue = "")
    val footerBold: String,
    @ColumnInfo(defaultValue = "")
    val footer: String,
    @ColumnInfo(defaultValue = "")
    val author: String
)