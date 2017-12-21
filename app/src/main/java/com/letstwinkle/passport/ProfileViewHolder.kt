package com.letstwinkle.passport

import android.support.v7.widget.RecyclerView
import com.letstwinkle.passport.databinding.RvProfileBinding

class ProfileViewHolder : RecyclerView.ViewHolder {
    val binding: RvProfileBinding

    constructor(binding: RvProfileBinding) : super(binding.root) {
        this.binding = binding
    }
}