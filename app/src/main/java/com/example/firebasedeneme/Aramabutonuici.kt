package com.example.firebasedeneme

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.WhentheclicksearchbuttonBinding

class Aramabutonuici:AppCompatActivity() {
    private lateinit var binding: WhentheclicksearchbuttonBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = WhentheclicksearchbuttonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //
        var aktifkullanicilistesi = intent.extras?.getString("kullanicilarlistesi")
            .toString()       //Program merhaba classından aktif kullanıcı isimlerini içeren string geldi.
        //Log.i("Aramabutonuici",aktifkullanicilistesi)
        var kullaniciidleri = intent.extras?.getString("kullaniciidleri").toString()
        //Log.i("hahahaha",kullaniciidleri)

        var userid: String = intent.extras?.getString("kullaniciid").toString()
        var kullanicilarListesi: List<String> =
            arrayListOf()                              //Adapter için arraylist oluşturuldu
        var kullanicilarIdleri: List<String> =
            arrayListOf()                              //Adapter için arraylist oluşturuldu
        kullanicilarListesi =
            aktifkullanicilistesi.split(",")                                         //string ifadeyi listeye atadık trim(,)
        //Log.i("Aramabutonuici", kullanicilarListesi.toString())
        kullanicilarIdleri = kullaniciidleri.split(",")
        //Log.i("Aramabutonuici Idler:", kullanicilarIdleri.toString())

        val itemsAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.simple_list_item_1, kullanicilarListesi)

        val itemsIdAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.simple_list_item_1, kullanicilarIdleri)

        binding.userList.adapter = itemsAdapter

        var kullanicisayisi: Int = kullaniciidleri.length.toInt()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {               //kullanıcı arama ya da entera bastıktan sonra çalışır.
                binding.searchView.clearFocus()
                if (kullanicilarListesi.contains(query)) {
                    itemsAdapter.filter.filter(query)

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {             //kullanıcın yazdığı her harf için özel filtreleme yapar.
                itemsAdapter.filter.filter(newText)
                return false
            }


        })

        var kullanim: Int = 0
        binding.userList.setOnItemClickListener { parent, view, position, id ->
            //Log.i("position",position.toString())
            //Log.i("id",id.toString())
            //Log.i("parent",parent.toString())
            //Log.i("view",view.toString())
            Toast.makeText(this,"item "+position+" tıklanıldı",Toast.LENGTH_SHORT).show()
            var intent=Intent(this,ArkadaslıkIstegiAtmaPenceresi::class.java)
            var bundle=Bundle()

            bundle.putString("kullaniciid",kullanicilarIdleri.get(position))
            intent.putExtras(bundle)
            startActivity(intent)
        }

    }


}
