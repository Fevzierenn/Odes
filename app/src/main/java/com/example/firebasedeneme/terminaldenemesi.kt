//package com.example.firebasedeneme



 class User(var id:String,var mail:String,var k_adi:String,var sifre:String,
                var alinacakpara:Double=0.0,var odenecekborcpara:Double=0.0,
                val arkadasListesi:MutableList<String> = mutableListOf()
) {
 }


    fun main(){
        var user1: User = User("1","mail@mail","kullanıcıadım","password12")
        //println(listeyeArkadasEkle(user1.arkadasListesi,"fevzierenn"))
        listeyeArkadasEkle(user1.arkadasListesi,"fevzierenn")
        listeyeArkadasEkle(user1.arkadasListesi,"fevzierenn")
        listeyeArkadasEkle(user1.arkadasListesi,"dila")
        println(listeyeArkadasEkle(user1.arkadasListesi,"fevzierenn"))

    }
    fun listeyeArkadasEkle(arkadasListesi: MutableList<String>, username:String):MutableList<String>{
        var sayac:Int=arkadasListesi.indexOf(username)
        if(sayac== -1){
            arkadasListesi.add(username)
            println(username+ "added by friends")
        }
        else{
            println(username+ "its already a friend")
        }
        return arkadasListesi
    }


