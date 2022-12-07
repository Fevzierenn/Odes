package com.example.firebasedeneme

import android.R
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.example.firebasedeneme.databinding.GrubakullanicieklemeactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GrubaKullanıcıEklemeSayfası : AppCompatActivity() {
    lateinit var binding: GrubakullanicieklemeactivityBinding
    var databaseReference: DatabaseReference? = null
    var mUsersRef: DatabaseReference? = null
    lateinit var auth: FirebaseAuth
    private lateinit var uid: String

    // Declaring the DataModel Array


    // Declaring the elements from the main layout file

    var konum: Int = 0


    var arkadaslariminIdLeri: ArrayList<String> = ArrayList<String>()
    var usernames: ArrayList<String> = ArrayList<String>()

    var oturumcununKullaniciAdi:String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = GrubakullanicieklemeactivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initializing the elements from the main layout file


        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")
        var grubaeklenenler: ArrayList<String> = ArrayList<String>()
        var grubaeklenenUsernameler: ArrayList<String> = ArrayList<String>()
        var arkadaslariminKullaniciAdlari: ArrayList<String> = ArrayList<String>()
        arkadaslariminIdLeri = ArrayList<String>()
        usernames = ArrayList<String>()
        var arkadaslarinIdleri: String = intent.extras?.getString("idler").toString()
        var arkadaslarinKullaniciAdlari: String =
            intent.extras?.getString("kullaniciadlari").toString()
        if (arkadaslarinKullaniciAdlari.contains(",")==false){
            arkadaslariminKullaniciAdlari.add(arkadaslarinKullaniciAdlari)
        }
        else{
            arkadaslariminKullaniciAdlari = arkadaslarinKullaniciAdlari.split(",") as ArrayList<String>     //fixlenmesi lazım
        }

        if (arkadaslarinIdleri.contains(",")==false){
            arkadaslariminIdLeri.add(arkadaslarinIdleri)
        }
        else{
            arkadaslariminIdLeri = arkadaslarinIdleri.split(",") as ArrayList<String>
        }
        if (arkadaslarinKullaniciAdlari.contains(",")==false){
            usernames.add(arkadaslarinKullaniciAdlari)
        }
        else{
            usernames = arkadaslarinKullaniciAdlari.split(",") as ArrayList<String>
        }






        //Log.i("kullanici sayfasi idler", arkadaslariminIdLeri.toString())
        //Log.i("kullanici sayfasi adlar", arkadaslariminKullaniciAdlari.toString())
        arkadaslar(arkadaslariminKullaniciAdlari)

        binding.userList.setOnItemClickListener{_,_,position,_->
            grubaeklenenler=tiklandigindaIdekle(position,arkadaslariminKullaniciAdlari,grubaeklenenler)
            grubaeklenenUsernameler=tiklandigindaUsernameEkle(position,arkadaslariminKullaniciAdlari,grubaeklenenUsernameler)

            //kullaniciadinatiklama(position)
        }
        OturumAcaninKullaniciAdiniAl()

        //Log.i("Oturum acan kullai",oturumcununKullaniciAdi)
        binding.tamamla.setOnClickListener{

            if(grubaeklenenler.size<1){
                binding.tamamla.error="kullanıcı eklemediniz"
            }else{
                grubaeklenenIdleriGonder(grubaeklenenler,grubaeklenenUsernameler,oturumcununKullaniciAdi)
                //grubaeklenenUsernameleriGonder(grubaeklenenUsernameler,arkadaslariminKullaniciAdlari)
            }

        }


    }//Oncreate bitişi

    private fun grubaeklenenUsernameleriGonder(grubaeklenenUsernameler: ArrayList<String>, arkadaslariminKullaniciAdlari: ArrayList<String>) {

    }
    private fun OturumAcaninKullaniciAdiniAl(){

        databaseReference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (i in snapshot.children){       // { key = 4BK7IVbwaeMBOSDNRKqkiEyGkX33, value = {k_adi=Neymar92, mail=neymar@gmail.com, odenecekborcpara=0, sifre=selamlar, alinacakpara=0} }

                        if(auth.uid?.trim().equals(i.key?.trim().toString())){

                            for (m in i.children){

                                if (m.key?.trim().equals("k_adi")){
                                    oturumcununKullaniciAdi= m.value.toString().trim()

                                    Log.i("oturum acan k_adi",oturumcununKullaniciAdi)
                                }
                                else{

                                }


                            }

                        }
                        else{

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

    }

    private fun grubaeklenenIdleriGonder(liste:ArrayList<String>,kullaniciadlari: ArrayList<String>,kullaniciUsername:String) {
        var stringIdler=liste.joinToString().trim()
        var stringusernamesler=kullaniciadlari.joinToString().trim()
        //Log.i("datayı yollama",liste.toString())
        //Log.i("datayı yollama",stringIdler)
        //Log.i("datayı yollama k_adlari",stringusernamesler)
        Log.i("yıldım",kullaniciUsername)
        var intent=Intent(this,GrupOlusturmaSayfası::class.java)
        var bundle=Bundle()
        bundle.putString("gruptakilerinIdeleri",stringIdler)
        bundle.putString("gruptakilerinKullaniciAdlari",stringusernamesler)
        bundle.putString("oturumacanUsername",kullaniciUsername)
        intent.putExtras(bundle)
        startActivity(intent)


    }
    fun tiklandigindaUsernameEkle(konum: Int,kullaniciadlari:ArrayList<String>,grubaeklenenler:ArrayList<String>):ArrayList<String>{
        //Log.i("deneme",binding.userList[konum].toString())

        //binding.userList[konum].setBackgroundColor(ContextCompat.getColor(this, R.color.holo_blue_bright))
        //Log.i("deneme",binding.userList[konum].setBackgroundColor(ContextCompat.getColor(this, R.color.holo_orange_light)).toString())
        var kontrol:Boolean=gruptavarmiydi(grubaeklenenler,usernames.get(konum).trim())
        if(kontrol==true){
            grubaeklenenler.add(usernames.get(konum).trim())
            //Log.i("gruba eklenenler sonra",grubaeklenenler.toString())
            Toast.makeText(this,kullaniciadlari.get(konum)+" gruba eklendi",Toast.LENGTH_SHORT).show()
        }
        else{
            //Log.i("eklenenler kalkınca",usernames.get(konum))
            grubaeklenenler.remove(usernames.get(konum).trim())
            //binding.userList[konum].setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            Toast.makeText(this,kullaniciadlari.get(konum)+" gruptan çıkarıldı",Toast.LENGTH_SHORT).show()

        }
        return grubaeklenenler
    }


    fun tiklandigindaIdekle(konum: Int,kullaniciadlari:ArrayList<String>,grubaeklenenler:ArrayList<String>):ArrayList<String>{
        //Log.i("deneme",binding.userList[konum].toString())

        binding.userList[konum].setBackgroundColor(ContextCompat.getColor(this, R.color.holo_blue_bright))
        //Log.i("deneme",binding.userList[konum].setBackgroundColor(ContextCompat.getColor(this, R.color.holo_orange_light)).toString())
        var kontrol:Boolean=gruptavarmiydi(grubaeklenenler,arkadaslariminIdLeri.get(konum).trim())
        if(kontrol==true){
            grubaeklenenler.add(arkadaslariminIdLeri.get(konum).trim())
            //Log.i("gruba eklenenler sonra",grubaeklenenler.toString())
            Toast.makeText(this,kullaniciadlari.get(konum)+" gruba eklendi",Toast.LENGTH_SHORT).show()
        }
        else{
            //Log.i("eklenenler kalkınca",arkadaslariminIdLeri.get(konum))
            grubaeklenenler.remove(arkadaslariminIdLeri.get(konum).trim())
            binding.userList[konum].setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            Toast.makeText(this,kullaniciadlari.get(konum)+" gruptan çıkarıldı",Toast.LENGTH_SHORT).show()

        }
        return grubaeklenenler
    }
    fun gruptavarmiydi(liste:ArrayList<String>,id:String):Boolean{
        if(liste.contains(id)){
            return false
        }
        return true
    }

    private fun kullaniciadinatiklama(konum:Int) {
        var intent= Intent(this,GrubaEkleme::class.java)
        var bundle=Bundle()
        bundle.putString("kullaniciId",arkadaslariminIdLeri.get(konum))

        //Log.i("tıklama",arkadaslariminIdLeri.get(konum))
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun arkadaslar(kAdlari: List<String>) {
        //Log.i("arkadaşlarım::",kAdlari.toString())
        val itemsAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(applicationContext, R.layout.simple_list_item_1, kAdlari)
        binding.userList.adapter = itemsAdapter         //listede arkadaşlarımız gözüktü.


    }

}