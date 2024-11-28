package com.example.loginsingupwithotp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class recycalerviewadpter(private val mlist:List<DatabaseHelper.User>,private val context: Context):
    RecyclerView.Adapter<recycalerviewadpter.MyViewHolder>() {
        internal var listofitem:List<DatabaseHelper.User> =ArrayList()
    init {
        this.listofitem=mlist
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name:TextView=itemView.findViewById(R.id.text_name_list)
        val email:TextView=itemView.findViewById(R.id.text_emai_list)
        val password:TextView=itemView.findViewById(R.id.text_password_list)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
      val view =LayoutInflater.from(context).inflate(R.layout.listofrecycleview,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
      return mlist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      val hold=mlist[position]
        holder.name.text=hold.username
        holder.email.text=hold.email
        holder.password.text=hold.password
    }
}