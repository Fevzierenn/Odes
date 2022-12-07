package com.example.firebasedeneme

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.GrubaeklemeBinding
var grubaeklenenler:ArrayList<String> ?= null

class GrubaEkleme : AppCompatActivity(){
    lateinit var binding:GrubaeklemeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=GrubaeklemeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gruptanCKar.visibility= View.INVISIBLE
        var gelenid=intent.extras?.getString("kullaniciId")
        binding.grubaEkle.setOnClickListener{
            grubaeklenenler?.add(gelenid.toString())
            Log.i("gruba eklenenler",gelenid.toString())
            binding.grubaEkle.visibility=View.INVISIBLE
            binding.gruptanCKar.visibility=View.VISIBLE
        }
    }
}