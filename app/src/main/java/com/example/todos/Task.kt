package com.example.todos

import android.os.Parcel
import android.os.Parcelable

data class Task(
    var title: String,
    var isCompleted: Boolean,
    var iconResId: Int = -1, // brak ikony
    var date: String,
    var time: String,
    var notes: String
) : Parcelable {

    // Konstruktor dla ułatwienia tworzenia obiektu bez podawania id ikony
    constructor(
        title: String,
        isCompleted: Boolean,
        date: String,
        time: String,
        notes: String
    ) : this(title, isCompleted, -1, date, time, notes)


    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeByte(if (isCompleted) 1 else 0)
        parcel.writeInt(iconResId)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(notes)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}
