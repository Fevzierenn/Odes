package com.example.firebasedeneme

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.GidereklemeactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.Random

class GiderEkleme: AppCompatActivity(){
    lateinit var binding: GidereklemeactivityBinding
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var databaseReferenceUser: DatabaseReference? = null
    var databaseReferenceGrupAktiviteleri: DatabaseReference? = null
    private lateinit var uid: String
    private lateinit var mUser: FirebaseUser
    var gruptakiIdler:ArrayList<String> = arrayListOf()
    var oturumcununKullaniciAdi:String=""
    var gruptaEnsonkalanSayi:Int=0
    var oturumacaninDatabasedekiALACAGİ:Double=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= GidereklemeactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        mUser = auth.currentUser!!
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Groups")
        databaseReferenceUser = FirebaseDatabase.getInstance().reference.child("users")
        databaseReferenceGrupAktiviteleri = FirebaseDatabase.getInstance().reference.child("Grup Aktiviteleri")


        var gruptakilerinİd: ArrayList<String> = intent.extras?.getStringArrayList("gruptakilerinİdesi") as ArrayList<String>
        Log.i("Gider ekleme sayfasi",gruptakilerinİd.toString())
        var gruptakileriK_adi:ArrayList<String> = intent.extras?.getStringArrayList("gruptakilerinKullaniciAdlari") as ArrayList<String>
        Log.i("Gider ekleme syf. k_adi",gruptakileriK_adi.toString())
        var gruplar:ArrayList<String> = intent?.extras?.getStringArrayList("gruplar") as ArrayList<String>
        Log.i("gruplar",gruplar.toString())
        var gruptakilerinBorclarioturumacanharic:ArrayList<String> = intent?.extras?.getStringArrayList("gruptakilerinBorclarUserHaric") as ArrayList<String>
        Log.i("gider ek.S BORCLAR",gruptakilerinBorclarioturumacanharic.toString())
        var position:Int?=intent.extras?.getInt("position")
        Log.i("grubun pozisyonu",position.toString())

