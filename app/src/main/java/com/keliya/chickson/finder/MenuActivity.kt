package com.keliya.chickson.finder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_menu.*
import java.security.MessageDigest


class MenuActivity : AppCompatActivity() {
    private var menuTitles: Array<String>? = null
    private var user:FirebaseUser?=null
    private var mStorageRef: StorageReference?=null
    var backButtonCount:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        mStorageRef= FirebaseStorage.getInstance().getReference();
        user=FirebaseAuth.getInstance().currentUser
        menuTitles = resources.getStringArray(R.array.menu)
        downloadImage()
        left_drawer!!.adapter = ArrayAdapter(this,
                R.layout.drawer_list_item, menuTitles)
        left_drawer.onItemClickListener = DrawerItemClickListener()

        textView.text=user!!.email
        if (user!!.isEmailVerified){
            verifyTv.text="Verified"

        }else{
            verifyTv.text="Not Verified"
        }
        bus.setOnClickListener { v ->
            val intent=BusActivity.newIntent(this)
            startActivity(intent)
        }
        train.setOnClickListener { v ->
            val intent=TrainActivity.newIntent(this)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        downloadImage()
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
    private inner class DrawerItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            selectItem(position)
        }
    }
    override fun onBackPressed(){

        if(backButtonCount>=1) {
            var intent: Intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }else{
            Toast.makeText(this,"Press the back button once again to close the application.", Toast.LENGTH_SHORT).show()
            backButtonCount++
        }
    }
    fun downloadImage(){
        Glide.with(this /* context */)
                .using(FirebaseImageLoader())
                .load(mStorageRef!!.child("profileImages").child(md5(user!!.email.toString())))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(profile_image)
    }
    private fun selectItem(position: Int) {
        if(position==2){
            val intent=MapsActivity.mapIntent(this)
            startActivity(intent)
        }else if(position==1){
            val mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
            if (mPrefs.getBoolean("is_edited", false)){
                val intent = Intent(this,ProfileActivity::class.java);
                intent.putExtra("email", user!!.email)
                startActivity(intent);
            }else{
                val intent = Intent(this,EditProfileActivity::class.java);
                intent.putExtra("email", user!!.email)
                startActivity(intent);
            }
        }
        else if (position==6){
            FirebaseAuth.getInstance().signOut()
            val mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
            val editor = mPrefs.edit()
            editor.putBoolean("is_logged_before", false)
            editor.commit()
            val intent=LoginActivity.newIntent(this)
            startActivity(intent)
        }
        left_drawer!!.setItemChecked(position, true)
        drawer_layout!!.closeDrawer(left_drawer)
    }
    companion object{
        fun newIntent(context:Context):Intent{
            val intent=Intent(context,MenuActivity::class.java)
            return intent
        }
    }
}



