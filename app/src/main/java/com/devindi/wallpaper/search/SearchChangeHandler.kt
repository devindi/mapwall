package com.devindi.wallpaper.search

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler
import com.devindi.wallpaper.R

class SearchChangeHandler(duration: Long = -1, removesFromViewOnPush: Boolean = true) : AnimatorChangeHandler(duration, removesFromViewOnPush) {

    override fun resetFromView(from: View) {
        //do nothing
    }

    override fun getAnimator(container: ViewGroup, from: View?, to: View?, isPush: Boolean, toAddedToContainer: Boolean): Animator {
        val animator = AnimatorSet()

        if (isPush && to != null) {
            val modalBg = to.findViewById<View>(R.id.modal_bg)
            val start: Float = if (toAddedToContainer) 0F else modalBg.alpha
            animator.play(ObjectAnimator.ofFloat(modalBg, View.ALPHA, start, 1F))

            val resultView = to.findViewById<View>(R.id.list)
            animator.play(ObjectAnimator.ofFloat(resultView, View.TRANSLATION_Y, to.height.toFloat(), 0F))
        }

        if (!isPush && from != null) {
            val modalBg = from.findViewById<View>(R.id.modal_bg)
            val start: Float = modalBg.alpha
            animator.play(ObjectAnimator.ofFloat(modalBg, View.ALPHA, start, 0F))

            val resultView = from.findViewById<View>(R.id.list)
            animator.play(ObjectAnimator.ofFloat(resultView, View.TRANSLATION_Y, from.height.toFloat()))
        }

        return animator
    }
}
