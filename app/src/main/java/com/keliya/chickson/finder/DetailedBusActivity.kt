package com.keliya.chickson.finder

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detailed_bus.*

class DetailedBusActivity : AppCompatActivity() {
    var myRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    var strRoute: String?=null
    val lst = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_bus)
        strRoute = intent.getStringExtra("route_id")
        initBusDetails()
    }
    private fun initBusDetails() {
        myRef.child("buses").child(strRoute).addValueEventListener( object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Result will be holded Here
                for (dsp in dataSnapshot.getChildren()) {
                    lst.add(dsp.key.toString()) //add result into array list
                }
                setList(lst)
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                // Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                Toast.makeText(this@DetailedBusActivity, "not read", Toast.LENGTH_LONG).show()
            }
        })
    }
    fun setList(list:ArrayList<String>){

        val itemsAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
        buslist.adapter=itemsAdapter

        buslist.onItemClickListener=DrawerItemClickListener()
    }
    private inner class DrawerItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            val txt:String= buslist.getItemAtPosition(position).toString()
            selectItem(position,txt)
        }
    }
    private fun selectItem(position: Int,posTxt:String) {

        val intent = Intent(this,BusScrollActivity::class.java);
        intent.putExtra("bus_id", posTxt)
        startActivity(intent);
    }
    companion object{
        fun newIntent(context: Context): Intent {
            val intent= Intent(context,DetailedBusActivity::class.java)
            return intent
        }
    }
}
