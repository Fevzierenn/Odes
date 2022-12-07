package com.example.firebasedeneme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.AdminanasayfaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

lateinit var auth: FirebaseAuth
var databaseReference: DatabaseReference? = null
var databaseReferenceUsers: DatabaseReference? = null
private lateinit var uid: String
private lateinit var mUser: FirebaseUser
var gruplar:ArrayList<String> = arrayListOf()
var kullaniciisimleri:ArrayList<String> = arrayListOf()
var kullaniciideleri:ArrayList<String> = arrayListOf()
var sayac:Int=0
var sayac2:Int=0
class Admin:AppCompatActivity(){
    lateinit var binding:AdminanasayfaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=AdminanasayfaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        mUser = auth.currentUser!!
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Groups")
        databaseReferenceUsers = FirebaseDatabase.getInstance().reference.child("users")

            if(sayac==0){
                gruplarlistesi()
                Log.i("başlangıç sayac", sayac.toString())
                //kullaniciadilistesi()
                //kullaniciidelerilistesi()
                sayac++
                Log.i("çıkış sayac", sayac.toString())
            }


        binding.button2.setOnClickListener {
                if (sayac==1){
                    //gruplarlistesi()
                    Log.i("btn sayac", sayac.toString())
                    sayac++
                }
            else if(sayac>1){
                    Log.i(" sayac 1den buyuk", sayac.toString())
            }
                 Log.i("gruplar",gruplar.toString())
                grupSilSayfasi()

        }
        if (sayac2==0){
            kullaniciidelerilistesi()
            kullaniciadilistesi()
            sayac2++
        }

        binding.arkadaslarimigoruntulemebutonu.setOnClickListener {
            Toast.makeText(this,"Kullanıcı sil",Toast.LENGTH_SHORT).show()
                    if (sayac2==1){
                        sayac2++
                    }
                    //kullaniciadilistesi()
                    //kullaniciidelerilistesi()
                    //Log.i("k_adlari soclist", kullaniciisimleri.toString())
                    //Log.i("ideler", kullaniciideleri.toString())



            KullaniciSilSayfasi()
        }
        binding.buton6.setOnClickListener {
            kayitsayfasinagit()
        }
    }

    private fun kayitsayfasinagit() {
        var intent=Intent(this,KayitSayfasi::class.java)
        startActivity(intent)
        finish()
    }

    private fun KullaniciSilSayfasi() {
        var intent=Intent(this, AdminKullaniciSil::class.java)
        var bundle=Bundle()
        Log.i("sayfada k_adlari", kullaniciisimleri.toString())
        Log.i("sayfada k_ideler", kullaniciideleri.toString())
        bundle.putStringArrayList("kullaniciisimleri", kullaniciisimleri)
        bundle.putStringArrayList("kullaniciideleri", kullaniciideleri)
        kullaniciisimleri= arrayListOf()
        kullaniciideleri= arrayListOf()
        intent.putExtras(bundle)
        startActivity(intent)

    }

    private fun kullaniciidelerilistesi() {
        databaseReferenceUsers?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        kullaniciideleri.add(i.key?.trim().toString())
                        Log.i("kullanici ideleri", kullaniciideleri.toString())
                    }
                }
                else{}
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun kullaniciadilistesi() {
        databaseReferenceUsers?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){           //i->{ key = 4BK7IVbwaeMBOSDNRKqkiEyGkX33, value = {k_adi=Neymar92, mail=neymar@gmail.com, odenecekborcpara=0, sifre=selamlar, alinacakpara=0} }
                        //Log.i("i",i.toString())
                        for (m in i.children){             //m->{ key = k_adi, value = Neymar92 }

                            if (m.key?.trim().toString().equals("k_adi")){
                                kullaniciisimleri.add(m.value.toString().trim())
                                Log.i("kullanici isimleri", kullaniciisimleri.toString())
                            }
                        }
                    }
                }
                else{}
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }



    private fun gruplarlistesi() {
        databaseReference?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    //Log.i("snapshot admin",snapshot.toString())
                    for (i in snapshot.children){
                        //Log.i("snapshot admin",i.toString())
                        gruplar.add(i.key?.trim().toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun grupSilSayfasi() {
        var intent=Intent(this, AdminGrupSil::class.java)
        var bundle=Bundle()
        //Log.i("tiklama", gruplar.toString())
        bundle.putStringArrayList("gruplar",gruplar)
        gruplar= arrayListOf()
        intent.putExtras(bundle)
        startActivity(intent)


    }

}