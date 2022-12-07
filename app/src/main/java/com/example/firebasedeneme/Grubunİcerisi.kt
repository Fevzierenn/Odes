package com.example.firebasedeneme

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedeneme.databinding.GrubunicerisiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class Grubunİcerisi: AppCompatActivity(){
    lateinit var binding:GrubunicerisiBinding
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var databaseReferenceGrupAktiviteleri: DatabaseReference? = null
    private lateinit var uid: String
    private lateinit var mUser: FirebaseUser
    var gruptakiIdler:ArrayList<String> = arrayListOf()
    var gruptakileriKullaniciADLARİ: ArrayList<String> = arrayListOf()
    var sayac:Int=0
    var gruptakileriNborclar:ArrayList<String> = arrayListOf()
    var gruptakileriNOturumacanaOncekiBorclari:ArrayList<String> = arrayListOf()

    lateinit var aktiviteRecyclerView: RecyclerView
    lateinit var aktiviteListe:ArrayList<Aktivite>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=GrubunicerisiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        mUser = auth.currentUser!!
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Groups")
        databaseReferenceGrupAktiviteleri = FirebaseDatabase.getInstance().reference.child("Grup Aktiviteleri")

        aktiviteRecyclerView= binding.grubunEtklnlikleri
        aktiviteRecyclerView.layoutManager=LinearLayoutManager(this)
        aktiviteRecyclerView.setHasFixedSize(true)
        aktiviteListe= arrayListOf<Aktivite>()



        var gruplar:String=intent.extras?.getString("gruplar").toString()

        var gruplarList: ArrayList<String> = arrayListOf()

        if (gruplar.contains(",") == false){
            gruplarList.add(gruplar.trim().toString())
        }
        else{
            gruplarList= gruplar.split(",") as ArrayList<String>
        }

        var grubunPozisyonu:Int=intent.extras?.getInt("position")!!.toInt()

        binding.grubunSmi.setText(gruplarList.get(grubunPozisyonu).toString())

        grupEtkinlikleriRecycler(gruplarList.get(grubunPozisyonu))

        Log.i("gruplar",gruplar)
        Log.i("gruplar", grubunPozisyonu.toString())
        binding.homePage.setOnClickListener {
            homePageTiklandiginda()
        }
//--------------------------------//
        if (sayac<=0){
            var gruptakiİdes:ArrayList<String> = tiklananGrubunKullaniciİdeleri(gruplarList.get(grubunPozisyonu))
            var gruptakiKullaniciAdlari:ArrayList<String> = tiklananGrubunKullaniciİsimleri(gruplarList.get(grubunPozisyonu),gruptakiİdes)
            sayac++
            var gruptakilerinBorclari: ArrayList<String> = gruptakilerinOncekiBorclari(gruplarList.get(grubunPozisyonu),gruptakiİdes)

            Log.i("Borclar main sayac=0",gruptakilerinBorclari.toString())
        }
        else{}
        binding.giderEkle.setOnClickListener {
            if (sayac<=1){
                var gruptakiİdes:ArrayList<String> = tiklananGrubunKullaniciİdeleri(gruplarList.get(grubunPozisyonu))
                var gruptakiKullaniciAdlari:ArrayList<String> = tiklananGrubunKullaniciİsimleri(gruplarList.get(grubunPozisyonu),gruptakiİdes)
                sayac++
                var gruptakilerinBorclari: ArrayList<String> = gruptakilerinOncekiBorclari(gruplarList.get(grubunPozisyonu),gruptakiİdes)

                Log.i("Borclar main sayac=1",gruptakilerinBorclari.toString())
                GiderEklemeSayfasinaVeriYolla(gruptakiİdes,gruptakiKullaniciAdlari,grubunPozisyonu,gruplarList,gruptakilerinBorclari)
            }
            else{}
        }      //SetOnClickListener
        binding.SettleUp.setOnClickListener {
            var gruptakiİdes:ArrayList<String> = tiklananGrubunKullaniciİdeleri(gruplarList.get(grubunPozisyonu))
            var gruptakiKullaniciAdlari:ArrayList<String> = tiklananGrubunKullaniciİsimleri(gruplarList.get(grubunPozisyonu),gruptakiİdes)

            var gruptakilerinBorclari: ArrayList<String> = gruptakilerinOncekiBorclari(gruplarList.get(grubunPozisyonu),gruptakiİdes)
            var gruptakilerinOturumacanaolanborclari: ArrayList<String> = gruptakilerinOturumAcanaOncekiBorclari(gruplarList.get(grubunPozisyonu),gruptakiİdes)
            //grupElemanlarininbilgisi(gruplarList.get(grubunPozisyonu),gruptakiİdes,uid)
            BorclariSifirlama(gruplarList.get(grubunPozisyonu).trim(),gruptakiİdes)
            AktiviteleriSilme(gruplarList.get(grubunPozisyonu).trim())
            //iDlerdekiBorclariSilme(gruplarList.get(grubunPozisyonu).trim(),gruptakiİdes)
        }
        //---------------------//--------------------------------------
    }

    private fun grupEtkinlikleriRecycler(grupismi:String) {
        databaseReferenceGrupAktiviteleri?.child(grupismi.trim())?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        Log.i("snapshot recycler",i.value.toString().trim())
                        var aktivite= Aktivite(i.value.toString().trim())
                        aktiviteListe.add(aktivite)

                    }
                    aktiviteRecyclerView.adapter=AktiviteAdapter(aktiviteListe)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun AktiviteleriSilme(grupİsmi: String) {
        databaseReferenceGrupAktiviteleri?.child(grupİsmi.toString())?.removeValue()?.addOnCompleteListener(this){task->
            if (task.isSuccessful){

                Toast.makeText(this,"Aktiviteler silindi",Toast.LENGTH_SHORT).show()
            }
            else{

            }

        }
    }

    private fun BorclariSifirlama(grupİsmi:String,gruptakilerinId:ArrayList<String>,) {
        Log.i("BorclariSifirlama",grupİsmi)
        Log.i("BorclariSifirlama",gruptakilerinId.toString())
        for (id in gruptakilerinId){
            var hashMapAlinacak: HashMap<String,String> = hashMapOf()
            hashMapAlinacak.put("alinacak","0")
            var hashMapBorc: HashMap<String,String> = hashMapOf()
            hashMapBorc.put("borc","0")
            databaseReference?.child(grupİsmi.trim())?.child(id)?.updateChildren(hashMapAlinacak as Map<String,String>)?.addOnCompleteListener(){task->
                if (task.isSuccessful)
                {
                    databaseReference?.child(grupİsmi.trim())?.child(id)?.updateChildren(hashMapBorc as Map<String,String>)?.addOnCompleteListener(){task->
                        if (task.isSuccessful){}
                        else{}      //task2 sonu        //
                    }
                }
                else{}      //task1 sonu
            }
        }
    }

    private fun gruptakilerinOturumAcanaOncekiBorclari(grupİsmi: String, gruptakilerinİd: ArrayList<String>):ArrayList<String>{
        var borclar:ArrayList<String> = arrayListOf()
        databaseReference?.child(grupİsmi.trim())
        val postListener =databaseReference?.child(grupİsmi.trim())?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){               //,-> { key = 4BK7IVbwaeMBOSDNRKqkiEyGkX33, value = {borc=0.0, k_adi=Neymar92, alinacak=0.0, id=4BK7IVbwaeMBOSDNRKqkiEyGkX33} }
                        //Log.i("i",i.toString())
                        if (i.key?.trim().toString().equals(mUser.uid.toString()) ){

                        }
                        else{

                            for (m in i.children){
                                if(m.key?.trim().toString().equals(mUser.uid.trim())) {
                                    gruptakileriNOturumacanaOncekiBorclari.add(m.value.toString().trim())
                                }
                                else{
                                    //gruptakileriNOturumacanaOncekiBorclari.add("0")
                                }
                            }
                        }

                    }
                }
                else{}      //snapshot.exist()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })

        return gruptakileriNOturumacanaOncekiBorclari
    }



    private fun gruptakilerinOncekiBorclari(grupİsmi: String, gruptakilerinİd: ArrayList<String>):ArrayList<String>{
        var borclar:ArrayList<String> = arrayListOf()
        databaseReference?.child(grupİsmi.trim())
        val postListener =databaseReference?.child(grupİsmi.trim())?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){               //,-> { key = 4BK7IVbwaeMBOSDNRKqkiEyGkX33, value = {borc=0.0, k_adi=Neymar92, alinacak=0.0, id=4BK7IVbwaeMBOSDNRKqkiEyGkX33} }
                        //Log.i("i",i.toString())
                        if (i.key?.trim().toString().equals(mUser.uid.toString()) ){
                            Log.i("oturum açan kullanici", i.key.toString())
                        }
                        else{
                            Log.i("kullanicilar",i.key.toString())
                            for (m in i.children){
                                if(m.key?.trim().toString().equals("borc")){
                                    //Log.i("borc", m.value.toString())

                                    gruptakileriNborclar.add(m.value.toString().trim())
                                    //Log.i("borc",gruptakileriNborclar.toString())
                                }
                                else{}
                            }
                        }

                    }
                }
                else{}      //snapshot.exist()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })

        return gruptakileriNborclar
    }

    private fun homePageTiklandiginda() {
        var intent=Intent(this,ProgramMerhabaSayfasi::class.java)
        startActivity(intent)
        finish()
    }

    private fun GiderEklemeSayfasinaVeriYolla(veriId:ArrayList<String>,veriK_adi:ArrayList<String>,grubunPozisyonu:Int,gruplar:ArrayList<String>,gruptakilerinBorclari:ArrayList<String>) {
        //var string:String = veriId.joinToString().trim()
        //Log.i("veri",veriId.toString())
        var intent=Intent(this,GiderEkleme::class.java)
        var bundle=Bundle()
        bundle.putStringArrayList("gruptakilerinİdesi",veriId)
        bundle.putStringArrayList("gruptakilerinKullaniciAdlari",veriK_adi)
        bundle.putStringArrayList("gruplar",gruplar)
        bundle.putInt("position",grubunPozisyonu)
        bundle.putStringArrayList("gruptakilerinBorclarUserHaric",gruptakilerinBorclari)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    private fun tiklananGrubunKullaniciİsimleri(grupname:String,ideler: ArrayList<String>):ArrayList<String>{

        databaseReference?.child(grupname.trim())?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var sayac:Int=0
                    for (i in snapshot.children){       //i-> { key = 9GWTuPEHpfc3XZXTCjuU3a0tZ7p2, value = {borc=15, k_adi=bdesus, alinacak=12, id=9GWTuPEHpfc3XZXTCjuU3a0tZ7p2} }
                        if (i.key?.trim().equals(ideler.get(sayac))){
                            sayac++
                            for (m in i.children){
                                if (m.key.equals("k_adi")){
                                    gruptakileriKullaniciADLARİ.add(m.value.toString().trim())
                                    Log.i("k_adi",gruptakileriKullaniciADLARİ.toString())
                                }
                                else{}
                            }
                        }
                        else{}
                    }
                }
                else{
                    Log.i("snapshot","snapshot bulunamadi")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })          //Value event listener sonu


        return gruptakileriKullaniciADLARİ
    }


    private fun tiklananGrubunKullaniciİdeleri(grupname: String):ArrayList<String>{

        databaseReference?.child(grupname.trim())?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        //Log.i("i",i.toString())
                        gruptakiIdler.add(i.key?.trim().toString())
                        //Log.i("i",gruptakiIdler.toString())
                    }
                }
                else{
                    //Log.i("snapshot","snapshot bulunamadi")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })          //Value event listener sonu


        return gruptakiIdler
    }
}