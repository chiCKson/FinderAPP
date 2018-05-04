package com.keliya.chickson.finder

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import  android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private var mAuth:FirebaseAuth?=null
    var backButtonCount:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth= FirebaseAuth.getInstance()
        progressBar2.progress = 10
        // checking user has logged in before
        val mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        if (mPrefs.getBoolean("is_logged_before", false)) {
            login()
        }
        forgotPW.setOnClickListener { v ->
            val intent=ForgotPW.newIntent(this)
            startActivity(intent)
        }
        SignUp.setOnClickListener { v ->
            val intent=MainActivity.newIntent(this)
             startActivity(intent)
        }
        signinlogin.setOnClickListener { v ->
            progressBar2.visibility=View.VISIBLE
            loginUser()
        }
    }
    private fun login() {
        val intent=MenuActivity.newIntent(this)
        progressBar2.visibility=View.INVISIBLE
        startActivity(intent)
    }
    private fun loginUser() {
        //getting email and password from edit texts
        val email = editTextEmailLogin.text.toString().trim()
        val password = editTextPasswordLogin.text.toString().trim()
        if (!validateForm()) {
            progressBar2.visibility=View.INVISIBLE
            return
        }
        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    //checking if success
                    if (task.isSuccessful) {
                        val mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
                        val editor = mPrefs.edit()
                        editor.putBoolean("is_logged_before", true)
                        editor.commit()
                        clearAll()
                        login()
                    } else {
                        //display some message here
                        progressBar2.visibility=View.INVISIBLE
                        Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_LONG).show()
                    }
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
    private fun validateForm(): Boolean {
        var valid = true
        val email = editTextEmailLogin.text.toString().trim()
        if (TextUtils.isEmpty(email)) {
            editTextEmailLogin.error = "Required."
            valid = false
        } else {
            editTextEmailLogin.error = null
        }
        val password = editTextPasswordLogin.text.toString().trim()
        if (TextUtils.isEmpty(password)) {
            editTextPasswordLogin.error = "Required."
            valid = false
        } else {
            editTextPasswordLogin.error = null
        }
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmailLogin.error = null
        }else{
            editTextEmailLogin.error = "Please enter a valid email address."
        }
        return valid
    }
    fun clearAll(){
        editTextEmailLogin.text.clear()
        editTextPasswordLogin.text.clear()
    }
    companion object{
        fun newIntent(context: Context): Intent {
            val intent= Intent(context,LoginActivity::class.java)
            return intent
        }
    }
}
