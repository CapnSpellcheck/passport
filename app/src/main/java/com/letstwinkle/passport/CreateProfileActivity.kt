package com.letstwinkle.passport

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Base64
import com.google.common.io.ByteStreams
import com.google.firebase.database.*
import com.letstwinkle.passport.databinding.ActivityCreateBinding

private const val RC_GET_STORAGE_PERM = 1
private const val RC_IMAGE_PICKER = 2

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

    override fun selectImage() {
        val perm = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(perm), RC_GET_STORAGE_PERM)
            return
        }
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        this.startActivityForResult(intent, com.letstwinkle.passport.RC_IMAGE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.data?.let { uri ->
            val stream = contentResolver.openInputStream(uri)
            // professional version: scale the image to a max size
            // If the image is too large, the completion listener seems to not get fired, and the
            // profile appears to be saved without the image blob (maybe there's some transfer time limit)
            val imageData = ByteStreams.toByteArray(stream)
            binding.profile!!.imageData = Base64.encodeToString(imageData, 0)
            UIUtils.renderImage(imageData, binding.image)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage()
        }
    }

}