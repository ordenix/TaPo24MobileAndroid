package pl.tapo24.twa.data.survey

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

data class Survey(
    var active: Boolean = false,
    var endDate: Long = 0,
    val id: Int? = null,
    var name: String? = null,
    var publish: Boolean= false,
    var singleChoice: Boolean = false,
    var startDate: Long = 0,
    var surveyAnswers: ArrayList<SurveyAnswer>? = null,
    var toLoginUser: Boolean? = null,
    var toVipUser: Boolean? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readLong(),
        parcel.createTypedArrayList(SurveyAnswer),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (active) 1 else 0)
        parcel.writeLong(endDate)
        if (id != null) {
            parcel.writeInt(id)
        }
        parcel.writeString(name)
        parcel.writeByte(if (publish) 1 else 0)
        parcel.writeByte(if (singleChoice) 1 else 0)
        parcel.writeLong(startDate)
        parcel.writeTypedList(surveyAnswers)
        parcel.writeByte(if (toLoginUser == true) 1 else 0)
        parcel.writeByte(if (toVipUser == true) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Survey> {
        override fun createFromParcel(parcel: Parcel): Survey {
            return Survey(parcel)
        }

        override fun newArray(size: Int): Array<Survey?> {
            return arrayOfNulls(size)
        }
    }
}