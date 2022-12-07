package com.example.firebasedeneme

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.ActivityThreeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*



class KayitSayfasi:AppCompatActivity() {
    lateinit var binding: ActivityThreeBinding
    lateinit var auth: FirebaseAuth
    var databaseReference:DatabaseReference?=null
    var database:FirebaseDatabase?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityThreeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()           //firebase auth ile konuma erişebiliyoruz
        database = FirebaseDatabase.getInstance()
        databaseReference=database?.reference!!.child("users")


        binding.button.setOnClickListener {
            var mail = binding.mailbox.text.toString()
            var k_Adi = binding.usernamebox.text.toString()
            var sifre = binding.passwordbox.text.toString()
            if(TextUtils.isEmpty(mail)){
                binding.mailbox.error="Mail kısmı boş bırakılamaz"
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(k_Adi)){
                binding.usernamebox.error="Kullanıcı adı kısmı boş bırakılamaz"
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(sifre)){
                binding.passwordbox.error="Sifre kısmı boş bırakılamaz"
                return@setOnClickListener
            }
            //email kullanıcı adı ve şifreyi databasee ekleme işlemleri
            auth.createUserWithEmailAndPassword(mail,sifre)
                .addOnCompleteListener(this){task ->
                    if(task.isComplete){
                        //şu anki kullanıcı bilgilerini alalım.
                        var currentUser=auth.currentUser
                        //kullanıcı id aldıktan sonra altında kayıt işlemi
                        var currentUserDb=currentUser?.let { it1 -> databaseReference?.child(it1.uid) }

                        var user:Kullanici=Kullanici(mail,k_Adi,sifre)
                        currentUserDb?.setValue(user)
                        Toast.makeText(this@KayitSayfasi,"kayıt edildi",Toast.LENGTH_SHORT).show()

                    }
                    else{

                        Toast.makeText(this@KayitSayfasi,"kayıt edilemedi",Toast.LENGTH_SHORT).show()
                    }

                }


        }
        binding.button2.setOnClickListener {
            var intent=Intent(this,GirisSayfasi::class.java)
            startActivity(intent)
        }





    }

}
