package com.example.firebasedeneme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedeneme.databinding.AdminkullanicisilactivityBinding

class AdminKullaniciSil : AppCompatActivity(), AdminCustomerAdapter.OnItemClickListener{
    lateinit var binding: AdminkullanicisilactivityBinding

    lateinit var KullaniciİsimleriRecyclerView: RecyclerView
    lateinit var KullaniciİsimleriArrayList: ArrayList<AdminKullaniciSilYonlendirme>
    var kullaniciisimleribaba:ArrayList<String> = arrayListOf()
    var kullaniciideleribaba:ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=AdminkullanicisilactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var k_adlari:ArrayList<String> =intent?.extras?.getStringArrayList("kullaniciisimleri") as ArrayList<String>
        var k_ideleri: ArrayList<String> =intent?.extras?.getStringArrayList("kullaniciideleri") as ArrayList<String>

        kullaniciisimleribaba=k_adlari
        kullaniciideleribaba=k_ideleri
        KullaniciİsimleriRecyclerView = binding.kullaniciList
        KullaniciİsimleriRecyclerView.layoutManager = LinearLayoutManager(this)
        KullaniciİsimleriRecyclerView.setHasFixedSize(true)
        KullaniciİsimleriArrayList=ArrayList<AdminKullaniciSilYonlendirme>()

        for (i in k_adlari){
            var username= AdminKullaniciSilYonlendirme(i.trim().toString())
            KullaniciİsimleriArrayList.add(username)
       }
        KullaniciİsimleriRecyclerView.adapter= AdminCustomerAdapter(KullaniciİsimleriArrayList,this@AdminKullaniciSil)



        Log.i("admin kullanici sil",k_adlari.toString())
        Log.i("admin kullanici sil",k_ideleri.toString())

    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this,"${position} elemente tıklanıldı.",Toast.LENGTH_SHORT).show()
        var intent= Intent(this, AdminKullaniciSilSon::class.java)
        var bundle=Bundle()
        bundle.putInt("pozisyon",position)
        bundle.putString("kullaniciAdi",kullaniciisimleribaba.get(position))
        bundle.putString("kullaniciIde",kullaniciideleribaba.get(position))
        intent.putExtras(bundle)
        startActivity(intent)

    }
}