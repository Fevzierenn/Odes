package com.example.firebasedeneme

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.ChangeemailBinding
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.changeemail.*
import java.net.PasswordAuthentication

class ChangeEmail:AppCompatActivity() {
    lateinit var binding:ChangeemailBinding
    var databaseReference: DatabaseReference? = null
    var databaseReferenceUsers: DatabaseReference? = null
    private lateinit var uid: String
    private lateinit var mUser: FirebaseUser
    private lateinit var credential: EmailAuthProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ChangeemailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        mUser = auth.currentUser!!
        uid = auth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().reference.child("Groups")
        databaseReferenceUsers = FirebaseDatabase.getInstance().reference.child("users")

        binding.homebutton.setOnClickListener {
            anasayfayadon()
        }
        binding.kaydet.setOnClickListener {
            var mail1:String= binding.yenimail1.text.toString()
            var mail2:String=binding.yenimail2.text.toString()
            var simdikimail:String=binding.simdikimail.text.toString()
            var sifre:String=binding.sifre.text.toString()
            var sonuc:Boolean=mailleraynimi(mail1,mail2)
            if (TextUtils.isEmpty(mail1)){
                binding.yenimail1.error="Boş bırakılamaz"
            }
            else if(TextUtils.isEmpty(mail2)){
                binding.yenimail2.error="Boş bırakılamaz"
            }
            else if(TextUtils.isEmpty(simdikimail)){
                binding.simdikimail.error="Boş bırakılamaz"
            }
            else if(TextUtils.isEmpty(sifre)){
                binding.sifre.error="Boş bırakılamaz"
            }
            else if(mail1.contains("@")==false || mail2.contains("@")==false|| simdikimail.contains("@")==false){
                Toast.makeText(this,"Mail adresinizde @ ibaresi olmalıdır.", Toast.LENGTH_SHORT).show()
            }
            else if (sonuc==false){
                Toast.makeText(this,"Mail adresleri birbirinden farklı", Toast.LENGTH_SHORT).show()
            }
            else{
                //sifreyiYenile(sifre1)
                mailiGuncelle(simdikimail,sifre,mail1)


            }
            Log.i("Sonuc",sonuc.toString())
        }




    }

    private fun anasayfayadon() {
        var intent = Intent(this, ProgramMerhabaSayfasi::class.java)
        startActivity(intent)
        finish()
    }

    private fun mailiGuncelle(simdikimail: String, sifre: String, yenimail1: String) {
        auth.signInWithEmailAndPassword(simdikimail,sifre).addOnCompleteListener { task->
            if (task.isSuccessful){
                auth.currentUser!!.updateEmail(yenimail1).addOnCompleteListener { task->
                    if (task.isSuccessful){
                        Toast.makeText(this,"Mail adresi başarıyla güncellendi.",Toast.LENGTH_SHORT).show()
                        mailidatabasedeGuncelle(yenimail1)
                    }
                }
            }
        }
    }

    private fun mailidatabasedeGuncelle(yenimail1: String) {
        var hashmap:HashMap<String,String> = hashMapOf()
        hashmap.put("mail",yenimail1.trim().toString())
databaseReferenceUsers?.child(mUser.uid.trim().toString())?.updateChildren(hashmap as Map<String,String>)?.addOnCompleteListener { task->
    if (task.isSuccessful){

    }
}
    }

    private fun mailleraynimi(mail1: String, mail2: String): Boolean {
        var kontrol:Boolean = false
        if (mail1.equals(mail2)){
            kontrol=true
        }
        else{}
        return kontrol
    }
}