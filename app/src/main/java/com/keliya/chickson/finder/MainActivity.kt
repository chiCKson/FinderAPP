package com.keliya.chickson.finder


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var mAuth:FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth= FirebaseAuth.getInstance()
        progressBar3.progress = 10
        signin.setOnClickListener { v ->
            progressBar3.visibility=View.VISIBLE
            registerUser()
        }
    }
    fun checkVerified() {
        val intent=LoginActivity.newIntent(this)
        progressBar3.visibility=View.INVISIBLE
        startActivity(intent)
    }
    fun clearAll(){
        editTextEmail.text.clear()
        editTextPassword.text.clear()
    }
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)
    }
    private fun registerUser() {
        val mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        val editor = mPrefs.edit()
        editor.putBoolean("is_edited", false)
        editor.commit()
        //getting email and password from edit texts
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        if (!validateForm()) {
            progressBar3.visibility=View.INVISIBLE
            return
        }
        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    //checking if success
                    if (task.isSuccessful) {
                        //display some message here
                        sendEmailVerification()
                        clearAll()
                        Toast.makeText(this@MainActivity, "Successfully registered", Toast.LENGTH_LONG).show()
                        checkVerified()
                    } else {
                        progressBar3.visibility=View.INVISIBLE
                        //display some message here
                        Toast.makeText(this@MainActivity, "Registration Error", Toast.LENGTH_LONG).show()
                    }
                }
    }
    private fun sendEmailVerification() {
        val user = mAuth!!.currentUser
        user!!.sendEmailVerification()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@MainActivity,
                                "Verification email sent to " + user.email!!,
                                Toast.LENGTH_SHORT).show()
                    } else {
                        //Log.e(FragmentActivity.TAG, "sendEmailVerification", task.exception)
                        Toast.makeText(this@MainActivity,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
        // [END send_email_verification]
    }

    private fun validateForm(): Boolean {
        var valid = true
        val email = editTextEmail.text.toString().trim()
        if (TextUtils.isEmpty(email)) {
            editTextEmail.error = "Required."
            valid = false
        } else {
            editTextEmail.error = null
        }
        val password = editTextPassword.text.toString().trim()
        if (TextUtils.isEmpty(password)) {
            editTextPassword.error = "Required."
            valid = false
        } else {
            editTextPassword.error = null
        }
        val passwordConfirm = editTextPasswordconfirm.text.toString().trim()
        if (TextUtils.isEmpty(passwordConfirm)) {
            editTextPasswordconfirm.error = "Required."
            valid = false
        } else {
            editTextPasswordconfirm.error = null
        }
        if(password!=passwordConfirm){
            editTextPasswordconfirm.error = "Password not matched."
            valid = false
        } else {
            editTextPasswordconfirm.error = null
        }
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.error = null
        }else{
            editTextEmail.error = "Enter a valid email."
        }
        if(password.length>6){
            editTextPassword.error = null
        }else{
            editTextPassword.error = "Password should be greater than 6 characters "
        }
        return valid
    }
    companion object{
        fun newIntent(context:Context): Intent {
            val intent= Intent(context,MainActivity::class.java)
            return intent
        }
    }
}
