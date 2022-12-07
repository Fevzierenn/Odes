package com.example.firebasedeneme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.AdmingrupsilmesonBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminGelenGrubuSilmefinal:AppCompatActivity() {
    lateinit var binding:AdmingrupsilmesonBinding
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var databaseReferenceAktivite: DatabaseReference? = null
    private lateinit var uid: String
    private lateinit var mUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=AdmingrupsilmesonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        mUser = auth.currentUser!!
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Groups")
        databaseReferenceAktivite = FirebaseDatabase.getInstance().reference.child("Grup Aktiviteleri")

        var silinecekgrupismi: String? =intent?.extras?.getString("grupismi")
        var pozisyon:Int?=intent?.extras?.getInt("pozisyon")
        Log.i("silme sinif",silinecekgrupismi!!)
        Log.i("silme sinif", pozisyon.toString())

        databasedenGrubuSil(silinecekgrupismi.trim().toString(),pozisyon)

        anasayfayagit()
    }

    private fun anasayfayagit() {
        var intent= Intent(this,Admin::class.java)
        startActivity(intent)
        finish()
    }

    private fun databasedenGrubuSil(silinecekgrupismi: String, pozisyon: Int?) {
            databaseReference?.child(silinecekgrupismi.trim())?.removeValue()?.addOnCompleteListener { task->
                if (task.isSuccessful){
                    Toast.makeText(this,"${silinecekgrupismi} adlı grup başarıyla silindi.",Toast.LENGTH_SHORT).show()
                    databaseReferenceAktivite?.child(silinecekgrupismi.trim())?.removeValue()?.addOnCompleteListener { task->
                        if (task.isSuccessful){
                            Toast.makeText(this,"${silinecekgrupismi} aktiviteleri başarıyla silindi.",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this,"${silinecekgrupismi} grubuna ait aktivite bulunamadı.",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    Toast.makeText(this,"${silinecekgrupismi} adlı grup silinmedi",Toast.LENGTH_SHORT).show()
                }
            }
    }
}