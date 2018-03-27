package com.shopapp.ui.custom.zoomable

import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.support.annotation.IntDef
import android.view.MotionEvent
import com.shopapp.ui.custom.zoomable.gestures.TransformGestureDetector

/**
 * Zoomable controller that calculates transformation based on touch events.
 */
open class DefaultZoomableController(protected val detector: TransformGestureDetector) : ZoomableController, TransformGestureDetector.Listener {
    // View bounds, in view-absolute coordinates
    /**
     * Gets the view bounds.
     */
    private val viewBounds = RectF()
    // Non-transformed image bounds, in view-absolute coordinates
    /**
     * Gets the non-transformed image bounds, in view-absolute coordinates.
     */
    private val imageBounds = RectF()
    // Transformed image bounds, in view-absolute coordinates
    /**
     * Gets the transformed image bounds, in view-absolute coordinates
     */
    private val transformedImageBounds = RectF()
    private val mPreviousTransform = Matrix()
    private val mActiveTransform = Matrix()
    private val mActiveTransformInverse = Matrix()
    private val mTempValues = FloatArray(9)
    private val mTempRect = RectF()
    private var mListener: ZoomableController.Listener? = null
    private var mIsEnabled = false

    override fun isEnabled(): Boolean {
        return mIsEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        mIsEnabled = enabled
    }

    private var isRotationEnabled = false
    /**
     * Gets/Sets whether the scale gesture is enabled or not.
     */
    private var isScaleEnabled = true
    /**
     * Gets/Sets whether the translations gesture is enabled or not.
     */
    private var isTranslationEnabled = true
    /**
     * Gets/Sets the minimum scale factor allowed.
     */
    var minScaleFactor = 1.0f
    /**
     * Gets/Sets the maximum scale factor allowed.
     */
    var maxScaleFactor = 2.0f
    private var mWasTransformCorrected: Boolean = false

    init {
        detector.setListener(this)
    }

    /**
     * Rests the controller.
     */
    open fun reset() {
        detector.reset()
        mPreviousTransform.reset()
        mActiveTransform.reset()
        onTransformChanged()
    }

    /**
     * Sets the zoomable listener.
     */
    override fun setListener(listener: ZoomableController.Listener?) {
        mListener = listener
    }

    /**
     * Gets the current scale factor.
     */
    override fun getScaleFactor(): Float {
        return getMatrixScaleFactor(mActiveTransform)
    }

    /**
     * Sets the image bounds, in view-absolute coordinates.
     */
    override fun setImageBounds(imageBounds: RectF) {
        if (imageBounds != this.imageBounds) {
            this.imageBounds.set(imageBounds)
            onTransformChanged()
        }
    }

    /**
     * Sets the view bounds.
     */
    override fun setViewBounds(viewBounds: RectF) {
        this.viewBounds.set(viewBounds)
    }

    /**
     * Returns true if the zoomable transform is identity matrix.
     */
    override fun isIdentity(): Boolean {
        return isMatrixIdentity(mActiveTransform, 1e-3f)
    }

    /**
     * Returns true if the transform was corrected during the last update.
     *
     *
     * We should rename this method to `wasTransformedWithoutCorrection` and just return the
     * internal flag directly. However, this requires interface change and negation of meaning.
     */
    override fun wasTransformCorrected(): Boolean {
        return mWasTransformCorrected
    }

    /**
     * Gets the matrix that transforms image-absolute coordinates to view-absolute coordinates.
     * The zoomable transformation is taken into account.
     *
     *
     * Internal matrix is exposed for performance reasons and is not to be modified by the callers.
     */
    override fun getTransform(): Matrix {
        return mActiveTransform
    }

    /**
     * Sets a new zoom transformation.
     */
    fun setTransform(newTransform: Matrix) {
        mActiveTransform.set(newTransform)
        onTransformChanged()
    }

    /**
     * Gets the matrix that transforms image-relative coordinates to view-absolute coordinates.
     * The zoomable transformation is taken into account.
     */
    fun getImageRelativeToViewAbsoluteTransform(outMatrix: Matrix) {
        outMatrix.setRectToRect(IDENTITY_RECT, transformedImageBounds, Matrix.ScaleToFit.FILL)
    }

    /**
     * Maps point from view-absolute to image-relative coordinates.
     * This takes into account the zoomable transformation.
     */
    fun mapViewToImage(viewPoint: PointF): PointF {
        val points = mTempValues
        points[0] = viewPoint.x
        points[1] = viewPoint.y
        mActiveTransform.invert(mActiveTransformInverse)
        mActiveTransformInverse.mapPoints(points, 0, points, 0, 1)
        mapAbsoluteToRelative(points, points, 1)
        return PointF(points[0], points[1])
    }

