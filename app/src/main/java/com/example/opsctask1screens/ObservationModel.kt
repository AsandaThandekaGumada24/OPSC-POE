package com.example.opsctask1screens

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class ObservationModel(
    val subId: String,
    val speciesCode: String,
    val comName: String,
    val sciName: String,
    val locName: String,
    val locId: String,
    val obsName: String,
    val obsValid: Boolean,
    val obsReviewed: Boolean,
    val locationPrivate: Boolean,
    val howMany: Int,
    val latitude: Double,
    val longitude: Double,
    val birdImageUrl: String = ""
) : Parcelable {
    companion object : Parceler<ObservationModel> {

        override fun ObservationModel.write(parcel: Parcel, flags: Int) {
            parcel.writeString(subId)
            parcel.writeString(speciesCode)
            parcel.writeString(comName)
            parcel.writeString(sciName)
            parcel.writeString(locName)
            parcel.writeString(locId)
            parcel.writeString(obsName)
            parcel.writeByte(if (obsValid) 1 else 0)
            parcel.writeByte(if (obsReviewed) 1 else 0)
            parcel.writeByte(if (locationPrivate) 1 else 0)
            parcel.writeInt(howMany)
            parcel.writeDouble(latitude)
            parcel.writeDouble(longitude)
            parcel.writeString(birdImageUrl)
        }

        override fun create(parcel: Parcel): ObservationModel {
            return ObservationModel(
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readInt(),
                parcel.readDouble(),
                parcel.readDouble(),
                parcel.readString() ?: ""
            )
        }
    }

    override fun describeContents(): Int {
        return 0
    }
}
