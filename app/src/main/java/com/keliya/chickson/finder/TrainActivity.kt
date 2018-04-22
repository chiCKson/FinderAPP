package com.keliya.chickson.finder

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class TrainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train)
    }
    companion object{
        fun newIntent(context: Context): Intent {
            val intent= Intent(context,TrainActivity::class.java)
            return intent
        }
    }
}
