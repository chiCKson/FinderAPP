package com.keliya.chickson.finder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bus_scroll.*
import kotlinx.android.synthetic.main.content_bus_scroll.*
class BusScrollActivity : AppCompatActivity() {
    var strBus:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_scroll)
        strBus = intent.getStringExtra("bus_id")
        tbDetailedBus.text=strBus
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "This is snack", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
    companion object{
        fun newIntent(context: Context): Intent {
            val intent= Intent(context,BusScrollActivity::class.java)
            return intent
        }
    }
}
