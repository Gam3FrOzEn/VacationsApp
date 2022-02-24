package com.android.vacationsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.vacationsapp.R
import kotlinx.android.synthetic.main.fragment_vacation.view.*
import com.android.vacationsapp.adapters.VacationAdapter
import com.android.vacationsapp.database.DBHelper
import com.android.vacationsapp.models.Vacation

class VacationFragment : Fragment() {

    var list: MutableList<Vacation> = ArrayList()
    var recyclerView: RecyclerView? = null
    var mAdapter: VacationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_vacation, container, false)

        (activity as AppCompatActivity).supportActionBar!!.title =
            getString(R.string.desired_vacations)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        recyclerView = view.recycler_view

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    view.add.hide()
                } else {
                    view.add.show()
                }
            }
        })

        view.add.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_layout, AddFragment.newInstance("add", Vacation()))
                .addToBackStack(null).commit()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        list.clear()
        list = DBHelper(requireContext()).vacations
        mAdapter = VacationAdapter(list, requireContext())
        recyclerView!!.adapter = mAdapter
    }
}