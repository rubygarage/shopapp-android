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

import android.graphics.Matrix
import android.graphics.RectF
import android.view.MotionEvent

/**
 * Interface for implementing a controller that works with [ZoomableDraweeView]
 * to control the zoom.
 */
interface ZoomableController {

    /**
     * Listener interface.
     */
    interface Listener {

        /**
         * Notifies the view that the transform changed.
         *
         * @param transform the new matrix
         */
        fun onTransformChanged(transform: Matrix)
    }

    /**
     * Enables the controller. The controller is enabled when the image has been loaded.
     *
     * @param enabled whether to enable the controller
     */
    fun setEnabled(enabled: Boolean)

    /**
     * Gets whether the controller is enabled. This should return the last value passed to
     * [.setEnabled].
     *
     * @return whether the controller is enabled.
     */
    fun isEnabled(): Boolean

    /**
     * Sets the listener for the controller to call back when the matrix changes.
     *
     * @param listener the listener
     */
    fun setListener(listener: Listener?)

    /**
     * Gets the current scale factor. A convenience method for calculating the scale from the
     * transform.
     *
     * @return the current scale factor
     */
    fun getScaleFactor(): Float

    /**
     * Returns true if the zoomable transform is identity matrix, and the controller is idle.
     */
    fun isIdentity(): Boolean

    /**
     * Returns true if the transform was corrected during the last update.
     *
     * This mainly happens when a gesture would cause the image to get out of limits and the
     * transform gets corrected in order to prevent that.
     */
    fun wasTransformCorrected(): Boolean

    /**
     * See [android.support.v4.view.ScrollingView].
     */
    fun computeHorizontalScrollRange(): Int

    fun computeHorizontalScrollOffset(): Int
    fun computeHorizontalScrollExtent(): Int
    fun computeVerticalScrollRange(): Int
    fun computeVerticalScrollOffset(): Int
    fun computeVerticalScrollExtent(): Int

    /**
     * Gets the current transform.
     *
     * @return the transform
     */
    fun getTransform(): Matrix

    /**
     * Sets the bounds of the image post transform prior to application of the zoomable
     * transformation.
     *
     * @param imageBounds the bounds of the image
     */
    fun setImageBounds(imageBounds: RectF)

    /**
     * Sets the bounds of the view.
     *
     * @param viewBounds the bounds of the view
     */
    fun setViewBounds(viewBounds: RectF)

    /**
     * Allows the controller to handle a touch event.
     *
     * @param event the touch event
     * @return whether the controller handled the event
     */
    fun onTouchEvent(event: MotionEvent): Boolean
}
