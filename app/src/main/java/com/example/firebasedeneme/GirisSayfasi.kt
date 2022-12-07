package com.example.firebasedeneme


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.ActivityMainBinding
import com.example.firebasedeneme.databinding.ActivityThreeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.*


class GirisSayfasi:AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    var databaseReference:DatabaseReference?=null
    var database:FirebaseDatabase?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()           //firebase auth ile konuma erişebiliyoruz
        database = FirebaseDatabase.getInstance()
        databaseReference=database?.reference!!.child("profile")


        binding.kayitolbutonu.setOnClickListener {
            val intent=Intent(this,KayitSayfasi::class.java)
            startActivity(intent)

        }
        binding.button2.setOnClickListener {
            var mail=binding.textmailorkadi.text.toString()
            var sifre=binding.textsifre.text.toString()

            if(TextUtils.isEmpty(mail)){
                binding.textmailorkadi.error="Mail kısmı boş bırakılamaz"
                return@setOnClickListener
            }
            else if (TextUtils.isEmpty(sifre)){
                binding.textsifre.error="Şifre kısmı boş bırakılamaz"
                return@setOnClickListener
            }
            else if(mail.equals("admin@admin.com") && sifre.equals("admin123")){
                Toast.makeText(this,"Admin Girişi Başarılı",Toast.LENGTH_SHORT).show()
                adminsayfasinagit()
            }
            else{
                auth.signInWithEmailAndPassword(mail, sifre)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information

                            val user = auth.currentUser
                            Toast.makeText(this,"Giriş Başarılı",Toast.LENGTH_SHORT).show()
                            val intent=Intent(this,ProgramMerhabaSayfasi::class.java)
                            startActivity(intent)
                            //finish()

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(baseContext, "Giriş Başarısız.",
                                Toast.LENGTH_SHORT).show()

                        }
                    }
            }
        }

    }

    private fun adminsayfasinagit() {
        var intent=Intent(this,Admin::class.java)
        startActivity(intent)
        finish()
    }


}