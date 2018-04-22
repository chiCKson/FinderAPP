package com.keliya.chickson.finder

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_pw.*


class ForgotPW : AppCompatActivity() {
    private var mAuth: FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pw)
        mAuth= FirebaseAuth.getInstance()
        progressBar4.progress=10
        rstPWbtn.setOnClickListener { v ->
            progressBar4.visibility=View.VISIBLE
            sendResetEmail()
        }
    }
    companion object{
        fun newIntent(context: Context): Intent {
            val intent= Intent(context,ForgotPW::class.java)
            return intent
        }
    }
    fun sendResetEmail() {
        var emailAddress:String =fpwEmail.text.toString().trim()
        mAuth!!.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(this) { task ->
                    //checking if success
                    if (task.isSuccessful) {
                        Toast.makeText(this@ForgotPW, "Reset Email send to email address you provided.", Toast.LENGTH_LONG).show()
                       val intent=LoginActivity.newIntent(this)
                        progressBar4.visibility=View.INVISIBLE
                        startActivity(intent)
                    }

                }
    }

}
