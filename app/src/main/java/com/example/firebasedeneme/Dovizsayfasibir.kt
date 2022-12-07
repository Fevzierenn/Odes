package com.example.firebasedeneme

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.KursayfasibaslangicActivityBinding
import kotlinx.android.synthetic.main.kursayfasibaslangic_activity.*

class Dovizsayfasibir: AppCompatActivity(){
    lateinit var binding: KursayfasibaslangicActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= KursayfasibaslangicActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextpage.setOnClickListener {
            var deger = binding.kurundegeri.text.toString()

            if (TextUtils.isEmpty(deger.trim())){
                binding.kurundegeri.error="Boş Bırakılamaz"
                return@setOnClickListener
            }

            else{

                nextToOtherPage(deger.trim())
            }

        }

    }

    private fun nextToOtherPage(deger:String) {
        var intent=Intent(this, Dovizler::class.java)
        var bundle=Bundle()
        Log.i("deger",deger.toString())
        bundle.putString("deger",deger.trim())
        intent.putExtras(bundle)
        startActivity(intent)
    }
}