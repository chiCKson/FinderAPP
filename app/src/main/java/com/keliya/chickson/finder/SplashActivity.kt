package com.keliya.chickson.finder

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.widget.Toast
import android.location.LocationManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        finish()
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
}
