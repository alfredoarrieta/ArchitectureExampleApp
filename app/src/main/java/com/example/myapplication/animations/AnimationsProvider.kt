package com.example.myapplication.animations

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

class AnimationsProvider {

    fun startLeftSlidingViewAnimation(view: View) {
        ObjectAnimator.ofFloat(view, "translationX", 0F).apply{
            duration = 750
            start()
        }
    }

    fun finishLeftSlidingViewAnimation(view: View){
        ObjectAnimator.ofFloat(view, "translationX", view.width.toFloat()).apply {
            duration = 500
            start()
        }
    }

    fun entryBottomAnimation(view: View){
        view.translationY = view.rootView.height.toFloat()
        ObjectAnimator.ofFloat(
            view,
            "translationY",
            0F
        ).setDuration(750).start()
    }

    fun exitBottomAnimation(view: View, callback: AnimationEndCallback?){
        ObjectAnimator.ofFloat(view, "translationY", view.rootView.height.toFloat())
            .apply {
                duration = 750
                addListener(object: Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator?) {}
                    override fun onAnimationEnd(animation: Animator?) {
                        callback?.onAnimationEnd()
                    }
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationRepeat(animation: Animator?) {}
                })
                start()
            }
    }

    fun entryRightAnimation(view: View){
        view.translationX = view.rootView.width.toFloat()
        ObjectAnimator.ofFloat(
            view,
            "translationX",
            0F
        ).setDuration(750).start()
    }

    fun exitRightAnimation(view: View, callback: AnimationEndCallback?){
        ObjectAnimator.ofFloat(view, "translationX", view.rootView.width.toFloat())
            .apply {
                duration = 750
                addListener(object: Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator?) {}
                    override fun onAnimationEnd(animation: Animator?) {
                        callback?.onAnimationEnd()
                    }
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationRepeat(animation: Animator?) {}
                })
                start()
            }
    }

    interface AnimationEndCallback{
        fun onAnimationEnd()
    }
}