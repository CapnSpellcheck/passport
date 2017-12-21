package com.letstwinkle.passport

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.letstwinkle.passport.databinding.RvProfileBinding

class ProfileRecyclerAdapter(options: FirebaseRecyclerOptions<Profile>) :
    FirebaseRecyclerAdapter<Profile, ProfileViewHolder>(options)
{
    var invertOrder: Boolean = false
    set(value) {
        if (field != value) {
            field = value
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = RvProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val vh = ProfileViewHolder(binding)
        binding.name.setOnClickListener { view ->
            Toast.makeText(vh.binding.root.context, "Open profile", Toast.LENGTH_LONG).show()
            this.openProfile(vh.adapterPosition)
        }

        return vh
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int, model: Profile) {
        holder.binding.profile = model
    }

    override fun getItem(position: Int): Profile {
        if (invertOrder)
            return super.getItem(this.itemCount - position - 1)
        return super.getItem(position)
    }

    fun openProfile(position: Int) {
    }
}