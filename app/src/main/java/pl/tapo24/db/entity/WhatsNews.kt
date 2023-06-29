package pl.tapo24.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WhatsNews(

    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val date: Int,
    val title: String,
    val p1: String,
    val p2: String,
    val p3: String,
    val p4: String,
    val footerBold: String,
    val footer: String,
    @ColumnInfo(defaultValue = "")
    val author: String
)