package pl.tapo24.twa.data.survey

import android.os.Parcel
import android.os.Parcelable

data class SurveyAnswer(
    val answer: String?,
    val id: Int? = null,
    var sortId: Int? = null,
    var isSelected: Boolean = false
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(answer)
        parcel.writeValue(id)
        parcel.writeValue(sortId)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SurveyAnswer> {
        override fun createFromParcel(parcel: Parcel): SurveyAnswer {
            return SurveyAnswer(parcel)
        }

        override fun newArray(size: Int): Array<SurveyAnswer?> {
            return arrayOfNulls(size)
        }
    }


}