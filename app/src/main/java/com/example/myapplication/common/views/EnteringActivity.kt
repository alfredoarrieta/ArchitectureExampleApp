package com.example.myapplication.common.views

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityEnteringBinding
import com.example.myapplication.mvvm.activities.MVVMActivity
import com.example.myapplication.redux.activities.ReduxActivity

class EnteringActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityEnteringBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Architecture Exercise"

        binding.tittle.text = HtmlCompat.fromHtml(
                "This example was created by: <b>Alfredo Arrieta</b>" +
                "<br><a href=\"https://www.linkedin.com/in/alfredo-josé-arrieta-bawab-85908996/\">linkedIn</br>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.tittle.movementMethod = LinkMovementMethod.getInstance()

        binding.enterButton1.text = "Enter MVVM Store"

        binding.enterButton1.setOnClickListener {
            Intent(this, MVVMActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.enterButton2.text = "Enter Redux Store"

        binding.enterButton2.setOnClickListener {
            Intent(this, ReduxActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}