package com.keliya.chickson.finder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_bus.*

class BusActivity : AppCompatActivity() {
    val lst = ArrayList<String>()
    var myRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)
        initBusMenu()
    }
    private inner class DrawerItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
           val txt:String= routelist.getItemAtPosition(position).toString()
            selectItem(position,txt)
        }
    }
    private fun selectItem(position: Int,posTxt:String) {

        val intent = Intent(this,DetailedBusActivity::class.java);
        intent.putExtra("route_id", posTxt)
        startActivity(intent);
    }
    fun setList(list:ArrayList<String>){
        val itemsAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
        routelist.adapter=itemsAdapter
        progressBar6.visibility=View.INVISIBLE
        routelist.onItemClickListener=DrawerItemClickListener()
    }
    private fun initBusMenu() {
        myRef.child("buses").addValueEventListener( object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               // Result will be holded Here
                for (dsp in dataSnapshot.getChildren()) {
                    lst.add(dsp.key.toString()) //add result into array list
                }
                setList(lst)
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Toast.makeText(this@BusActivity, "Error in Reading Database.", Toast.LENGTH_LONG).show()
            }
        })
    }
    companion object{
        fun newIntent(context: Context): Intent {
            val intent= Intent(context,BusActivity::class.java)
            return intent
        }
    }
}