    /**
     * Maps point from image-relative to view-absolute coordinates.
     * This takes into account the zoomable transformation.
     */
    fun mapImageToView(imagePoint: PointF): PointF {
        val points = mTempValues
        points[0] = imagePoint.x
        points[1] = imagePoint.y
        mapRelativeToAbsolute(points, points, 1)
        mActiveTransform.mapPoints(points, 0, points, 0, 1)
        return PointF(points[0], points[1])
    }

    /**
     * Maps array of 2D points from view-absolute to image-relative coordinates.
     * This does NOT take into account the zoomable transformation.
     * Points are represented by a float array of [x0, y0, x1, y1, ...].
     *
     * @param destPoints destination array (may be the same as source array)
     * @param srcPoints  source array
     * @param numPoints  number of points to map
     */
    private fun mapAbsoluteToRelative(destPoints: FloatArray, srcPoints: FloatArray, numPoints: Int) {
        for (i in 0 until numPoints) {
            destPoints[i * 2 + 0] = (srcPoints[i * 2 + 0] - imageBounds.left) / imageBounds.width()
            destPoints[i * 2 + 1] = (srcPoints[i * 2 + 1] - imageBounds.top) / imageBounds.height()
        }
    }

    /**
     * Maps array of 2D points from image-relative to view-absolute coordinates.
     * This does NOT take into account the zoomable transformation.
     * Points are represented by float array of [x0, y0, x1, y1, ...].
     *
     * @param destPoints destination array (may be the same as source array)
     * @param srcPoints  source array
     * @param numPoints  number of points to map
     */
    private fun mapRelativeToAbsolute(destPoints: FloatArray, srcPoints: FloatArray, numPoints: Int) {
        for (i in 0 until numPoints) {
            destPoints[i * 2 + 0] = srcPoints[i * 2 + 0] * imageBounds.width() + imageBounds.left
            destPoints[i * 2 + 1] = srcPoints[i * 2 + 1] * imageBounds.height() + imageBounds.top
        }
    }

    /**
     * Zooms to the desired scale and positions the image so that the given image point corresponds
     * to the given view point.
     *
     * @param scale      desired scale, will be limited to {min, max} scale factor
     * @param imagePoint 2D point in image's relative coordinate system (i.e. 0 <= x, y <= 1)
     * @param viewPoint  2D point in view's absolute coordinate system
     */
    open fun zoomToPoint(scale: Float, imagePoint: PointF, viewPoint: PointF) {
        calculateZoomToPointTransform(mActiveTransform, scale, imagePoint, viewPoint, LIMIT_ALL)
        onTransformChanged()
    }

    /**
     * Calculates the zoom transformation that would zoom to the desired scale and position the image
     * so that the given image point corresponds to the given view point.
     *
     * @param outTransform the matrix to store the result to
     * @param scale        desired scale, will be limited to {min, max} scale factor
     * @param imagePoint   2D point in image's relative coordinate system (i.e. 0 <= x, y <= 1)
     * @param viewPoint    2D point in view's absolute coordinate system
     * @param limitFlags   whether to limit translation and/or scale.
     * @return whether or not the transform has been corrected due to limitation
     */
    protected fun calculateZoomToPointTransform(
        outTransform: Matrix,
        scale: Float,
        imagePoint: PointF,
        viewPoint: PointF,
        @LimitFlag limitFlags: Int): Boolean {
        val viewAbsolute = mTempValues
        viewAbsolute[0] = imagePoint.x
        viewAbsolute[1] = imagePoint.y
        mapRelativeToAbsolute(viewAbsolute, viewAbsolute, 1)
        val distanceX = viewPoint.x - viewAbsolute[0]
        val distanceY = viewPoint.y - viewAbsolute[1]
        var transformCorrected = false
        outTransform.setScale(scale, scale, viewAbsolute[0], viewAbsolute[1])
        transformCorrected = transformCorrected or limitScale(outTransform, viewAbsolute[0], viewAbsolute[1], limitFlags)
        outTransform.postTranslate(distanceX, distanceY)
        transformCorrected = transformCorrected or limitTranslation(outTransform, limitFlags)
        return transformCorrected
    }

    /**
     * Notifies controller of the received touch event.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mIsEnabled) {
            detector.onTouchEvent(event)
        } else false
    }

    /* TransformGestureDetector.Listener methods  */

