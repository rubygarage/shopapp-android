/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.client.shop.ui.custom.zoomable

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Matrix
import android.view.animation.DecelerateInterpolator
import com.client.shop.ui.custom.zoomable.gestures.TransformGestureDetector
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
