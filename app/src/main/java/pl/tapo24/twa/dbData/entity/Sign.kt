package pl.tapo24.twa.dbData.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Sign(
    @PrimaryKey(autoGenerate = false)
    var id: Long? = null,

    @ColumnInfo(defaultValue = "")
    var name: String? = null,

    @ColumnInfo(defaultValue = "")
    var description: String? = null,

    @ColumnInfo(defaultValue = "")
    var path: String? = null,

    @ColumnInfo(defaultValue = "")
    var linkOld: String? = null,

    @ColumnInfo(defaultValue = "")
    var linkNew: String? = null,

    @ColumnInfo(defaultValue = "")
    var signCategory: String? = null,

    @ColumnInfo(defaultValue = "")
    var signExtendDescription: String? = null

) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(path)
        parcel.writeString(linkOld)
        parcel.writeString(linkNew)
        parcel.writeString(signCategory)
        parcel.writeString(signExtendDescription)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sign> {
        override fun createFromParcel(parcel: Parcel): Sign {
            return Sign(parcel)
        }

        override fun newArray(size: Int): Array<Sign?> {
            return arrayOfNulls(size)
        }
    }
}