    override fun onGestureBegin(detector: TransformGestureDetector) {
        mPreviousTransform.set(mActiveTransform)
        // We only received a touch down event so far, and so we don't know yet in which direction a
        // future move event will follow. Therefore, if we can't scroll in all directions, we have to
        // assume the worst case where the user tries to scroll out of edge, which would cause
        // transformation to be corrected.
        mWasTransformCorrected = !canScrollInAllDirection()
    }

    override fun onGestureUpdate(detector: TransformGestureDetector) {
        val transformCorrected = calculateGestureTransform(mActiveTransform, LIMIT_ALL)
        onTransformChanged()
        if (transformCorrected) {
            this.detector.restartGesture()
        }
        // A transformation happened, but was it without correction?
        mWasTransformCorrected = transformCorrected
    }

    override fun onGestureEnd(detector: TransformGestureDetector) {
    }

    /**
     * Calculates the zoom transformation based on the current gesture.
     *
     * @param outTransform the matrix to store the result to
     * @param limitTypes   whether to limit translation and/or scale.
     * @return whether or not the transform has been corrected due to limitation
     */
    private fun calculateGestureTransform(
        outTransform: Matrix,
        @LimitFlag limitTypes: Int): Boolean {
        val detector = this.detector
        var transformCorrected = false
        outTransform.set(mPreviousTransform)
        if (isRotationEnabled) {
            val angle = detector.rotation * (180 / Math.PI).toFloat()
            outTransform.postRotate(angle, detector.pivotX, detector.pivotY)
        }
        if (isScaleEnabled) {
            val scale = detector.scale
            outTransform.postScale(scale, scale, detector.pivotX, detector.pivotY)
        }
        transformCorrected = transformCorrected or limitScale(outTransform, detector.pivotX, detector.pivotY, limitTypes)
        if (isTranslationEnabled) {
            outTransform.postTranslate(detector.translationX, detector.translationY)
        }
        transformCorrected = transformCorrected or limitTranslation(outTransform, limitTypes)
        return transformCorrected
    }

    private fun onTransformChanged() {
        mActiveTransform.mapRect(transformedImageBounds, imageBounds)
        if (mListener != null && isEnabled()) {
            mListener!!.onTransformChanged(mActiveTransform)
        }
    }

    /**
     * Keeps the scaling factor within the specified limits.
     *
     * @param pivotX     x coordinate of the pivot point
     * @param pivotY     y coordinate of the pivot point
     * @param limitTypes whether to limit scale.
     * @return whether limiting has been applied or not
     */
    private fun limitScale(
        transform: Matrix,
        pivotX: Float,
        pivotY: Float,
        @LimitFlag limitTypes: Int): Boolean {
        if (!shouldLimit(limitTypes, LIMIT_SCALE)) {
            return false
        }
        val currentScale = getMatrixScaleFactor(transform)
        val targetScale = limit(currentScale, minScaleFactor, maxScaleFactor)
        if (targetScale != currentScale) {
            val scale = targetScale / currentScale
            transform.postScale(scale, scale, pivotX, pivotY)
            return true
        }
        return false
    }

    /**
     * Limits the translation so that there are no empty spaces on the sides if possible.
     *
     *
     *
     *  The image is attempted to be centered within the view bounds if the transformed image is
     * smaller. There will be no empty spaces within the view bounds if the transformed image is
     * bigger. This applies to each dimension (horizontal and vertical) independently.
     *
     * @param limitTypes whether to limit translation along the specific axis.
     * @return whether limiting has been applied or not
     */
    private fun limitTranslation(transform: Matrix, @LimitFlag limitTypes: Int): Boolean {
        if (!shouldLimit(limitTypes, LIMIT_TRANSLATION_X or LIMIT_TRANSLATION_Y)) {
            return false
        }
        val b = mTempRect
        b.set(imageBounds)
        transform.mapRect(b)
        val offsetLeft = if (shouldLimit(limitTypes, LIMIT_TRANSLATION_X))
            getOffset(b.left, b.right, viewBounds.left, viewBounds.right, imageBounds.centerX())
        else
            0f
        val offsetTop = if (shouldLimit(limitTypes, LIMIT_TRANSLATION_Y))
            getOffset(b.top, b.bottom, viewBounds.top, viewBounds.bottom, imageBounds.centerY())
        else
            0f
        if (offsetLeft != 0f || offsetTop != 0f) {
            transform.postTranslate(offsetLeft, offsetTop)
            return true
        }
        return false
    }

