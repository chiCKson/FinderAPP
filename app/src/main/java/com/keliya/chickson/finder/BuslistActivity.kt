package com.keliya.chickson.finder

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_buslist.*

class BuslistActivity : AppCompatActivity() {
    var strBus:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buslist)
        strBus = intent.getStringExtra("bus_id")
        textView3.text=strBus
    }
    companion object{
        fun newIntent(context: Context): Intent {
            val intent= Intent(context,BuslistActivity::class.java)
            return intent
        }
    }
}
