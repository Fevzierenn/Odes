package com.example.firebasedeneme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.DovizdegerlerinialmaBinding

class DovizDegerleriniAlma:AppCompatActivity() {
    lateinit var binding:DovizdegerlerinialmaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DovizdegerlerinialmaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var pozisyon: Int? = intent?.extras?.getInt("position")
        var kurlar:ArrayList<String> =intent?.extras?.getStringArrayList("dovizdegerleri") as ArrayList<String>
        var deger:String=intent?.extras?.getString("deger").toString().trim()
        var songuncelleme:String=intent?.extras?.getString("songuncelleme").toString().trim()

        Log.i("deger",deger)
        Log.i("pozisyon",pozisyon.toString())
        Log.i("dovizler",kurlar.toString())
        Log.i("son guncelleme aa",songuncelleme.toString())



        binding.deger.setText(deger.toString().trim())
        binding.birimkurdeger.setText(kurlar.get(pozisyon!!).toString().trim())
        binding.karsililgi.setText(hesaplama(deger,pozisyon,kurlar))
        binding.songuncelleme.setText(songuncelleme.toString())

        binding.home2.setOnClickListener {
            var intent= Intent(this,ProgramMerhabaSayfasi::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun hesaplama(deger: String, pozisyon: Int?, kurlar: ArrayList<String>):String {
            var kurOrani:Double= kurlar.get(pozisyon!!).trim().toDouble()
            var sonuc:Double= (deger.trim().toDouble())/(kurOrani)
            var sonucstring=String.format("%.3f",sonuc).trim()
            Log.i("sonuc",sonuc.toString())
            Log.i("sonucString",sonucstring)
            return sonucstring.toString()

    }
}