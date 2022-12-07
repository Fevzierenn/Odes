package com.example.firebasedeneme

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedeneme.databinding.ChangepasswordBinding
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.oAuthCredential
import com.google.firebase.database.*

class ChangePassword:AppCompatActivity() {
    lateinit var binding: ChangepasswordBinding
    var databaseReference: DatabaseReference? = null
    var databaseReferenceUsers: DatabaseReference? = null
    private lateinit var uid: String
    private lateinit var mUser: FirebaseUser
    private lateinit var credential: AuthCredential


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ChangepasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        mUser = auth.currentUser!!
        uid = auth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().reference.child("Groups")
        databaseReferenceUsers = FirebaseDatabase.getInstance().reference.child("users")

        binding.homebutton2.setOnClickListener {
            anasayfayadon()
        }

        binding.button3.setOnClickListener {
            var sifre1:String= binding.sifre1.text.toString()
            var sifre2:String=binding.sifre2.text.toString()
            var simdikisifre:String=binding.simdikisifre.text.toString()
            var sonuc:Boolean=sifrelerAyniMi(sifre1,sifre2)
            if (TextUtils.isEmpty(sifre1)){
                binding.sifre1.error="Boş bırakılamaz"
            }
            else if(TextUtils.isEmpty(sifre2)){
                binding.sifre2.error="Boş bırakılamaz"
            }
            else if(TextUtils.isEmpty(simdikisifre)){
                binding.simdikisifre.error="Boş bırakılamaz"
            }
            else if(sifre1.length<8 || sifre2.length<8){
                Toast.makeText(this,"Şifre minimum 8 haneli olmalıdır",Toast.LENGTH_SHORT).show()
            }
            else if (sonuc==false){
                Toast.makeText(this,"Şifreler birbirinden farklı",Toast.LENGTH_SHORT).show()
            }
            else{
                //sifreyiYenile(sifre1)
                credential= EmailAuthProvider.getCredential(mUser.email!!,simdikisifre.toString())
                sifreyiYenileAuth(sifre1)
                ayarlaraDon()
            }
            Log.i("Sonuc",sonuc.toString())
        }


    }

    private fun anasayfayadon() {
        var intent=Intent(this,ProgramMerhabaSayfasi::class.java)
        startActivity(intent)
        finish()
    }

    private fun sifreyiYenileAuth(sifre: String) {
    mUser.reauthenticate(credential).addOnCompleteListener { task->
        if (task.isSuccessful){
            mUser.updatePassword(sifre.trim().toString()).addOnCompleteListener { task->
                if (task.isSuccessful){
                    Toast.makeText(this,"Auth şifresi de  başarıyla değiştirildi",Toast.LENGTH_SHORT).show()
                    sifreyiYenile(sifre)
                }
                else{Toast.makeText(this,"Auth şifresi değiştirilemedi",Toast.LENGTH_SHORT).show()}
            }
        }
        else{Toast.makeText(this,"Auth şifresi değiştirilemedi...",Toast.LENGTH_SHORT).show()}
    }
    }

    private fun ayarlaraDon() {
        var intent = Intent(this, AyarlarActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun sifreyiYenile(sifre1: String) {
        var hashmap:HashMap<String,String> = hashMapOf()
        hashmap.put("sifre",sifre1)
            databaseReferenceUsers?.child(mUser.uid.trim())?.updateChildren(hashmap as Map<String,String>)?.addOnCompleteListener { task->
                if (task.isComplete){
                    Toast.makeText(this,"Şifreler başarıyla değiştirildi",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,"Bir şeyler yanlış gitti.",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sifrelerAyniMi(sifre1: String, sifre2: String) :Boolean{
        var kontrol:Boolean=false
        if (sifre1.equals(sifre2)){
                kontrol=true
            }
        else{}
        return kontrol
    }

    private fun kullaniciadlarinigetir() :ArrayList<String>{
        var kullaniciadlari2:ArrayList<String> = arrayListOf()
        databaseReferenceUsers?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        for (m in i.children){
                            Log.i("m",m.toString())
                            if (m.key?.trim().equals("k_adi")){
                                kullaniciadlari2.add(m.value.toString().trim())
                                Log.i("kullanici adi listesi",kullaniciadlari2.toString())
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return kullaniciadlari2
    }
}