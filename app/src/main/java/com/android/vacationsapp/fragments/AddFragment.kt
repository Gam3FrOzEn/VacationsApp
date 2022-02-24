package com.android.vacationsapp.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.android.vacationsapp.R
import kotlinx.android.synthetic.main.fragment_add.view.*
import com.android.vacationsapp.database.DBHelper
import com.android.vacationsapp.models.Vacation

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddFragment : Fragment() {
    private var type: String? = null
    private var vacation: Vacation? = null
    private val pickImage = 100
    private var uri: Uri? = null
    var v: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(ARG_PARAM1)
            vacation = it.getSerializable(ARG_PARAM2) as Vacation
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        v = view

        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.add_vacation)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (type == "update") {
            view.image.setImageURI(Uri.parse(vacation!!.image))
            uri = Uri.parse(vacation!!.image)
            view.name.setText(vacation!!.name)
            view.hotel.setText(vacation!!.hotel)
            view.location.setText(vacation!!.location)
            view.amount.setText(vacation!!.money)
            view.desc.setText(vacation!!.description)
            view!!.add.text = getString(R.string.update_vacation)
        }

        view.browse_image.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        view.add.setOnClickListener {
            val n = view.name.text.toString()
            val h = view.hotel.text.toString()
            val l = view.location.text.toString()
            val m = view.amount.text.toString()
            val d = view.desc.text.toString()

            when {
                uri == null -> {
                    Toast.makeText(requireActivity(), "Choose image", Toast.LENGTH_SHORT).show()
                }
                n.isEmpty() -> {
                    view.name.error = "Enter Name"
                }
                h.isEmpty() -> {
                    view.hotel.error = "Enter Hotel Name"
                }
                l.isEmpty() -> {
                    view.location.error = "Enter Location"
                }
                m.isEmpty() -> {
                    view.amount.error = "Enter Amount"
                }
                d.isEmpty() -> {
                    view.desc.error = "Enter Description"
                }
                else -> {
                    if (type == "add") {
                        DBHelper(requireActivity()).insertVacation(
                            Vacation(
                                n,
                                h,
                                l,
                                m,
                                d,
                                uri.toString()
                            )
                        )
                    } else {
                        DBHelper(requireContext()).updateVacation(
                            Vacation(
                                vacation!!.id,
                                n,
                                h,
                                l,
                                m,
                                d,
                                uri.toString()
                            )
                        )
                    }
                    requireActivity().supportFragmentManager.popBackStack(
                        null,
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
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            uri = data?.data
            v!!.image.setImageURI(uri)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(type: String, vacation: Vacation) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, type)
                    putSerializable(ARG_PARAM2, vacation)
                }
            }
    }
}