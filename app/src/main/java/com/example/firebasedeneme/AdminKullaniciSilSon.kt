package com.example.firebasedeneme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.AdminkullanicisilsonBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminKullaniciSilSon:AppCompatActivity() {
    lateinit var binding: AdminkullanicisilsonBinding

    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var databaseReferenceUsers: DatabaseReference? = null
    private lateinit var uid: String
    private lateinit var mUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=AdminkullanicisilsonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        mUser = auth.currentUser!!
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Groups")
        databaseReferenceUsers = FirebaseDatabase.getInstance().reference.child("users")

        var username:String=intent?.extras?.getString("kullaniciAdi")?.trim().toString()
        var id:String=intent?.extras?.getString("kullaniciIde")?.trim().toString()
        var pozisyon:Int= intent?.extras?.getInt("pozisyon")!!
        Log.i("pozisyon",pozisyon.toString())
        Log.i("username",username)
        Log.i("id",id)

        kullaniciyiDatabasedenSil(id.trim(),username.trim().toString())

        adminPanelineDon()

    }

    private fun adminPanelineDon() {
        var intent= Intent(this, Admin::class.java)
        startActivity(intent)
        finish()
    }

    private fun kullaniciyiDatabasedenSil(id:String,username:String) {
        databaseReferenceUsers?.child(id.trim().toString())?.removeValue()?.addOnCompleteListener { task->
            if (task.isSuccessful){
                Toast.makeText(this,"${username} adlı kullanıcı silindi.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}