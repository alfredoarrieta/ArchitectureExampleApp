package com.example.myapplication.mvvm.fragments

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAboutBinding
import com.example.myapplication.databinding.FragmentStoreBinding

class MVVMAboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAboutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.aboutText.text = HtmlCompat.fromHtml(" <h1>Hello and Welcome</h1>" +
                "<p>This is a <b>Store</b> built using <b>MVVM</b> architecture, " +
                "this architecture uses: </p> <b>View Models</b> to store information and Business Logic" +
                "<br><b>Live Data</b> to propagate information changes </br>" +
                "<p>All fragments share the activity <b>View Model</b>, " +
                "and in this <b>VM</b> the navigation and product information is stored.</p>" +
                "This example was created by: <b>Alfredo Arrieta</b>" +
                "<br><a href=\"https://www.linkedin.com/in/alfredo-josé-arrieta-bawab-85908996/\">linkedIn</br>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.aboutText.movementMethod = LinkMovementMethod.getInstance()
    }
}