package com.example.firebasedeneme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val groupList: ArrayList<User>, private var listener: OnItemClickListener) :     //User classındaki bilgileri atadık
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {



    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
    View.OnClickListener{
       val grupismi:TextView=view.findViewById(R.id.kurunismi)
       val alinacak:TextView=view.findViewById(R.id.tvAlinacak)
       val borc:TextView=view.findViewById(R.id.tvBorc)
       val ekonomikDurum:TextView=view.findViewById(R.id.tvGrupEkonomikDurum)

        init {
            itemView.setOnClickListener(this,)
        }

        override fun onClick(v: View?) {
            val position:Int = absoluteAdapterPosition          //değiştirilebilir
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
            else{
                listener.onItemClick(position)
            }


        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.user_group_list, viewGroup, false)                //item viewdeki örnek sayfamızı tanımlıyoruz

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.grupismi.text = groupList[position].grupİsmi             //Sınıfımıza gelen bilgileri sayfamıza taşıyoruz.
        viewHolder.alinacak.text = groupList[position].k_adi             //Sınıfımıza gelen bilgileri sayfamıza taşıyoruz.
        viewHolder.alinacak.text = groupList[position].alinacak.toString()
        viewHolder.borc.text = groupList[position].borc.toString()
        viewHolder.ekonomikDurum.text = groupList[position].ekonomikDurum.toString()             //Sınıfımıza gelen bilgileri sayfamıza taşıyoruz.

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = groupList.size

}
