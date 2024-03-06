package flacto.klapto.kiptos.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import flacto.klapto.kiptos.R
import flacto.klapto.kiptos.recyclerview.DETAILSDATE
import flacto.klapto.kiptos.recyclerview.DETAILSMATH
import flacto.klapto.kiptos.recyclerview.DETAILSTRIVIA

class DetailsFragment : Fragment(R.layout.fragment_details) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.tvNumberDetails).text = arguments?.getString(DETAILSMATH)
        view.findViewById<TextView>(R.id.tvNumberDetailsTrivia).text = arguments?.getString(DETAILSTRIVIA)
        view.findViewById<TextView>(R.id.tvNumberDetailsDate).text = arguments?.getString(DETAILSDATE)

        view.findViewById<TextView>(R.id.btnBack).setOnClickListener {
            findNavController().navigate(R.id.action_detailsFragment_to_mainFragment)
        }

    }

}