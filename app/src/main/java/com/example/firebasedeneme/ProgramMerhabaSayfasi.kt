package com.example.firebasedeneme

import User
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.ActivityMainBinding
import com.example.firebasedeneme.databinding.ActivitySecondBinding
import com.example.firebasedeneme.databinding.ProgramkarsilamaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.okhttp.internal.Util.contains


open class ProgramMerhabaSayfasi:AppCompatActivity() {
    private lateinit var binding: ProgramkarsilamaBinding
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var arkadaslarimreferans: DatabaseReference? = null

    lateinit var mUser: FirebaseUser

    //private lateinit var storageReference: StorageReference
    private lateinit var dialog: Dialog
    private lateinit var user: User
    private lateinit var uid: String
    var database: FirebaseDatabase? = null
    var mUsersRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ProgramkarsilamaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")
        arkadaslarimreferans = FirebaseDatabase.getInstance().reference.child("Friends")
        mUsersRef= FirebaseDatabase.getInstance().getReference().child("users")
        //val kullaniciIsımleri:MutableList<String> = mutableListOf()
        var kullanici: ArrayList<String> = ArrayList<String>()
        var kullaniciIdleri: ArrayList<String> = ArrayList<String>()
        //kullanici.add("denemeler")
        var arkadaslarinidleri: ArrayList<String> = ArrayList<String>()
        var Arkadasfinalidler: ArrayList<String> = ArrayList<String>()
        var arkadaskullaniciadlari: ArrayList<String> = ArrayList<String>()
        var arkadaskullaniciadlarigrup: ArrayList<String> = ArrayList<String>()


        Arkadasfinalidler=arkadaslarinidleri(arkadaslarinidleri)
        //Log.i("arkadas idler",Arkadasfinalidler.toString())
        kullanici = getUserInfos(kullanici)
        kullaniciIdleri = getUserIdes(kullaniciIdleri)

        arkadaskullaniciadlari=idlerinKullaniciİsimleri(Arkadasfinalidler,arkadaskullaniciadlari)           //arkadasların kullanıcı adları
        arkadaskullaniciadlarigrup=idlerinKullaniciİsimleri(Arkadasfinalidler,arkadaskullaniciadlarigrup)   //arkadasların kullanıcı adlarını gruba taşıma

        binding.grupolustur.setOnClickListener{

            gruparkadaslarimsayfasinagitme(arkadaskullaniciadlarigrup,Arkadasfinalidler)
        }
        binding.buton6.setOnClickListener {

            var idler:String=idleristringyapma(kullaniciIdleri)
            //Log.i("idler.",idler)
            butontıklama(kullanici,idler)
            arkadaskullaniciadlari=idlerinKullaniciİsimleri(Arkadasfinalidler,arkadaskullaniciadlari)
            arkadaskullaniciadlarigrup=idlerinKullaniciİsimleri(Arkadasfinalidler,arkadaskullaniciadlarigrup)
         }

    binding.button4.setOnClickListener {
        var intent=Intent(this, Dovizsayfasibir::class.java)
        startActivity(intent)
    }

        binding.arkadaslarimigoruntulemebutonu.setOnClickListener{
            arkadaskullaniciadlari=idlerinKullaniciİsimleri(Arkadasfinalidler,arkadaskullaniciadlari)
            arkadaslarimSayfasi(arkadaskullaniciadlari)
            arkadaskullaniciadlarigrup=idlerinKullaniciİsimleri(Arkadasfinalidler,arkadaskullaniciadlarigrup)
            return@setOnClickListener
        }

        Log.i("class içi",Arkadasfinalidler.toString())

