package com.example.firebasedeneme

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.ArkadaslarimBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class Arkadaslarim:AppCompatActivity(){

    lateinit var mAuth: FirebaseAuth
    var mUsersRef: DatabaseReference? = null
    var arkadaslarim: DatabaseReference? = null
    lateinit var mUser: FirebaseUser

    lateinit var binding: ArkadaslarimBinding
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ArkadaslarimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mUsersRef= FirebaseDatabase.getInstance().getReference().child("users")
        arkadaslarim= FirebaseDatabase.getInstance().getReference().child("Arkadaslarim")
        mAuth=FirebaseAuth.getInstance()                            //com.google.firebase.auth.internal.zzv@...
        mUser= mAuth.currentUser!!


        var arkadaslarinIdleri:String=intent.extras?.getString("arkadaslarim").toString()
        //Log.i("arkadaşlarım",arkadaslarinIdleri)
        var arkIdleri:List<String> = arrayListOf()
        var k_adlari:List<String> = arrayListOf()
        var arkadaslarinkullaniciadlari:List<String> = arrayListOf()
        var kullaniciadlari:ArrayList<String> = ArrayList<String>()

        arkIdleri=arkadaslarinIdleri.split(",")  //string gelen arkadaş idleri listeye atandı.


            arkadaslarimGoruntuleme(arkIdleri)







    }

    private fun arkadaslarimGoruntuleme(kAdlari: List<String>) {
        //Log.i("arkadaşlarım::",kAdlari.toString())
        val itemsAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(applicationContext, R.layout.simple_list_item_1, kAdlari)
             binding.userList.adapter = itemsAdapter         //listede arkadaşlarımız gözüktü.


    }



}