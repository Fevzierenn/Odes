package com.example.firebasedeneme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedeneme.databinding.AdmingrupsilactivityBinding
import kotlinx.android.synthetic.main.admingrupsilactivity.*


lateinit var grupİsimleriRecyclerView: RecyclerView
lateinit var grupİsimleriArrayList: ArrayList<AdminGrupSilYonlendirme>
var gruplarinisimleribaba:ArrayList<String> = arrayListOf()
class AdminGrupSil: AppCompatActivity(),AdminGrupSilRecyclerView.OnItemClickListener{
    lateinit var binding:AdmingrupsilactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=AdmingrupsilactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var gruplarinisimleri=intent?.extras?.getStringArrayList("gruplar") as ArrayList<String>
        gruplarinisimleribaba=gruplarinisimleri

        Log.i("rcycler gruplar",gruplarinisimleri.toString())

        grupİsimleriRecyclerView = binding.gruplarinSimleri
        grupİsimleriRecyclerView.layoutManager = LinearLayoutManager(this)
        grupİsimleriRecyclerView.setHasFixedSize(true)
        grupİsimleriArrayList=ArrayList<AdminGrupSilYonlendirme>()

        for (i in gruplarinisimleri){
            var grups= AdminGrupSilYonlendirme(i.trim().toString())
            grupİsimleriArrayList.add(grups)
        }
        grupİsimleriRecyclerView.adapter=AdminGrupSilRecyclerView(grupİsimleriArrayList,this@AdminGrupSil)

    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this,"item ${position} clicked.", Toast.LENGTH_SHORT).show()
        var intent= Intent(this, AdminGelenGrubuSilmefinal::class.java)
        var bundle=Bundle()
        //Log.i("onitem",gruplarinisimleribaba.toString())
        bundle.putInt("pozisyon",position)
        bundle.putString("grupismi",gruplarinisimleribaba.get(position).toString().trim())
        intent.putExtras(bundle)
        startActivity(intent)

    }
}