        binding.button2.setOnClickListener {
            var intent=Intent(this,GroupListActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.ayarlar.setOnClickListener {
            var intent=Intent(this, AyarlarActivity::class.java)
            startActivity(intent)
        }



    }
    fun idlerinKullaniciİsimleri(arkIdleri: List<String>, kullaniciadlari:ArrayList<String>):ArrayList<String>{

        var referans=mUsersRef
        //Log.i("idilerin K_adi isimleri","başlangiç")
        val listener=referans?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (m in snapshot.children){
                        for (i in arkIdleri){
                            //Log.i("m?",m.key.toString())
                            //Log.i("m?",i)
                            if (i.trim().equals(m.key?.trim().toString())){
                                //Log.i("esit",m.child("k_adi").value.toString())
                                if(kullaniciadlari.contains(m.child("k_adi").value.toString())){

                                }
                                else{
                                    kullaniciadlari.add(m.child("k_adi").value.toString())
                                    //Log.i("arkadaslar",kullaniciadlari.toString())
                                }


                            }
                            else{
                                //Log.i("arkadas degil",m.child("k_adi").value.toString())
                                //Log.i("arkadaslar",kullaniciadlari.toString())
                            }
                        }
                    }
                }
                else{

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        referans?.addListenerForSingleValueEvent(listener!!)
        return kullaniciadlari
    }

    private fun gruparkadaslarimsayfasinagitme(arkadaslarlistesi: ArrayList<String>,arkIdleri: List<String>) {
        var idler=arkadaslarlistesi.joinToString()
        var ides=arkIdleri.joinToString()
        //Log.i("tek string:",idler)
        var intent=Intent(this,GrupOlusturmaSayfası::class.java)
        var bundle=Bundle()

        bundle.putString("arkadaslarim",idler)
        bundle.putString("arkadaslarIdes",ides)
        intent.putExtras(bundle)

        startActivity(intent)


    }



    private fun arkadaslarimSayfasi(arkadaslarlistesi: ArrayList<String>) {
        var idler=arkadaslarlistesi.joinToString()
        //Log.i("tek string:",idler)
        var intent=Intent(this,Arkadaslarim::class.java)
        var bundle=Bundle()

        bundle.putString("arkadaslarim",idler)
        intent.putExtras(bundle)

        startActivity(intent)


    }

    private fun arkadaslarinidleri(liste:ArrayList<String>):ArrayList<String> {
       var referansArk=arkadaslarimreferans?.child(uid)
        val listener=referansArk?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children) {
                        if (liste.contains(i.key.toString()) == false) {
                            liste.add(i.key.toString())
                            //Log.i("snapshot:", i.key.toString())
                            //Log.i("arkadaşListesi:", liste.toString())
                        }
                        else{

                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        //Log.i("return liste öncesi:", liste.toString())
        return liste
    }

    @SuppressLint("LongLogTag")
    private fun getUserIdes(kullaniciIdleri: ArrayList<String>): ArrayList<String> {
        val postListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var sb = StringBuilder()
                var sk = StringBuilder()
                for (i in snapshot.children) {


                    kullaniciIdleri.add(i.key.toString())
                    //Log.i("kullaniciIdleri", "Got value ${kullaniciIdleri.toString()}")
                    //Log.i("firebase", "Got value ${kullaniciIdleri}")

                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        //Log.i("kullaniciId post once", "Got value ${kullaniciIdleri.toString()}")
        databaseReference?.addValueEventListener(postListener)
        //Log.i("kullaniciIdleri return once", "Got value ${kullaniciIdleri.toString()}")
        return kullaniciIdleri
    }


    fun butontıklama(kullanici: ArrayList<String>,idler:String) {

        if (uid.isNotEmpty()) {
            var intent = Intent(this, Aramabutonuici::class.java)

            //getUserInfos(kullanici)           //databasedeki elemanları listeye ekledik.
            val st: String =
                kullanici.joinToString()      //listedeki elemanları tekbir stringe atandı.



            val bundle = Bundle()
            bundle.putString("kullanicilarlistesi", st)  //gönderildi.

            bundle.putString("kullaniciid", uid)
            bundle.putString("kullaniciidleri",idler)
            intent.putExtras(bundle)
            startActivity(intent)

        } else {
            Toast.makeText(this, "Bir hata oluştu", Toast.LENGTH_SHORT).show()
        }

    }

    var idler:String=""
    fun idleristringyapma(kullaniciIdler: ArrayList<String>):String {

        if (uid.isNotEmpty()) {
            var intent = Intent(this, Aramabutonuici::class.java)

            //getUserInfos(kullanici)           //databasedeki elemanları listeye ekledik.
            idler =
                kullaniciIdler.joinToString()      //listedeki elemanları tekbir stringe atandı.





        } else {
            Toast.makeText(this, "Bir hata oluştu", Toast.LENGTH_SHORT).show()
        }
        return idler
    }



    fun getUserInfos(liste: ArrayList<String>): ArrayList<String> {
        val postListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var sb = StringBuilder()
                var sk = StringBuilder()
                for (i in snapshot.children) {

                    var kullaniciadi = i.child("k_adi").getValue().toString()
                    var datamail = i.child("mail").getValue()
                    var sifre = i.child("sifre").getValue()
                    liste.add(kullaniciadi)
                    //Log.i("firebase", "Got value ${kullaniciadi}")
                    //Log.i("firebase", "Got value ${datamail}")
                    //Log.i("firebase", "Got value ${sifre}")

                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                //Log.i("firebase", "bilgi alınmadı")
            }
        }
        databaseReference?.addValueEventListener(postListener)


        return liste


    }



}
