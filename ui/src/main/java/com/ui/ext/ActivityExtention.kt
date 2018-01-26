package com.ui.ext

import android.app.Activity
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar

fun Activity.registerKeyboardVisibilityListener(listener: KeyboardVisibilityEventListener): Unregistrar {
    return KeyboardVisibilityEvent.registerEventListener(this, listener)
}