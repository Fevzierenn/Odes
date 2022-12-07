package com.example.firebasedeneme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminGrupSilRecyclerView(private val dataSet: ArrayList<AdminGrupSilYonlendirme>,private var listener:AdminGrupSil
) :
    RecyclerView.Adapter<AdminGrupSilRecyclerView.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener{
        val isimler:TextView =view.findViewById(R.id.grupName)


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
            .inflate(R.layout.admingrupsil_oneitem, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.isimler.text=dataSet[position].grupismi
    }

}