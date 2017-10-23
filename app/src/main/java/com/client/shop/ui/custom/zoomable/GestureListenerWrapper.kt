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

import android.view.GestureDetector
import android.view.MotionEvent

/**
 * Wrapper for SimpleOnGestureListener as GestureDetector does not allow changing its listener.
 */
class GestureListenerWrapper : GestureDetector.SimpleOnGestureListener() {

    private var mDelegate: GestureDetector.SimpleOnGestureListener

    init {
        mDelegate = GestureDetector.SimpleOnGestureListener()
    }

    fun setListener(listener: GestureDetector.SimpleOnGestureListener) {
        mDelegate = listener
    }

    override fun onLongPress(e: MotionEvent) {
        mDelegate.onLongPress(e)
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        return mDelegate.onScroll(e1, e2, distanceX, distanceY)
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        return mDelegate.onFling(e1, e2, velocityX, velocityY)
    }

    override fun onShowPress(e: MotionEvent) {
        mDelegate.onShowPress(e)
    }

    override fun onDown(e: MotionEvent): Boolean {
        return mDelegate.onDown(e)
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        return mDelegate.onDoubleTap(e)
    }

    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
        return mDelegate.onDoubleTapEvent(e)
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        return mDelegate.onSingleTapConfirmed(e)
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return mDelegate.onSingleTapUp(e)
    }
}
