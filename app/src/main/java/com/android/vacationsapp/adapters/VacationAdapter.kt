package com.android.vacationsapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.vacationsapp.R
import kotlinx.android.synthetic.main.rv_vacation.view.*
import com.android.vacationsapp.database.DBHelper
import com.android.vacationsapp.fragments.AddFragment
import com.android.vacationsapp.fragments.DetailFragment
import com.android.vacationsapp.models.Vacation

class VacationAdapter(
    var mValues: MutableList<Vacation>,
    val mCtx: Context
) : RecyclerView.Adapter<VacationAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_vacation, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(h: MyViewHolder, position: Int) {
        val m = mValues[position]

        val db = DBHelper(mCtx)
        h.name.text = m.name!!
        h.desc.text = m.location!!
        h.image.setImageURI(Uri.parse(m.image))

        h.itemView.setOnClickListener {
            (mCtx as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_layout, DetailFragment.newInstance(m)).addToBackStack(null)
                .commit()
        }
        h.itemView.setOnLongClickListener {
            AlertDialog.Builder(mCtx)
                .setMessage("Update or Delete?")
                .setPositiveButton(
                    "Update"
                ) { p0, p1 ->
                    p0.dismiss()
                    (mCtx as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout, AddFragment.newInstance("update", m))
                        .addToBackStack(null)
                        .commit()
                }
                .setNegativeButton(
                    "Delete"
                ) { p0, p1 ->
                    db.deleteVacation(m.id.toString())
                    mValues.remove(m)
                    mValues = db.vacations
                    Toast.makeText(mCtx, "Successfully deleted!", Toast.LENGTH_LONG).show()
                    notifyDataSetChanged()
                }
                .show()
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val name = itemView.name
        val desc = itemView.location
        val image = itemView.image_view
    }

    companion object {
        private var onItemClickListener: OnItemClickListener? = null
    }

    fun setOnItemClickListener(click: OnItemClickListener) {
        onItemClickListener = click
    }

    interface OnItemClickListener {
        fun onItemClick(u: String)
    }

    fun setFilter(newList: MutableList<Vacation>) {
        mValues = ArrayList()
        mValues.addAll(newList)
        notifyDataSetChanged()
    }
}
