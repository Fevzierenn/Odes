package com.example.firebasedeneme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminCustomerAdapter(
    private val dataSet: ArrayList<AdminKullaniciSilYonlendirme>,private  var listener: AdminKullaniciSil
) :
    RecyclerView.Adapter<AdminCustomerAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener{
        val kullaniciadlari: TextView =view.findViewById(R.id.kullaniciadi)


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
            .inflate(R.layout.adminkullanicisilone_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.kullaniciadlari.text=dataSet[position].kullaniciADİ

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
    }