    /**
     * Returns the offset necessary to make sure that:
     * - the image is centered within the limit if the image is smaller than the limit
     * - there is no empty space on left/right if the image is bigger than the limit
     */
    private fun getOffset(
        imageStart: Float,
        imageEnd: Float,
        limitStart: Float,
        limitEnd: Float,
        limitCenter: Float): Float {
        val imageWidth = imageEnd - imageStart
        val limitWidth = limitEnd - limitStart
        val limitInnerWidth = Math.min(limitCenter - limitStart, limitEnd - limitCenter) * 2
        // center if smaller than limitInnerWidth
        if (imageWidth < limitInnerWidth) {
            return limitCenter - (imageEnd + imageStart) / 2
        }
        // to the edge if in between and limitCenter is not (limitLeft + limitRight) / 2
        if (imageWidth < limitWidth) {
            return if (limitCenter < (limitStart + limitEnd) / 2) {
                limitStart - imageStart
            } else {
                limitEnd - imageEnd
            }
        }
        // to the edge if larger than limitWidth and empty space visible
        if (imageStart > limitStart) {
            return limitStart - imageStart
        }
        return if (imageEnd < limitEnd) {
            limitEnd - imageEnd
        } else 0f
    }

    /**
     * Limits the value to the given min and max range.
     */
    private fun limit(value: Float, min: Float, max: Float): Float {
        return Math.min(Math.max(min, value), max)
    }

    /**
     * Gets the scale factor for the given matrix.
     * This method assumes the equal scaling factor for X and Y axis.
     */
    private fun getMatrixScaleFactor(transform: Matrix): Float {
        transform.getValues(mTempValues)
        return mTempValues[Matrix.MSCALE_X]
    }

    /**
     * Same as `Matrix.isIdentity()`, but with tolerance `eps`.
     */
    private fun isMatrixIdentity(transform: Matrix, eps: Float): Boolean {
        // Checks whether the given matrix is close enough to the identity matrix:
        //   1 0 0
        //   0 1 0
        //   0 0 1
        // Or equivalently to the zero matrix, after subtracting 1.0f from the diagonal elements:
        //   0 0 0
        //   0 0 0
        //   0 0 0
        transform.getValues(mTempValues)
        mTempValues[0] -= 1.0f // m00
        mTempValues[4] -= 1.0f // m11
        mTempValues[8] -= 1.0f // m22
        return (0..8).none { Math.abs(mTempValues[it]) > eps }
    }

    /**
     * Returns whether the scroll can happen in all directions. I.e. the image is not on any edge.
     */
    private fun canScrollInAllDirection(): Boolean {
        return transformedImageBounds.left < viewBounds.left - EPS &&
                transformedImageBounds.top < viewBounds.top - EPS &&
                transformedImageBounds.right > viewBounds.right + EPS &&
                transformedImageBounds.bottom > viewBounds.bottom + EPS
    }

    override fun computeHorizontalScrollRange(): Int {
        return transformedImageBounds.width().toInt()
    }

    override fun computeHorizontalScrollOffset(): Int {
        return (viewBounds.left - transformedImageBounds.left).toInt()
    }

    override fun computeHorizontalScrollExtent(): Int {
        return viewBounds.width().toInt()
    }

    override fun computeVerticalScrollRange(): Int {
        return transformedImageBounds.height().toInt()
    }

    override fun computeVerticalScrollOffset(): Int {
        return (viewBounds.top - transformedImageBounds.top).toInt()
    }

    override fun computeVerticalScrollExtent(): Int {
        return viewBounds.height().toInt()
    }

    @IntDef(flag = true, value = [LIMIT_NONE, LIMIT_TRANSLATION_X,
        LIMIT_TRANSLATION_Y, LIMIT_SCALE, LIMIT_ALL])
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    annotation class LimitFlag

    companion object {

        const val LIMIT_NONE = 0
        const val LIMIT_TRANSLATION_X = 1
        const val LIMIT_TRANSLATION_Y = 2
        const val LIMIT_SCALE = 4
        const val LIMIT_ALL = LIMIT_TRANSLATION_X or LIMIT_TRANSLATION_Y or LIMIT_SCALE
        private const val EPS = 1e-3f
        private val IDENTITY_RECT = RectF(0f, 0f, 1f, 1f)

        fun newInstance(): DefaultZoomableController {
            return DefaultZoomableController(TransformGestureDetector.newInstance())
        }

        /**
         * Checks whether the specified limit flag is present in the limits provided.
         *
         *
         *
         *  If the flag contains multiple flags together using a bitwise OR, this only checks that at
         * least one of the flags is included.
         *
         * @param limits the limits to apply
         * @param flag   the limit flag(s) to check for
         * @return true if the flag (or one of the flags) is included in the limits
         */
        private fun shouldLimit(@LimitFlag limits: Int, @LimitFlag flag: Int): Boolean {
            return limits and flag != LIMIT_NONE
        }
    }
}
