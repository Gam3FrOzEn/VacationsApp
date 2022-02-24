package com.android.vacationsapp.fragments
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.vacationsapp.R
import kotlinx.android.synthetic.main.fragment_detail.view.*
import com.android.vacationsapp.models.Vacation

private const val ARG_PARAM1 = "param1"

class DetailFragment : Fragment() {
    private var vacation: Vacation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            vacation = it.getSerializable(ARG_PARAM1) as Vacation?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        (activity as AppCompatActivity).supportActionBar!!.title = vacation!!.name
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        view.image.setImageURI(Uri.parse(vacation!!.image))
        view.name.text = vacation!!.name
        view.hotel.text = vacation!!.hotel
        view.location.text = vacation!!.location
        view.amount.text = vacation!!.money
        view.desc.text = vacation!!.description

        setHasOptionsMenu(true)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(vacation: Vacation) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, vacation)
                }
            }
    }
}