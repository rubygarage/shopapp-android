package com.shopapp.ui.custom.zoomable.gestures

import android.view.MotionEvent

/**
 * Component that detects and tracks multiple pointers based on touch events.
 *
 *
 * Each time a pointer gets pressed or released, the current gesture (if any) will end, and a new
 * one will be started (if there are still pressed pointers left). It is guaranteed that the number
 * of pointers within the single gesture will remain the same during the whole gesture.
 */
class MultiPointerGestureDetector {

    /** Gets whether there is a gesture in progress  */
    var isGestureInProgress: Boolean = false
        private set
    /** Gets the number of pointers in the current gesture  */
    var pointerCount: Int = 0
        private set
    /** Gets the number of pointers after the current gesture  */
    var newPointerCount: Int = 0
        private set
    private val mId = IntArray(MAX_POINTERS)
    /**
     * Gets the start X coordinates for the all pointers
     * Mutable array is exposed for performance reasons and is not to be modified by the callers.
     */
    val startX = FloatArray(MAX_POINTERS)
    /**
     * Gets the start Y coordinates for the all pointers
     * Mutable array is exposed for performance reasons and is not to be modified by the callers.
     */
    val startY = FloatArray(MAX_POINTERS)
    /**
     * Gets the current X coordinates for the all pointers
     * Mutable array is exposed for performance reasons and is not to be modified by the callers.
     */
    val currentX = FloatArray(MAX_POINTERS)
    /**
     * Gets the current Y coordinates for the all pointers
     * Mutable array is exposed for performance reasons and is not to be modified by the callers.
     */
    val currentY = FloatArray(MAX_POINTERS)

    private var mListener: Listener? = null

    /** The listener for receiving notifications when gestures occur.  */
    interface Listener {
        /** A callback called right before the gesture is about to start.  */
        fun onGestureBegin(detector: MultiPointerGestureDetector)

        /** A callback called each time the gesture gets updated.  */
        fun onGestureUpdate(detector: MultiPointerGestureDetector)

        /** A callback called right after the gesture has finished.  */
        fun onGestureEnd(detector: MultiPointerGestureDetector)
    }

    init {
        reset()
    }

    /**
     * Sets the listener.
     * @param listener listener to set
     */
    fun setListener(listener: Listener) {
        mListener = listener
    }

    /**
     * Resets the component to the initial state.
     */
    fun reset() {
        isGestureInProgress = false
        pointerCount = 0
        for (i in 0 until MAX_POINTERS) {
            mId[i] = MotionEvent.INVALID_POINTER_ID
        }
    }

    /**
     * This method can be overridden in order to perform threshold check or something similar.
     * @return whether or not to start a new gesture
     */
    protected fun shouldStartGesture(): Boolean {
        return true
    }

    /**
     * Starts a new gesture and calls the listener just before starting it.
     */
    private fun startGesture() {
        if (!isGestureInProgress) {
            if (mListener != null) {
                mListener!!.onGestureBegin(this)
            }
            isGestureInProgress = true
        }
    }

    /**
     * Stops the current gesture and calls the listener right after stopping it.
     */
    private fun stopGesture() {
        if (isGestureInProgress) {
            isGestureInProgress = false
            if (mListener != null) {
                mListener!!.onGestureEnd(this)
            }
        }
    }

    /**
     * Gets the index of the i-th pressed pointer.
     * Normally, the index will be equal to i, except in the case when the pointer is released.
     * @return index of the specified pointer or -1 if not found (i.e. not enough pointers are down)
     */
    private fun getPressedPointerIndex(event: MotionEvent, i: Int): Int {
        var indexPointer = i
        val count = event.pointerCount
        val action = event.actionMasked
        val index = event.actionIndex
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
            if (indexPointer >= index) {
                indexPointer++
            }
        }
        return if (indexPointer < count) indexPointer else -1
    }

    private fun updatePointersOnTap(event: MotionEvent) {
        pointerCount = 0
        for (i in 0 until MAX_POINTERS) {
            val index = getPressedPointerIndex(event, i)
            if (index == -1) {
                mId[i] = MotionEvent.INVALID_POINTER_ID
            } else {
                mId[i] = event.getPointerId(index)
                startX[i] = event.getX(index)
                currentX[i] = startX[i]
                startY[i] = event.getY(index)
                currentY[i] = startY[i]
                pointerCount++
            }
        }
    }

    private fun updatePointersOnMove(event: MotionEvent) {
        for (i in 0 until MAX_POINTERS) {
            val index = event.findPointerIndex(mId[i])
            if (index != -1) {
                currentX[i] = event.getX(index)
                currentY[i] = event.getY(index)
            }
        }
    }

    /**
     * Handles the given motion event.
     * @param event event to handle
     * @return whether or not the event was handled
     */
    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                // update pointers
                updatePointersOnMove(event)
                // start a new gesture if not already started
                if (!isGestureInProgress && pointerCount > 0 && shouldStartGesture()) {
                    startGesture()
                }
                // notify listener
                if (isGestureInProgress && mListener != null) {
                    mListener!!.onGestureUpdate(this)
                }
            }

            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_UP -> {
                // restart gesture whenever the number of pointers changes
                newPointerCount = getPressedPointerCount(event)
                stopGesture()
                updatePointersOnTap(event)
                if (pointerCount > 0 && shouldStartGesture()) {
                    startGesture()
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                newPointerCount = 0
                stopGesture()
                reset()
            }
        }
        return true
    }

    /** Restarts the current gesture (if any).   */
    fun restartGesture() {
        if (!isGestureInProgress) {
            return
        }
        stopGesture()
        for (i in 0 until MAX_POINTERS) {
            startX[i] = currentX[i]
            startY[i] = currentY[i]
        }
        startGesture()
    }

    companion object {

        private const val MAX_POINTERS = 2

        /** Factory method that creates a new instance of MultiPointerGestureDetector  */
        fun newInstance(): MultiPointerGestureDetector {
            return MultiPointerGestureDetector()
        }

        /**
         * Gets the number of pressed pointers (fingers down).
         */
        private fun getPressedPointerCount(event: MotionEvent): Int {
            var count = event.pointerCount
            val action = event.actionMasked
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
                count--
            }
            return count
        }
    }
}
