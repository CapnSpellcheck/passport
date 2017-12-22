package com.letstwinkle.passport

import android.content.res.Resources
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.graphics.Bitmap
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.util.Base64
import com.google.firebase.database.Exclude

// Note that I forewent having a "unique integer ID", which is problematic. Firebase DB ensures
// that the unique ID is timestamp-driven, so that lexical sorting by 'key' also sorts chronologically.
// I have to assume that this was the intended effect of "The   list   should   be   sorted   by   ID,   ascending   by   default."
class Profile : BaseObservable {
    @get:Bindable var age: Int = 0
    @get:Bindable var name: String = ""
    @get:Bindable var gender: Gender? = null
    @get:Bindable var hobbies: String = ""
    var imageData: String? = null

    // I had created this before I realized that FbDB's id encapsulates the creation time.
//    var createdAt: Long = 0; private set

    // As per Firebase DB doc
    constructor() {}

    val backgroundColor: Int
        @ColorRes get() {
            return when (gender) {
                Profile.Gender.Male -> R.color.male
                Profile.Gender.Female -> R.color.female
                null -> android.R.color.transparent
            }
        }

    enum class Gender(@StringRes val displayStr: Int) : Displayable {
        Male(R.string.male), Female(R.string.female);

        override fun displayString(res: Resources): String = res.getString(displayStr)
    }
}