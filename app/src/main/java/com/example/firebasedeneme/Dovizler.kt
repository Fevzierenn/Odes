package com.example.firebasedeneme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedeneme.databinding.KuroranlariactivityBinding
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class Dovizler: AppCompatActivity(), DovizAdapter.OnItemClickListener{
    lateinit var binding:KuroranlariactivityBinding
    lateinit var dovizRecyclerView: RecyclerView
    lateinit var dovizArrayList: ArrayList<DoviziRecyclerVieweTasima>
    var dovizlerinisimleri:ArrayList<String> = arrayListOf()
    var siralidovizlerindegerleri:ArrayList<String> = arrayListOf()
    var siralidovizlerinTamisimleri:ArrayList<String> = arrayListOf()
    var deger:String = ""
    var songuncelleme:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = KuroranlariactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

         deger=intent?.extras?.getString("deger").toString().trim()
        Log.i("deger doviz page",deger)
        dovizRecyclerView = binding.dovizList
        dovizRecyclerView.layoutManager = LinearLayoutManager(this)
        dovizRecyclerView.setHasFixedSize(true)

        dovizArrayList = arrayListOf<DoviziRecyclerVieweTasima>()             //Userdaki bilgileri alacağız


        fetchCurrencyData().start()
        dovizlerinisimleri = arrayListOf("EUR","USD","GBP","JPY","RUB","AUD","SAR","CAD","CHF","CNY","MXN","SGD","HKD","KRW","INR","BRL","TRY")
        siralidovizlerinTamisimleri= arrayListOf("Euro","Dolar","Pound","Japon Yeni","Ruble","Avustralya Doları","Arabistan Riyali","Kanada Doları",
            "İsviçre Frangı","Çin Yuanı","Meksika Pezosu","Singapur Doları","Hong Kong Doları","Güney Kore Wongu","Hint Rupisi",
            "Brezilya Reali","Turk Lirası")
        for (i in dovizlerinisimleri){
            var doviz1 = DoviziRecyclerVieweTasima(i.toString(),siralidovizlerinTamisimleri.get(dovizlerinisimleri.indexOf(i)).trim().toString())
            dovizArrayList.add(doviz1)
        }
        dovizRecyclerView.adapter=DovizAdapter(dovizArrayList,this@Dovizler)

        binding.Home.setOnClickListener {
            var intent= Intent(this, ProgramMerhabaSayfasi::class.java)
            startActivity(intent)
        }
    }

    private fun fetchCurrencyData(): Thread
    {
        return Thread {
            val url = URL("https://open.er-api.com/v6/latest/try")
            val connection  = url.openConnection() as HttpsURLConnection

            if(connection.responseCode == 200)
            {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val request = Gson().fromJson(inputStreamReader, Request::class.java)
                updateUI(request)
                inputStreamReader.close()
                inputSystem.close()
            }
            else
            {
                binding.hatamessage.text = "Failed Connection"
            }
        }
    }
    private fun updateUI(request: Request)
    {
        runOnUiThread {
            kotlin.run {
                //binding.lastUpdated.text = request.time_last_update_utc
                //binding.nzd.text = String.format("EURO: %.4f", request.rates.EUR)
                //binding.usd.text = String.format("USD: %.4f", request.rates.USD)
                //binding.gbp.text = String.format("GBP: %.4f", request.rates.GBP)
                //binding.turklirasi.text = String.format("TRY: %.4f", request.rates.GBP)
                songuncelleme=request.time_last_update_utc
                Log.i("doviz",songuncelleme)
                var Euro:String=  String.format("%.4f", request.rates.EUR)
                var Dolar:String=  String.format("%.4f", request.rates.USD)
                var Pound:String=  String.format("%.4f", request.rates.GBP)
                var Japon_Yeni:String=  String.format("%.4f", request.rates.JPY)
                var Ruble:String=  String.format("%.4f", request.rates.RUB)
                var Avustralya_Dolari:String=  String.format("%.4f", request.rates.AUD)
                var Suudi_Arabistan_Riyali:String=  String.format("%.4f", request.rates.SAR)
                var Kanada_Dolari:String=  String.format("%.4f", request.rates.CAD)
                var İsviçre_Frangi:String=  String.format("%.4f", request.rates.CHF)
                var Cin_Yuani:String=  String.format("%.4f", request.rates.CNY)
                var Meksika_Pezosu:String=  String.format("%.4f", request.rates.MXN)
                var Singapur_Dolari:String=  String.format("%.4f", request.rates.SGD)
                var Hong_Kong_Dolari:String=  String.format("%.4f", request.rates.HKD)
                var Güney_Koreli_Wonu:String=  String.format("%.4f", request.rates.KRW)
                var Hint_Rupisi:String=  String.format("%.4f", request.rates.INR)
                var Brezilya_Reali:String=  String.format("%.4f", request.rates.BRL)
                var Turk_Lirasi:String=  String.format("%.4f", request.rates.TRY)
                siralidovizlerindegerleri.add(Euro)
                siralidovizlerindegerleri.add(Dolar)
                siralidovizlerindegerleri.add(Pound)
                siralidovizlerindegerleri.add(Japon_Yeni)
                siralidovizlerindegerleri.add(Ruble)
                siralidovizlerindegerleri.add(Avustralya_Dolari)
                siralidovizlerindegerleri.add(Suudi_Arabistan_Riyali)
                siralidovizlerindegerleri.add(Kanada_Dolari)
                siralidovizlerindegerleri.add(İsviçre_Frangi)
                siralidovizlerindegerleri.add(Cin_Yuani)
                siralidovizlerindegerleri.add(Meksika_Pezosu)
                siralidovizlerindegerleri.add(Singapur_Dolari)
                siralidovizlerindegerleri.add(Hong_Kong_Dolari)
                siralidovizlerindegerleri.add(Güney_Koreli_Wonu)
                siralidovizlerindegerleri.add(Hint_Rupisi)
                siralidovizlerindegerleri.add(Brezilya_Reali)
                siralidovizlerindegerleri.add(Turk_Lirasi)
                Log.i("döviz liste",siralidovizlerindegerleri.toString())


            }
        }
    }
    override fun onItemClick(position: Int) {
        Log.i("onitem list",siralidovizlerindegerleri.toString())
        Log.i("onitem deger",deger.trim().toString())


        Toast.makeText(this,"item ${position} clicked.", Toast.LENGTH_SHORT).show()
        //val clickedItem=groupArrayList[position]
        var intent= Intent(this,DovizDegerleriniAlma::class.java)
        var bundle=Bundle()
        bundle.putInt("position",position)
        bundle.putStringArrayList("dovizdegerleri",siralidovizlerindegerleri)
        bundle.putString("deger",deger.trim())
        bundle.putString("songuncelleme",songuncelleme.trim())
        intent.putExtras(bundle)
        startActivity(intent)


    }





}


