package com.example.firebasedeneme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedeneme.databinding.ActivityGroupListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.grubunicerisi.*

class GroupListActivity : AppCompatActivity() , CustomAdapter.OnItemClickListener{         //UserList activity
    lateinit var binding: ActivityGroupListBinding
    lateinit var groupRecyclerView: RecyclerView
    lateinit var groupArrayList: ArrayList<User>

    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    private lateinit var uid: String
    private lateinit var mUser: FirebaseUser

    var bulundugumgruplar:ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        mUser = auth.currentUser!!
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Groups")


        groupRecyclerView = binding.userList
        groupRecyclerView.layoutManager = LinearLayoutManager(this)
        groupRecyclerView.setHasFixedSize(true)

        groupArrayList = arrayListOf<User>()             //Userdaki bilgileri alacağız

        grupBilgileriniGetir()
        Log.i("gruplar",bulundugumgruplar.toString())

        binding.Home.setOnClickListener {
            var intent=Intent(this,ProgramMerhabaSayfasi::class.java)
            startActivity(intent)
        }





    }


    private fun grupBilgileriniGetir() {
        var idler:ArrayList<String> = arrayListOf()
        var alinacak:String? =null
        var borc:String?     =null
        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){                   // i = { key = Hancagiz tayfa, value = {uwVpTIBm4ad2aqWQBZF8pnIPmgW2={borc=0, k_adi=fevzierenn, alinacak=0, id=uwVpTIBm4ad2aqWQBZF8pnIPmgW2}, 9GWTuPEHpfc3XZXTCjuU3a0tZ7p2={borc=0, k_adi=bdesus, alinacak=0, id=9GWTuPEHpfc3XZXTCjuU3a0tZ7p2}} },  { key = denemeler, value = {4BK7IVbwaeMBOSDNRKqkiEyGkX33={borc=0, k_adi=Neymar92, alinacak=0, id=4BK7IVbwaeMBOSDNRKqkiEyGkX33}, uwVpTIBm4ad2aqWQBZF8pnIPmgW2={borc=0, k_adi=fevzierenn, alinacak=0, id=uwVpTIBm4ad2aqWQBZF8pnIPmgW2}, 9GWTuPEHpfc3XZXTCjuU3a0tZ7p2={borc=0, k_adi=bdesus, alinacak=0, id=9GWTuPEHpfc3XZXTCjuU3a0tZ7p2}, 5mBMJZVyJ5TarmOb4yVYCnh1IqJ3={borc=0, k_adi=messi10, alinacak=0, id=5mBMJZVyJ5TarmOb4yVYCnh1IqJ3}} }
                        //Log.i("snapshot",i.value.toString())
                        for (m in i.children){                      //m snapshot->{ key = 4BK7IVbwaeMBOSDNRKqkiEyGkX33, value = {borc=0, k_adi=Neymar92, alinacak=0, id=4BK7IVbwaeMBOSDNRKqkiEyGkX33} }
                            //idler.add(m.key.toString())                                         //m snapshot->{ key = 9GWTuPEHpfc3XZXTCjuU3a0tZ7p2, value = {borc=0, k_adi=bdesus, alinacak=0, id=9GWTuPEHpfc3XZXTCjuU3a0tZ7p2} }
                            //Log.i("m snapshot",m.key.toString())        //m.keyler gruplardaki idleri veriyor


                            if (m.key?.trim().toString().equals(uid.toString().trim())){


                                    bulundugumgruplar.add(i.key.toString())

                                for (k in m.children){
                                    //Log.i("k snapshot",k.toString())
                                    if (k.key?.trim().toString().equals("alinacak")){
                                           alinacak = k.value.toString()
                                        Log.i("alinacak",alinacak.toString())
                                    }
                                    if (k.key?.trim().toString().equals("borc")){
                                        borc=k.value.toString()
                                        Log.i("borc",k.value.toString())
                                    }
                                    if (k.key?.trim().toString().equals("k_adi")){

                                        var username:String=k.value.toString()
                                        Log.i("k_adi",k.value.toString())
                                        var user1=User(i.key.toString(),username,alinacak!!.toDouble(),borc!!.toDouble())
                                        groupArrayList.add(user1)
                                        groupRecyclerView.adapter=CustomAdapter(groupArrayList,this@GroupListActivity)

                                    }




                                }


                            }
                            else{}  // if (m.key.trim().toString().equals(uid.toString().trim())){
                        }


                    }

                }
                else{ }// snapshot.exist() sonu

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }   //fonksiyon bitişi b

    override fun onItemClick(position: Int) {
        Toast.makeText(this,"item ${position} clicked",Toast.LENGTH_SHORT).show()
        //val clickedItem=groupArrayList[position]
        var intent=Intent(this,Grubunİcerisi::class.java)
        var bundle=Bundle()
        bundle.putInt("position",position)
        bundle.putString("gruplar",bulundugumgruplar.joinToString())
        intent.putExtras(bundle)
        startActivity(intent)


    }
}