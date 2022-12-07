package com.example.firebasedeneme

class Kullanici(var mail:String,var k_adi:String,var sifre:String,
                var alinacakpara:Double=0.0,var odenecekborcpara:Double=0.0,
                val arkadasListesi:ArrayList<String> = ArrayList<String>()
) {

    fun main(){
        var user1:Kullanici= Kullanici("mail@mail","kullanıcıadım","password12",)
        println(listeyeArkadasEkle(user1.arkadasListesi,"fevzierenn"))

    }
    fun listeyeArkadasEkle(arkadasListesi: ArrayList<String>, username:String):Int{
        var sayac:Int=arkadasListesi.indexOf(username)
        return sayac
    }


}