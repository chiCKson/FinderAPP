package com.keliya.chickson.finder

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.security.MessageDigest
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.OnSuccessListener
import android.support.annotation.NonNull
import android.view.View
import com.google.android.gms.tasks.OnFailureListener
import java.net.URI


class EditProfileActivity : AppCompatActivity() {
    var userEmail:String=""
    private var mStorageRef: StorageReference?=null
    var myRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        progressBarEDitprofile.visibility=View.INVISIBLE
        mStorageRef= FirebaseStorage.getInstance().getReference()
        userEmail = intent.getStringExtra("email")
        submit.setOnClickListener { v ->
            val id:String= md5(userEmail)
            val name=tbUn.text.toString().trim()
            val phone=tbPN.text.toString().trim()
            writeProfile(id,name,phone)
            val mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
            val editor = mPrefs.edit()
            editor.putBoolean("is_edited", true)
            editor.commit()
            val intent=ProfileActivity.newIntent(this)
            startActivity(intent)
        }
        textView6.setOnClickListener { v->
            val intent=ForgotPW.newIntent(this)
            startActivity(intent)
        }
        gallery.setOnClickListener { v->
            selectImageInAlbum()
        }

    }

    fun writeProfile(userId:String,name:String,mobileNo:String){
        var user:User=User(name,mobileNo)
        myRef!!.child("userProfiles").child(userId).setValue(user)
        Toast.makeText(this@EditProfileActivity, "Profile Updated.", Toast.LENGTH_LONG).show()
    }

    fun md5(toEncrypt: String): String {
        try {
            val digest = MessageDigest.getInstance("md5")
            digest.update(toEncrypt.toByteArray())
            val bytes = digest.digest()
            val sb = StringBuilder()
            for (i in bytes.indices) {
                sb.append(String.format("%02X", bytes[i]))
            }
            return sb.toString().toLowerCase()
        } catch (exc: Exception) {
            return "" // Impossibru!
        }
    }
    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
        progressBarEDitprofile.visibility=View.VISIBLE
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        val imgUri = data.data
        gallery.setImageURI(imgUri)
        uploadImage(imgUri)
    }
    fun takePhoto() {
        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent1.resolveActivity(packageManager) != null) {
            startActivityForResult(intent1, REQUEST_TAKE_PHOTO)
        }
    }
    fun uploadImage(file:Uri){
        val riversRef = mStorageRef!!.child("profileImages").child(md5(userEmail))
        val uploadTask:UploadTask = riversRef.putFile(file)
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener( {
            // Handle unsuccessful uploads
            Toast.makeText(this,"Upload not successed.Please select different image.", Toast.LENGTH_SHORT).show()
        }).addOnSuccessListener( { taskSnapshot ->
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            progressBarEDitprofile.visibility=View.INVISIBLE
            Toast.makeText(this,"upload success.", Toast.LENGTH_SHORT).show()
            val downloadUrl = taskSnapshot.downloadUrl
        })
    }

    companion object{
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
        fun newIntent(context: Context): Intent {
            val intent= Intent(context,EditProfileActivity::class.java)
            return intent
        }
    }
}

class User(username:String,mobileNo:String ){
    var username:String =username
    var mobileNo:String=mobileNo
}
