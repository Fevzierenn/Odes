package com.example.firebasedeneme

import User
import android.app.Dialog
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.example.firebasedeneme.databinding.ArkadaslikistegiyollamapenceresiBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class ArkadaslıkIstegiAtmaPenceresi:AppCompatActivity(){
    lateinit var binding:ArkadaslikistegiyollamapenceresiBinding
    lateinit var mAuth: FirebaseAuth
    var mUserRef: DatabaseReference? = null
    var arkadaslarim: DatabaseReference? = null
    var mRequestRef: DatabaseReference? = null
    var mfriendRef: DatabaseReference? = null
    lateinit var mUser:FirebaseUser
    //private lateinit var storageReference: StorageReference
    private lateinit var dialog: Dialog
    private lateinit var user: User
    private lateinit var uid: String
    var database: FirebaseDatabase? = null

    var CurrentState:String="nothing_happen"
    var kontrol:Boolean=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ArkadaslikistegiyollamapenceresiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //----------_---------
        var userId:String = intent.extras?.getString("kullaniciid").toString()          //listede tıklananın idsi

        mUserRef=FirebaseDatabase.getInstance().getReference().child("users").child(userId)
        mRequestRef=FirebaseDatabase.getInstance().getReference().child("Requests")
        arkadaslarim=FirebaseDatabase.getInstance().getReference().child("Arkadaslarim")

        mfriendRef=FirebaseDatabase.getInstance().getReference().child("Friends")
        mAuth=FirebaseAuth.getInstance()                            //com.google.firebase.auth.internal.zzv@...
        mUser= mAuth.currentUser!!

        var arkadaslistesiidler: ArrayList<String> =ArrayList()


        binding.arkYolla.setOnClickListener {
            PerformAction(userId)


        }
         chechUserExistance(userId)






    }   //oncreate bitişi

    private fun arkadaslarinkullaniciisimleri() {

    }

    private fun chechUserExistance(userId: String){

            var refarans1=mfriendRef?.child(mUser.uid)?.child(userId)
        val listener=refarans1?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    if (snapshot.child("status").getValue().toString().equals("friend")){
                        binding.arkYolla.setText("Arkadaşlıktan çıkar")
                        kontrol=true
                        //binding.textView1.text=mfriendRef?.child(mUser.uid)?.child(userId).toString()

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }       //userexistance fonksiyonu sonu


    fun PerformAction(userId:String) {

        if (CurrentState.equals("nothing_happen")){
            var hashmap:HashMap<String,String> = HashMap()
            hashmap.put("status","pending")
            mRequestRef!!.child(mUser!!.uid).child(userId).updateChildren(hashmap as Map<String, String>).addOnCompleteListener(this){task->
                if (task.isSuccessful){
                    Toast.makeText(this,"arkadaş olarak ekledin.",Toast.LENGTH_SHORT).show()

                    CurrentState="He_sent_pending"
                    Log.i("Current state:","Nothing happing çalıştı.")
                    binding.arkYolla.setText("arkadaşlıktan çıkar")
                    mRequestRef!!.child(userId).child(mUser!!.uid).updateChildren(hashmap as Map<String,String>).addOnCompleteListener(this){task->
                        if (task.isSuccessful){
                            hashmap.put("status","friend")
                            mfriendRef?.child(mUser.uid)?.child(userId)?.updateChildren(hashmap as Map<String,String>)!!.addOnCompleteListener(this){task->
                            if(task.isSuccessful){

                            }

                                else{

                                }
                            }
                        }

                    }
                }
                else{
                    Toast.makeText(this,""+task.exception.toString(),Toast.LENGTH_SHORT).show()
                }
            }

        }



    }       //perform action fonksiyonu bitişi


}