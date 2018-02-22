package com.shopapp.ui.custom.zoomable

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Matrix
import android.view.animation.DecelerateInterpolator
import com.shopapp.ui.custom.zoomable.gestures.TransformGestureDetector
import com.facebook.common.internal.Preconditions

/**
 * ZoomableController that adds animation capabilities to DefaultZoomableController using standard
 * Android animation classes
 */
class AnimatedZoomableController @SuppressLint("NewApi")
constructor(transformGestureDetector: TransformGestureDetector) : AbstractAnimatedZoomableController(transformGestureDetector) {

    private val mValueAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)

    init {
        mValueAnimator.interpolator = DecelerateInterpolator()
    }

    @SuppressLint("NewApi")
    override fun setTransformAnimated(
        newTransform: Matrix,
        durationMs: Long,
        onAnimationComplete: Runnable?) {
        stopAnimation()
        Preconditions.checkArgument(durationMs > 0)
        Preconditions.checkState(!isAnimating)
        isAnimating = true
        mValueAnimator.duration = durationMs
        getTransform().getValues(startValues)
        newTransform.getValues(stopValues)
        mValueAnimator.addUpdateListener { valueAnimator ->
            calculateInterpolation(workingTransform, valueAnimator.animatedValue as Float)
            super@AnimatedZoomableController.setTransform(workingTransform)
        }
        mValueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationCancel(animation: Animator) {
                onAnimationStopped()
            }

            override fun onAnimationEnd(animation: Animator) {
                onAnimationStopped()
            }

            private fun onAnimationStopped() {
                onAnimationComplete?.run()
                isAnimating = false
                detector.restartGesture()
            }
        })
        mValueAnimator.start()
    }

    @SuppressLint("NewApi")
    public override fun stopAnimation() {
        if (!isAnimating) {
            return
        }
        mValueAnimator.cancel()
        mValueAnimator.removeAllUpdateListeners()
        mValueAnimator.removeAllListeners()
    }

    companion object {
        fun newInstance(): AnimatedZoomableController {
            return AnimatedZoomableController(TransformGestureDetector.newInstance())
        }
    }

}
