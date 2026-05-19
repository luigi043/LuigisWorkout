package com.luigisworkout.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A LiveData that delivers each value at most once per [setValue]/[postValue] call,
 * regardless of how many observers attach later.
 *
 * Use for one-shot side effects (finish activity, navigate, show toast) so they don't
 * replay on config change. Drop-in replacement for `MutableLiveData<Boolean>` patterns
 * where `true` means "go now".
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            // Mirror Google's SingleLiveEvent guidance — warn but allow.
            android.util.Log.w(
                "SingleLiveEvent",
                "Multiple observers registered but only one will be notified of changes."
            )
        }
        super.observe(owner) { value ->
            if (pending.compareAndSet(true, false)) observer.onChanged(value)
        }
    }

    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }

    override fun postValue(value: T) {
        pending.set(true)
        super.postValue(value)
    }

    /** Convenience for Unit/Boolean-style events. */
    fun fire() {
        @Suppress("UNCHECKED_CAST")
        value = (true as T)
    }
}
