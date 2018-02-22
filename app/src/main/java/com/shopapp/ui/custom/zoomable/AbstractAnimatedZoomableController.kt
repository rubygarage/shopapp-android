package com.shopapp.ui.custom.zoomable

import android.graphics.Matrix
import android.graphics.PointF
import com.shopapp.ui.custom.zoomable.gestures.TransformGestureDetector

/**
 * Abstract class for ZoomableController that adds animation capabilities to
 * DefaultZoomableController.
 */
abstract class AbstractAnimatedZoomableController(transformGestureDetector: TransformGestureDetector) : DefaultZoomableController(transformGestureDetector) {

    protected val startValues = FloatArray(9)
    protected val stopValues = FloatArray(9)
    private val mCurrentValues = FloatArray(9)
    private val mNewTransform = Matrix()
    protected val workingTransform = Matrix()
    protected var isAnimating: Boolean = false

    override fun reset() {
        stopAnimation()
        workingTransform.reset()
        mNewTransform.reset()
        super.reset()
    }

    /**
     * Returns true if the zoomable transform is identity matrix, and the controller is idle.
     */
    override fun isIdentity(): Boolean {
        return !isAnimating && super.isIdentity()
    }

    /**
     * Zooms to the desired scale and positions the image so that the given image point corresponds
     * to the given view point.
     *
     *
     *
     * If this method is called while an animation or gesture is already in progress,
     * the current animation or gesture will be stopped first.
     *
     * @param scale      desired scale, will be limited to {min, max} scale factor
     * @param imagePoint 2D point in image's relative coordinate system (i.e. 0 <= x, y <= 1)
     * @param viewPoint  2D point in view's absolute coordinate system
     */
    override fun zoomToPoint(
        scale: Float,
        imagePoint: PointF,
        viewPoint: PointF) {
        zoomToPoint(scale, imagePoint, viewPoint, LIMIT_ALL, 0, null)
    }

    /**
     * Zooms to the desired scale and positions the image so that the given image point corresponds
     * to the given view point.
     *
     *
     *
     * If this method is called while an animation or gesture is already in progress,
     * the current animation or gesture will be stopped first.
     *
     * @param scale               desired scale, will be limited to {min, max} scale factor
     * @param imagePoint          2D point in image's relative coordinate system (i.e. 0 <= x, y <= 1)
     * @param viewPoint           2D point in view's absolute coordinate system
     * @param limitFlags          whether to limit translation and/or scale.
     * @param durationMs          length of animation of the zoom, or 0 if no animation desired
     * @param onAnimationComplete code to run when the animation completes. Ignored if durationMs=0
     */
    fun zoomToPoint(
        scale: Float,
        imagePoint: PointF,
        viewPoint: PointF,
        @LimitFlag limitFlags: Int,
        durationMs: Long,
        onAnimationComplete: Runnable?) {
        calculateZoomToPointTransform(
            mNewTransform,
            scale,
            imagePoint,
            viewPoint,
            limitFlags)
        setTransform(mNewTransform, durationMs, onAnimationComplete)
    }

    /**
     * Sets a new zoomable transformation and animates to it if desired.
     *
     *
     *
     * If this method is called while an animation or gesture is already in progress,
     * the current animation or gesture will be stopped first.
     *
     * @param newTransform        new transform to make active
     * @param durationMs          duration of the animation, or 0 to not animate
     * @param onAnimationComplete code to run when the animation completes. Ignored if durationMs=0
     */
    private fun setTransform(
        newTransform: Matrix,
        durationMs: Long,
        onAnimationComplete: Runnable?) {
        if (durationMs <= 0) {
            setTransformImmediate(newTransform)
        } else {
            setTransformAnimated(newTransform, durationMs, onAnimationComplete)
        }
    }

    private fun setTransformImmediate(newTransform: Matrix) {
        stopAnimation()
        workingTransform.set(newTransform)
        super.setTransform(newTransform)
        detector.restartGesture()
    }

    override fun onGestureBegin(detector: TransformGestureDetector) {
        stopAnimation()
        super.onGestureBegin(detector)
    }

    override fun onGestureUpdate(detector: TransformGestureDetector) {
        if (isAnimating) {
            return
        }
        super.onGestureUpdate(detector)
    }

    protected fun calculateInterpolation(outMatrix: Matrix, fraction: Float) {
        for (i in 0..8) {
            mCurrentValues[i] = (1 - fraction) * startValues[i] + fraction * stopValues[i]
        }
        outMatrix.setValues(mCurrentValues)
    }

    abstract fun setTransformAnimated(
        newTransform: Matrix,
        durationMs: Long,
        onAnimationComplete: Runnable?)

    protected abstract fun stopAnimation()
}
