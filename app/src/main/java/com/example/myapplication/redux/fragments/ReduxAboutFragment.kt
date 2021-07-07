package com.example.myapplication.redux.fragments

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.example.myapplication.animations.AnimationsProvider
import com.example.myapplication.databinding.FragmentAboutBinding
import org.koin.core.component.inject

class ReduxAboutFragment : ReduxFragment() {

    private lateinit var binding: FragmentAboutBinding
    private val animationsProvider: AnimationsProvider by inject()
    private var entryAnimationPresented = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAboutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.aboutText.text = HtmlCompat.fromHtml(" <h1>Hello and Welcome</h1>" +
                "<p>This is a <b>Store</b> built using <b>Redux</b> architecture, " +
                "this architecture uses:</p> <b>State</b> to store the app information " +
                "<br><b>Reducers</b> to execute any change in the State </br>" +
                "<br><b>Epics</b> to store all the business logic.</br>" +
                "<br><b>RxAndroid</b> to propagate information changes. </br>" +
                "<p> All information in the <b>AppState</b> is App Wide available trough the <b>AppStore</b>, " +
                "and views can either get the current value or subscribe to get all further changes since the state is a <b>BehaviourSubject</b>.</p>" +
                "This example was created by: <b>Alfredo Arrieta</b>" +
                "<br><a href=\"https://www.linkedin.com/in/alfredo-josé-arrieta-bawab-85908996/\">linkedIn</br>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.aboutText.movementMethod = LinkMovementMethod.getInstance()
    }

    fun entryAnimation(){
        if(entryAnimationPresented.not()){
            binding.mainContainer.post { animationsProvider.entryRightAnimation(binding.mainContainer) }
            entryAnimationPresented = true
        }
    }

    fun exitAnimation(callback: AnimationsProvider.AnimationEndCallback? = null){
        if(entryAnimationPresented) {
            binding.mainContainer.post { animationsProvider.exitRightAnimation(binding.mainContainer,callback) }
            entryAnimationPresented = false
        } else {
            callback?.onAnimationEnd()
        }
    }
}