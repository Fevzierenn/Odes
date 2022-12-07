package com.example.firebasedeneme

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.ActivitySecondBinding
import com.example.firebasedeneme.databinding.GrupolusturmapenceresiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class GrupOlusturmaSayfası:AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var arkadaslarimreferans: DatabaseReference? = null
    lateinit var mUser: FirebaseUser
    //private lateinit var storageReference: StorageReference
    private lateinit var dialog: Dialog
    private lateinit var uid: String


    // Declaring the elements from the main layout file
    private lateinit var listView: ListView

    var database: FirebaseDatabase? = null
    var mUsersRef: DatabaseReference? = null

    var denemeİdes:ArrayList<String> = ArrayList<String>()
    lateinit var binding:GrupolusturmapenceresiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=GrupolusturmapenceresiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Groups")
        mUsersRef= FirebaseDatabase.getInstance().getReference().child("users")
        //denemeİdes=ArrayList<String>()
        var kullaniciisimleri:ArrayList<String> = ArrayList<String>()
        denemeİdes= ArrayList<String>()

        var kullaniciides:List<String> = arrayListOf()
        var arkadaslarınKullanıcıAdları:String=intent.extras?.getString("arkadaslarim").toString()
        var arkadaslarınİdleri:String=intent.extras?.getString("arkadaslarIdes").toString()
        kullaniciides= arkadaslarınİdleri.split(",")
        Log.i("grup oluşturma sayfası",arkadaslarınKullanıcıAdları)
        Log.i("grup oluşturma sayfası",arkadaslarınİdleri)


        kullaniciisimleri=idlerinKullaniciİsimleri(kullaniciides,kullaniciisimleri)
        var ides:String=denemeİdes.joinToString()
        var kullanici:String=kullaniciisimleri.joinToString()
        Log.i("ides",ides.toString())
        Log.i("isimler",kullanici.toString())

        ///////---//////////
        binding.grubakullaniciekle.setOnClickListener{
            //grubaKullaniciEkleme(arkadaslarınİdleri,arkadaslarınKullanıcıAdları)        //kullanıcı ekleme sayfamıza bilgileri atıyoruz
              grubaKullaniciEkleme(denemeİdes,kullaniciisimleri)

        }
        var gruptakilerinIdleriString: String? =intent.extras?.getString("gruptakilerinIdeleri")
        var gruptakilerinKullaniciAdlariString: String? =intent.extras?.getString("gruptakilerinKullaniciAdlari")
        var oturumAcaninUserNamesi: String? =intent.extras?.getString("oturumacanUsername")
        Log.i("dönüş",gruptakilerinIdleriString.toString())
        Log.i("dönüş",gruptakilerinKullaniciAdlariString.toString())
        Log.i("dönüş",oturumAcaninUserNamesi?.trim().toString())

        binding.grubuOlustur.setOnClickListener {
            var grupİsmi:String=binding.grupSmi.text.toString()
            if (TextUtils.isEmpty(grupİsmi)){
                binding.grupSmi.error="Grup adı boş bırakılamaz"
            }
            else{
                databaseGruplarıKaydetme(gruptakilerinIdleriString,grupİsmi,gruptakilerinKullaniciAdlariString,oturumAcaninUserNamesi.toString())
                var intent=Intent(this,ProgramMerhabaSayfasi::class.java)
                startActivity(intent)
            }

        }


    }



    private fun databaseGruplarıKaydetme(idler:String?,grupİsmi:String,usernames:String?,oturumUsername:String) {
        var liste:ArrayList<String> = arrayListOf()
        var usernameliste:ArrayList<String> = arrayListOf()
        if (idler?.isEmpty()==true){
            var intent=Intent(this,ProgramMerhabaSayfasi::class.java)
            Toast.makeText(this,"Üye eklemeden grup oluşturulamaz",Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        else if(idler?.contains(",")==false){
            liste.add(idler.trim())
            liste.add(uid.trim())
            usernameliste.add(usernames?.trim().toString())         //oturum açan kullanıcının usernameni al
            usernameliste.add(oturumUsername)
        }

        else{
            liste=idler?.split(",") as ArrayList<String>
            liste.add(uid.trim())
            usernameliste=usernames?.split(",") as ArrayList<String>
            usernameliste.add(oturumUsername.trim().toString())
        }


            Log.i("fonk içi",usernameliste.toString())                  //sorun olursa trim() yap
        for(i in liste)
        {
            var hashmap:HashMap<String,String> = hashMapOf()
            var hashmapBorc:HashMap<String,Int> = hashMapOf()
            var hashmapAlınacak:HashMap<String,Int> = hashMapOf()
            hashmap.put("${usernameliste.get(liste.indexOf(i))}",i)
            hashmapBorc.put("Borc",0)
            hashmapAlınacak.put("Alinacak",0)
            var referans1=databaseReference?.child(grupİsmi.toString())?.child(i.trim().toString())
            var grup1=Grup(usernameliste.get(liste.indexOf(i)).trim(),"0.0","0.0",i.trim().toString())
            referans1?.setValue(grup1)
        }

    }



    fun idlerinKullaniciİsimleri(arkIdleri: List<String>, kullaniciadlari:ArrayList<String>):ArrayList<String>{

        var referans=mUsersRef
        //Log.i("idilerin K_adi isimleri","başlangiç")
        val listener=referans?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (m in snapshot.children){
                        for (i in arkIdleri){
                            //Log.i("m?",m.key.toString())
                            //Log.i("m?",i)
                            if (i.trim().equals(m.key?.trim().toString())){
                                //Log.i("esit",m.child("k_adi").value.toString())
                                if(kullaniciadlari.contains(m.child("k_adi").value.toString())){
                                    denemeİdes.add(i.trim())               //kullanici adi listesiyle denk ideler oluşturuldu
                                    //Log.i("denemeİdes",denemeİdes.toString())

                                }
                                else{
                                    kullaniciadlari.add(m.child("k_adi").value.toString())

                                    //Log.i("arkadaslar",kullaniciadlari.toString())
                                }


                            }
                            else{
                                //Log.i("arkadas degil",m.child("k_adi").value.toString())

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

    private fun grubaKullaniciEkleme(arkadaslarınİdleri: ArrayList<String>, arkadaslarınKullanıcıAdları: ArrayList<String>) {
        var id:String=arkadaslarınİdleri.joinToString()
        var kadi:String=arkadaslarınKullanıcıAdları.joinToString()
        //Log.i("bilgileri yollama",id.toString())
        //Log.i("bilgileri yollama",kadi.toString())

        var intent= Intent(this,GrubaKullanıcıEklemeSayfası::class.java)
        var bundle=Bundle()
        bundle.putString("idler",id)
        bundle.putString("kullaniciadlari",kadi)
        intent.putExtras(bundle)
        startActivity(intent)


    }
}