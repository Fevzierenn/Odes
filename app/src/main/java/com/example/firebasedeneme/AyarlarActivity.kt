package com.example.firebasedeneme

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.AyarlarActivityBinding
import kotlinx.android.synthetic.main.ayarlar_activity.*

class AyarlarActivity:AppCompatActivity() {
    lateinit var binding: AyarlarActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=AyarlarActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.anasayfayadon.setOnClickListener {
            anaekranadon()
        }
        binding.imageexit.setOnClickListener {
            cikisyap()
        }
        binding.imageView2.setOnClickListener {
            sifredegistirmesayfasinagit()
        }
        binding.imagemail.setOnClickListener {
            mailiguncellemesayfasinagit()
        }

    }

    private fun anaekranadon() {
        var intent=Intent(this, ProgramMerhabaSayfasi::class.java)
        startActivity(intent)
        finish()
    }

    private fun mailiguncellemesayfasinagit() {
        var intent=Intent(this,ChangeEmail::class.java)
        startActivity(intent)
    }

    private fun sifredegistirmesayfasinagit() {
        var intent = Intent(this, ChangePassword::class.java)
        startActivity(intent)

    }

    private fun cikisyap() {
        var intent = Intent(this, GirisSayfasi::class.java)
        startActivity(intent)
        finish()
    }
}