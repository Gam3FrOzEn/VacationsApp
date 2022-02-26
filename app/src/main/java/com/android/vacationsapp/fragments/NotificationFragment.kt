package com.android.vacationsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.android.vacationsapp.R
import kotlinx.android.synthetic.main.fragment_notification.view.*
import com.android.vacationsapp.adapters.NotificationAdapter
import com.android.vacationsapp.database.DBHelper
import com.android.vacationsapp.models.Notification
import com.android.vacationsapp.models.Vacation

private const val ARG_PARAM1 = "param1"

class NotificationFragment : Fragment() {
    private var vacation: Vacation? = null

    var list: MutableList<Notification> = ArrayList()
    var recyclerView: RecyclerView? = null
    var mAdapter: NotificationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            vacation = it.getSerializable(ARG_PARAM1) as Vacation
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notification, container, false)
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.notifications)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        recyclerView = view.recycler_view

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    view.create.hide()
                } else {
                    view.create.show()
                }
            }
        })

        view.create.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_layout, AddNotificationFragment.newInstance(vacation!!))
                .addToBackStack("b").commit()
        }

        setHasOptionsMenu(true)
        return view
    }

    override fun onResume() {
        super.onResume()
        list.clear()
        list = DBHelper(requireContext()).notifications(vacation!!.id.toString())
        mAdapter = NotificationAdapter(list, requireContext())
        recyclerView!!.adapter = mAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().supportFragmentManager.popBackStack(
                "n",
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance(vacation: Vacation) =
            NotificationFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, vacation)
                }
            }
    }
}