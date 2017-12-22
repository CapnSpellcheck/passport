package com.letstwinkle.passport

import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

object UIUtils {
    fun showOverlay(activity: Activity, @LayoutRes layout: Int): View {
        val parent = activity.findViewById<ViewGroup>(android.R.id.content)
        val overlay = activity.layoutInflater.inflate(layout, parent, false)
        overlay.setTag(R.id.overlayTag, true)
        overlay.isClickable = true
        overlay.isSoundEffectsEnabled = false
        parent.addView(overlay)
        return overlay
    }

    fun hideOverlay(activity: Activity): Boolean {
        val content = activity.findViewById<ViewGroup>(android.R.id.content)
        for (i in content.childCount - 1 downTo 0) {
            if (content.getChildAt(i).getTag(R.id.overlayTag) != null) {
                content.getChildAt(i).setTag(R.id.overlayTag, null)
                content.removeViewAt(i)
                return true
            }
        }
        return false
    }

    fun renderImage(data: ByteArray, imageView: ImageView) {
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        val bitmapDrawable = BitmapDrawable(imageView.context.resources, bitmap)
        imageView.setImageDrawable(bitmapDrawable)
    }
}