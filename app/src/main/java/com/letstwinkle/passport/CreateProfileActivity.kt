package com.letstwinkle.passport

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.google.firebase.database.*
import com.letstwinkle.passport.databinding.ActivityCreateBinding

class CreateProfileActivity : Activity(), CreateProfilePresenter {

    private lateinit var binding: ActivityCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create)
        binding.profile = Profile()
        binding.presenter = this
        binding.gender.adapter = InitialEmptyEnumAdapter(this, Profile.Gender::class.java)
    }

    override fun updateSaveEnabled() {
        binding.root.post {
            val profile = binding.profile!!
            binding.saveButton.isEnabled = binding.age.text.toString().toIntOrNull() ?: 0 > 0
                && profile.name.isNotEmpty()
                && profile.gender != null
        }
    }

    override fun saveProfile() {
        val db = FirebaseDatabase.getInstance()
        val newUser = db.getReference("users").push()
        newUser.setValue(binding.profile){ error: DatabaseError?, p1: DatabaseReference? ->
            if (error == null)
                finish()
            else {
                val errorDialog = SimpleDialogFragment()
                errorDialog.title = R.string.db_error
                errorDialog.messageCS = error.message
                errorDialog.show(fragmentManager, "error")
            }
        }
    }
}