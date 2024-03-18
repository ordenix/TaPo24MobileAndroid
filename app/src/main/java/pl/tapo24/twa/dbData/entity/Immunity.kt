package pl.tapo24.twa.dbData.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Immunity(

    @PrimaryKey(autoGenerate = false)
    var id: Long,

    @ColumnInfo(defaultValue = "")
    var person: String? = null,

    @ColumnInfo(defaultValue = "")
    var type: String? = null,

    @ColumnInfo(defaultValue = "")
    var p1: String? = null,

    @ColumnInfo(defaultValue = "")
    var p2: String? = null,

    @ColumnInfo(defaultValue = "")
    var p3: String? = null,

    @ColumnInfo(defaultValue = "")
    var p4: String? = null,

    @ColumnInfo(defaultValue = "")
    var p5: String? = null,
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
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
        parcel.writeLong(id)
        parcel.writeString(person)
        parcel.writeString(type)
        parcel.writeString(p1)
        parcel.writeString(p2)
        parcel.writeString(p3)
        parcel.writeString(p4)
        parcel.writeString(p5)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Immunity> {
        override fun createFromParcel(parcel: Parcel): Immunity {
            return Immunity(parcel)
        }

        override fun newArray(size: Int): Array<Immunity?> {
            return arrayOfNulls(size)
        }
    }

}
