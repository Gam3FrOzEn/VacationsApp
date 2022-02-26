package com.android.vacationsapp.fragments

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.android.vacationsapp.R
import kotlinx.android.synthetic.main.fragment_add_notification.view.*
import com.android.vacationsapp.database.DBHelper
import com.android.vacationsapp.models.Notification
import com.android.vacationsapp.models.Vacation
import com.android.vacationsapp.receiver.AlarmBroadcast
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "param1"

class AddNotificationFragment : Fragment() {
    private var vacation: Vacation? = null
    var da = ""
    var ti = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            vacation = it.getSerializable(ARG_PARAM1) as Vacation
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_notification, container, false)

        (activity as AppCompatActivity).supportActionBar!!.title =
            getString(R.string.create_notification)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        view.choose_time.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker = TimePickerDialog(
                requireActivity(), R.style.datepicker,
                { _: TimePicker?, selectedHour: Int, selectedMinute: Int ->

                    val h1 = if (selectedHour > 11)
                        selectedHour - 12 else selectedHour

                    val status = if (selectedHour > 11)
                        "PM" else "AM"

                    val convertMin = if (selectedMinute.toString().length == 1)
                        "0$selectedMinute" else selectedMinute

                    val convertHour = if (h1.toString().length == 1)
                        "0$h1" else h1

                    val convertH = if (selectedHour.toString().length == 1)
                        "0$selectedHour" else selectedHour

                    view.choose_time.text = "$convertHour:$convertMin $status"
                    ti = "$convertH:$convertMin"

                }, hour, minute, false
            )

            mTimePicker.show()
            mTimePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#FF6200EE"))
            mTimePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#FF6200EE"))
        }

        val myCalendar = Calendar.getInstance()
        val date1 =
            DatePickerDialog.OnDateSetListener { v: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                @SuppressLint("SimpleDateFormat") val sdf =
                    SimpleDateFormat("E, MM-dd-yyyy")
                view.choose_date.text = sdf.format(myCalendar.time)
                da = sdf.format(myCalendar.time)
            }

        view.choose_date.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireActivity(), R.style.datepicker, date1, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            datePicker.show()
            datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#FF6200EE"))
            datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#FF6200EE"))
        }

        view.create.setOnClickListener {
            val t = view.title.text.toString()
            val m = view.desc.text.toString()
            when {
                t.isEmpty() -> {
                    view.title.error = "Enter Notification title"
                }
                m.isEmpty() -> {
                    view.desc.error = "Enter Notification message"
                }
                da.isEmpty() -> {
                    Toast.makeText(
                        requireActivity(),
                        "Select notification date",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                ti.isEmpty() -> {
                    Toast.makeText(
                        requireActivity(),
                        "Select notification time",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> {
                    val am = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
                    val hasPermission: Boolean =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            am.canScheduleExactAlarms()
                        } else {
                            true
                        }

                    DBHelper(requireContext()).insertNotification(
                        Notification(
                            t,
                            m,
                            da,
                            ti,
                            vacation!!.id.toString()
                        )
                    )

                    if (!hasPermission) {
                        val intent = Intent().apply {
                            action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                        }
                        startActivity(intent)
                    } else {
                        val reqCode =
                            DBHelper(requireContext()).notifications(vacation!!.id.toString()).size + 1
                        val dt = "$da $ti}"
                        val intent = Intent(requireActivity(), AlarmBroadcast::class.java)
                        intent.putExtra("title", t)
                        intent.putExtra(
                            "message",
                            "$m in ${vacation!!.name}(${vacation!!.location})"
                        )
                        intent.putExtra("code", reqCode)

                        val pendingIntent = PendingIntent.getBroadcast(
                            requireActivity(),
                            reqCode,
                            intent,
                            PendingIntent.FLAG_IMMUTABLE
                        )
                        val date = SimpleDateFormat("E, MM-dd-yyyy hh:mm").parse(dt)
                        am.setExact(
                            AlarmManager.RTC_WAKEUP,
                            date!!.time,
                            pendingIntent
                        )
                    }

                    requireActivity().supportFragmentManager.popBackStack(
                        "b",
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }
            }
        }

        setHasOptionsMenu(true)
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().supportFragmentManager.popBackStack(
                "b",
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance(vacation: Vacation) =
            AddNotificationFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, vacation)
                }
            }
    }
}