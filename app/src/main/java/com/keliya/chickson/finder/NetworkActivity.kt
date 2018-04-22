package com.keliya.chickson.finder

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_network.*

class NetworkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)
        button3.setOnClickListener { v ->
            val callNetwork = Intent(
                    android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS )
            startActivity(callNetwork)

        }

    }
    override fun onRestart() {
        super.onRestart()
        if (locationEnabled()) {
            if (!haveNetworkConnection()) {

                val intent = NetworkActivity.newIntent(this)
                startActivity(intent)
            } else {
                val intent = LoginActivity.newIntent(this)
                startActivity(intent)
            }
        }else{
            val intent = LoccheckActivity.newIntent(this)
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
            val intent= Intent(context,NetworkActivity::class.java)
            return intent
        }
    }
}
