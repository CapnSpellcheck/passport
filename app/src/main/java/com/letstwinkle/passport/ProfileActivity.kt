package com.letstwinkle.passport

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.AsyncTask
import android.os.Bundle
import android.util.Base64
import com.google.firebase.database.*
import com.letstwinkle.passport.databinding.ActivityProfileBinding

class ProfileActivity : Activity(), EditProfilePresenter {
    lateinit var profileRef: DatabaseReference
    lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val refUrl = this.intent.getStringExtra(IntentExtras.SnapshotURL)
        if (refUrl == null) {
            finish()
            return
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        profileRef = FirebaseDatabase.getInstance().getReferenceFromUrl(refUrl)
        profileRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                finish()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                binding.profile = snapshot.getValue(Profile::class.java)
                binding.profile!!.imageData?.let { encodedData ->
                    val imageData = Base64.decode(encodedData, 0)
                    UIUtils.renderImage(imageData, binding.image)
                }
            }
        })
        binding.presenter = this
    }

    // We know only the hobbies can change, so this is optimized to write just that.
    override fun saveProfile() {
        profileRef.child("hobbies").setValue(binding.profile!!.hobbies)
    }

    override fun deleteProfile() {
        val overlay = UIUtils.showOverlay(this, R.layout.view_loading)
        profileRef.removeValue() { error: DatabaseError?, p1: DatabaseReference? ->
            UIUtils.hideOverlay(this)
            if (error == null)
                finish()
            else {
                // The profile could have been deleted by another running instance.
                // FOr a professional version, deletion should be detected, the app should
                // inform the user and the activity should finish.
                val errorDialog = SimpleDialogFragment()
                errorDialog.title = R.string.db_error
                errorDialog.messageCS = error.message
                errorDialog.show(fragmentManager, "error")
            }
        }
    }
}