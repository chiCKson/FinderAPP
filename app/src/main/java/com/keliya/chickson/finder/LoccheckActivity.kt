package com.keliya.chickson.finder

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_loccheck.*

class LoccheckActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loccheck)
        button2.setOnClickListener { v ->
            val callGPSSettingIntent = Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(callGPSSettingIntent)
        }
    }
    override fun onRestart() {
        super.onRestart()
        if (haveNetworkConnection()) {
            if (!locationEnabled()) {
                val intent = LoccheckActivity.newIntent(this)
                startActivity(intent)
            } else {
                val intent = LoginActivity.newIntent(this)
                startActivity(intent)
            }
        }else{
            val intent = NetworkActivity.newIntent(this)
            startActivity(intent)
        }
    }
    private fun haveNetworkConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
    private fun locationEnabled():Boolean{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    companion object{
        fun newIntent(context: Context): Intent {
            val intent= Intent(context,LoccheckActivity::class.java)
            return intent
        }
    }
}

