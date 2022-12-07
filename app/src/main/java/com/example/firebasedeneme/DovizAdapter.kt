package com.example.firebasedeneme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DovizAdapter(private val dataSet: ArrayList<DoviziRecyclerVieweTasima>, private var listener: Dovizler) :
    RecyclerView.Adapter<DovizAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener{
        val kurismi:TextView =view.findViewById(R.id.dovizName)
        val tamkurismi:TextView=view.findViewById(R.id.isimler)

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
            .inflate(R.layout.doviz_one_item, viewGroup, false)                //item viewdeki örnek sayfamızı tanımlıyoruz

        return ViewHolder(view)
    }



    override fun getItemCount() = dataSet.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.kurismi.text= dataSet[position].paraİsmi.toString()
        holder.tamkurismi.text= dataSet[position].paratamismi.toString()
    }


}




