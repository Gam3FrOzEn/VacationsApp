package com.android.vacationsapp.adapters

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.vacationsapp.R
import kotlinx.android.synthetic.main.rv_notification.view.*
import kotlinx.android.synthetic.main.rv_vacation.view.name
import com.android.vacationsapp.database.DBHelper
import com.android.vacationsapp.models.Notification

class NotificationAdapter(
    var mValues: MutableList<Notification>,
    val mCtx: Context
) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_notification, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(h: MyViewHolder, position: Int) {
        val m = mValues[position]

        val db = DBHelper(mCtx)
        h.name.text = m.name!!
        h.desc.text = m.desc!!
        h.time.text = "${m.date} ${m.time}"

        h.itemView.setOnLongClickListener {
            AlertDialog.Builder(mCtx)
                .setMessage("Update or Delete?")

                .setPositiveButton(
                    "Delete"
                ) { p0, p1 ->
                    val pendingIntent = PendingIntent.getBroadcast(
                        mCtx,
                        m.id,
                        Intent(),
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    val am = mCtx.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
                    am.cancel(pendingIntent)

                    db.deleteNotification(m.id.toString())
                    mValues.remove(m)
                    mValues = db.notifications(m.vacation.toString())
                    Toast.makeText(mCtx, "Successfully deleted!", Toast.LENGTH_LONG).show()
                    notifyDataSetChanged()
                }
                .setNegativeButton(
                    "Dismiss"
                ) { p0, p1 ->
                    p0.dismiss()
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
        val desc = itemView.desc
        val time = itemView.time
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

    fun setFilter(newList: MutableList<Notification>) {
        mValues = ArrayList()
        mValues.addAll(newList)
        notifyDataSetChanged()
    }
}
