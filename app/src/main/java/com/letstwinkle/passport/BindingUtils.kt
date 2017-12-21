package com.letstwinkle.passport;

import android.databinding.BindingAdapter
import android.databinding.InverseBindingListener
import android.databinding.adapters.AdapterViewBindingAdapter
import android.widget.Spinner

private val nothingSelectedListener = AdapterViewBindingAdapter.OnNothingSelected { }

// NOTE: This is an example from my own personal codebase.

@BindingAdapter("app:selectionEnum", "app:selectionChanged", "app:selectionAttrChanged", requireAll = false)
fun bindSpinnerEnum(spinner: Spinner,
                    newValue: Enum<*>?,
                    itemSelectedListener: AdapterViewBindingAdapter.OnItemSelected?,
                    changeListener: InverseBindingListener?) {
    val spinnerAdapter = spinner.adapter
    if (spinnerAdapter !is EnumAdapter<*>) {
        throw UnsupportedOperationException("app:selectionEnum attribute on Spinner requires EnumAdapter")
    }
    if (changeListener == null && itemSelectedListener == null)
        spinner.onItemSelectedListener = null
    else
        spinner.onItemSelectedListener = AdapterViewBindingAdapter.OnItemSelectedComponentListener(itemSelectedListener,
                                                                                                   nothingSelectedListener,
                                                                                                   changeListener)

    val isOptional = spinnerAdapter.isOptional
    if (!isOptional && newValue == null) {
        throw IllegalStateException("Can't set a null value for app:selectionEnum, adapter.isOptional is false")
    }
    if (spinner.selectedItem != newValue) {
        val sel = if (isOptional) spinnerAdapter.optionalPosition(newValue) else newValue!!.ordinal
        spinner.setSelection(sel)
    }
}