        OturumAcaninKullaniciAdiniAl()
        OturumAcaninAlacaklariniDatabasedanAL(gruplar.get(position!!))
        //aktivitelerdeensonkalansayi(gruplar.get(position!!))
        //aktivitelerdeensonkalansayi(gruplar.get(position!!))
        binding.HesabiBicimleme.setOnClickListener {

            var aciklama: String = binding.tvHesapAciklama.text.toString().trim()
            var tutar: String = binding.Tutar.text.toString().trim()              //aslında double

            var herbirkullanicinBorcu: Double =
                gruptakiKullanicilarinAdamBasiBorcu(tutar.toDouble(), gruptakilerinİd.size.toInt())
            var oturumacaninAlacagi: Double = oturumacaninAlacagi(
                tutar.toDouble(),
                herbirkullanicinBorcu,
                gruptakilerinİd.size.toInt()
            )
            Log.i("alinacak soclstner", oturumacaninDatabasedekiALACAGİ.toString())
            Log.i("herbirborc soclstner", herbirkullanicinBorcu.toString())

            OturumAcaninAlacaginiDatabaseyeEkleme(
                gruplar.get(position!!),
                oturumacaninAlacagi,
                gruptakilerinBorclarioturumacanharic,oturumacaninDatabasedekiALACAGİ
            )
            tutariDatabaseyeEkleme(
                gruplar.get(position!!),
                gruptakilerinİd,
                gruptakilerinBorclarioturumacanharic,
                herbirkullanicinBorcu,
                oturumacaninAlacagi,
                gruptakileriK_adi,
                oturumcununKullaniciAdi
            )
            //aktivitelerdeensonkalansayi(gruplar.get(position!!))

            GrupAktiviteleri(
                gruplar.get(position!!),
                gruptakilerinİd,
                gruptakilerinBorclarioturumacanharic,
                herbirkullanicinBorcu,
                oturumacaninAlacagi,
                gruptakileriK_adi,
                oturumcununKullaniciAdi
            )

            //GrupAktiviteleri(gruplar.get(position!!),gruptakilerinİd,gruptakilerinBorclarioturumacanharic,herbirkullanicinBorcu,oturumacaninAlacagi,gruptakileriK_adi,oturumcununKullaniciAdi,gruptaEnsonkalanSayi)

        }
        binding.homepage.setOnClickListener {
            anaMenuyeDon()
        }

    }       //Oncreate bitişi

    private fun OturumAcaninAlacaklariniDatabasedanAL(grupİsmi:String) {
        var sayi: String="0"
        databaseReference?.child(grupİsmi.trim())?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    Log.i("snapshot","xist")
                    for (i in snapshot.children){
                        if (i.key?.trim().equals(mUser.uid.trim())){

                            for (n in i.children){
                                if (n.key?.trim().equals("alinacak")){
                                    Log.i("alinacaklar",n.value.toString())
                                    sayi=n.value.toString()
                                    oturumacaninDatabasedekiALACAGİ=sayi.toDouble()
                                    Log.i("alinacaklar",oturumacaninDatabasedekiALACAGİ.toString())
                                }

                            }
                        }
                    }
                }
                else{
                    Log.i("snapshot","not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun anaMenuyeDon() {
        var intent= Intent(this,ProgramMerhabaSayfasi::class.java)
        startActivity(intent)
    }

    private fun aktivitelerdeensonkalansayi(grupismi:String) {
        Log.i("fonk aktivite sayi"," ".toString())
        Log.i("gruptaki son sayi",gruptaEnsonkalanSayi.toString())
        var sayilar:ArrayList<String> = arrayListOf()
        var referans=databaseReferenceGrupAktiviteleri?.child(grupismi)
       referans?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                {
                    Log.i("snapshot exist"," ".toString())
                    for (i in snapshot.children)       // { key = 471, value = Neymar92 kullanicisi fevzierenn'a 4.0 borçlandı. }
                    {
                        sayilar.add(i.key.toString().trim())
                        Log.i("sayilar",sayilar.toString())
                        gruptaEnsonkalanSayi=sayilar.get(sayilar.size-1).toInt()
                        Log.i("gruptaki son sayi",gruptaEnsonkalanSayi.toString())
                    }
                }
                else{}
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun OturumAcaninKullaniciAdiniAl(){

        databaseReferenceUser?.addValueEventListener(object : ValueEventListener{
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


    private fun OturumAcaninAlacaginiDatabaseyeEkleme(grupismi:String,alinacakMiktar:Double,OncekiBorclar:ArrayList<String>,oncekiAlacagi:Double){
        var oncekiAlinacaklar:Double=oncekiAlacagi
        Log.i("oturum acan onc alck",oncekiAlacagi.toString())

        var hashmap: HashMap<String,String> = hashMapOf()
        hashmap.put("alinacak",(alinacakMiktar+oncekiAlinacaklar).toString())
        databaseReference?.child(grupismi.trim())?.child(mUser.uid.toString().trim())?.updateChildren(
            hashmap as HashMap<String,String> as Map<String, Any>)?.addOnCompleteListener(this){task->
            if (task.isSuccessful){}
            else{}
        }
    }
    private fun oturumacaninAlacagi(hesapTutar: Double,kisibasitutar:Double, gruptakiKisiSayisi:Int): Double {
        var alinacak:Double= kisibasitutar * (gruptakiKisiSayisi-1)
        Log.i("alinacak",alinacak.toString())
        return alinacak
    }
    private fun gruptakiKullanicilarinAdamBasiBorcu(hesapTutar: Double, gruptakiKisiSayisi:Int): Double {
        var borc:Double= (hesapTutar/gruptakiKisiSayisi)
        Log.i("borc",borc.toString())
        return borc
    }
    private fun GrupAktiviteleri(grupName:String,gruptakilerinIdeleri:ArrayList<String>,gruptakilerinOmcekiBorcu:ArrayList<String>,eklenecekBorc:Double,oturumAcaninalacagi:Double,KullaniciAdlari: ArrayList<String>,oturumacanKadi:String) {
        var ides:ArrayList<String> = gruptakilerinIdeleri
        var k_adlari:ArrayList<String> = KullaniciAdlari
        Log.i("k_adlari remove once",k_adlari.toString())
        k_adlari.remove(oturumacanKadi.trim())

        Log.i("oturum acan k?adi",oturumacanKadi)
        Log.i("k_adlari remove sonra",k_adlari.toString())
        ides.remove(mUser.uid.trim())
        var sayac:Int = 0
        Log.i("fonk ici kullanici",KullaniciAdlari.toString())

        for(id in ides){

            val randomNumber: Int = Random().nextInt(10000000)
            var hashmapBorcKime: HashMap<String,String> = hashMapOf()

            hashmapBorcKime.put((randomNumber).toString(),"'${k_adlari.get(sayac)}' kullanıcısı '${oturumacanKadi}' kullanıcısına ${eklenecekBorc} TL borçlandı.")

            var referans=databaseReferenceGrupAktiviteleri?.child(grupName.trim())
            referans?.updateChildren(hashmapBorcKime as Map<String,String>)
            //var aktivite = GrupİcerisindekiAktiviteleriDBYazma(hashmapBorcKime)
            //referans?.setValue(aktivite)
            sayac++
            }
    }

    private fun tutariDatabaseyeEkleme(grupName:String,gruptakilerinIdeleri:ArrayList<String>,gruptakilerinOmcekiBorcu:ArrayList<String>,eklenecekBorc:Double,oturumAcaninalacagi:Double,KullaniciAdlari: ArrayList<String>,oturumacanKadi:String) {
        var ides:ArrayList<String> = gruptakilerinIdeleri
        var k_adlari:ArrayList<String> = KullaniciAdlari
        Log.i("k_adlari remove once",k_adlari.toString())
        k_adlari.remove(oturumacanKadi.trim())

        Log.i("oturum acan k?adi",oturumacanKadi)
        Log.i("k_adlari remove sonra",k_adlari.toString())
        ides.remove(mUser.uid.trim())
        var sayac:Int = 0
        Log.i("fonk ici kullanici",KullaniciAdlari.toString())
        for(id in ides){

            var hashmap: HashMap<String,String> = hashMapOf()

            hashmap.put("borc",(gruptakilerinOmcekiBorcu.get(sayac).toDouble()+eklenecekBorc).toString())

            sayac++
            databaseReference?.child(grupName.trim())?.child(id.trim())?.updateChildren(hashmap as Map<String, String>)?.addOnCompleteListener(){task->
                if (task.isSuccessful){

                    Log.i("task",hashmap.toString())
                }
                else{}
            }

        }
    }
}           //Class bitişi
