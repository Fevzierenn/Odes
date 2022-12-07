package com.example.firebasedeneme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AktiviteAdapter(private val dataSet: ArrayList<Aktivite>) :
    RecyclerView.Adapter<AktiviteAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val aktivite: TextView = view.findViewById(R.id.bilgi)


    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.aktiviteler_listesi, viewGroup, false)                //item viewdeki örnek sayfamızı tanımlıyoruz

        return ViewHolder(view)
    }



    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.aktivite.text=dataSet[position].aktivite
    }
}




