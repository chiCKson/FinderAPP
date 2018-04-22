package com.keliya.chickson.finder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.security.MessageDigest
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.keliya.chickson.finder.R.id.imageView
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class ProfileActivity : AppCompatActivity() {
    var userEmail:String=""
    private var mStorageRef: StorageReference?=null
    var myRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        mStorageRef= FirebaseStorage.getInstance().getReference();
        userEmail = intent.getStringExtra("email")
        readProfile()
        downloadImage()
        button4.setOnClickListener { v->
            val intent = Intent(this,EditProfileActivity::class.java);
            intent.putExtra("email", userEmail)
            startActivity(intent);
        }

    }

    override fun onResume() {
        super.onResume()
        downloadImage()
    }
    fun readProfile(){
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username:String?=dataSnapshot.child("userProfiles").child(md5(userEmail)).child("username").getValue(String::class.java)
                val phone:String?=dataSnapshot.child("userProfiles").child(md5(userEmail)).child("mobileNo").getValue(String::class.java)
                textView.text=username
                verifyTv.text=userEmail
                textView4.text=phone
                progressBar7.visibility=View.INVISIBLE
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "error", Toast.LENGTH_LONG).show()
            }
        })
    }
    fun downloadImage(){
       Glide.with(this /* context */)
                .using(FirebaseImageLoader())
                .load(mStorageRef!!.child("profileImages").child(md5(userEmail)))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(profile_image)
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
    override fun onBackPressed(){
        val intent=MenuActivity.newIntent(this)
        startActivity(intent)
    }
    companion object{
        fun newIntent(context: Context): Intent {
            val intent= Intent(context,ProfileActivity::class.java)
            return intent
        }
    }
}
