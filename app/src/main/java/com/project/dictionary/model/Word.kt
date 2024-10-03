package com.project.dictionary.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    @PrimaryKey @NonNull
    val wordId: String = "",
    val wordName: String = "",
    val wordDescription: String = "",
    val wordFullDescription: String = "",
    val wordLink: String = "",
    val wordImage: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(wordId)
        parcel.writeString(wordName)
        parcel.writeString(wordDescription)
        parcel.writeString(wordFullDescription)
        parcel.writeString(wordLink)
        parcel.writeString(wordImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Word> {
        override fun createFromParcel(parcel: Parcel): Word {
            return Word(parcel)
        }

        override fun newArray(size: Int): Array<Word?> {
            return arrayOfNulls(size)
        }
    }
}
