package com.letstwinkle.passport

import android.app.*
import android.content.DialogInterface
import android.content.DialogInterface.*
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View

// NOTE: This is an excerpt from my personal codebase. The code may be terse.

open internal class SimpleDialogFragment() : DialogFragment(), DialogInterface.OnClickListener, DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {
    var ID: Int = -1
    @StringRes
    var title: Int? = null
    @StringRes
    var message:  Int? = null
    var vieww: View? = null
    var delegate: SimpleDialogDelegate? = null
    @StringRes
    var positiveLabel: Int? = null
    @StringRes
    var negativeLabel: Int? = null
    @StringRes
    var neutralLabel: Int? = null
    var listItems: Array<CharSequence>? = null
    var messageCS: CharSequence? = null // takes precedecnce
    var canceledOnTouchOutside = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(this.activity)
        title?.let {builder.setTitle(it)}
        message?.let {builder.setMessage(it)}
        messageCS?.let {builder.setMessage(it)}
        positiveLabel?.let {builder.setPositiveButton(it, this)}
        negativeLabel?.let {builder.setNegativeButton(it, this)}
        neutralLabel?.let {builder.setNeutralButton(it, this)}
        vieww?.let {builder.setView(it); vieww = null}
        listItems?.let {builder.setItems(listItems, this)}
        delegate?.onCreateDialog(builder)
        val dialog = builder.create()
        if (!canceledOnTouchOutside)
            dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        delegate?.simpleDialogClicked(this, which)
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        delegate?.simpleDialogCanceled(this)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        delegate?.simpleDialogDismissed(this)
    }

    override fun onStart() {
        super.onStart()
        delegate?.simpleDialogStarted(this)
    }
}

internal interface SimpleDialogDelegate {
    fun onCreateDialog(builder: AlertDialog.Builder) {}
    fun simpleDialogClicked(dlog: SimpleDialogFragment, which: Int) {}
    fun simpleDialogCanceled(dlog: SimpleDialogFragment) {}
    fun simpleDialogDismissed(dlog: SimpleDialogFragment) {}
    fun simpleDialogStarted(dlog: SimpleDialogFragment) {}
}
