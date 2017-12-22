package com.letstwinkle.passport

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
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
    private lateinit var context: Context

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        context = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = RvProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val vh = ProfileViewHolder(binding)
        binding.name.setOnClickListener { view ->
            this.openProfile(vh.adapterPosition, getRef(vh.adapterPosition).toString())
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

    fun openProfile(position: Int, url: String) {
        val reference = 0
        val intent = Intent(context, ProfileActivity::class.java)
        intent.putExtra(IntentExtras.SnapshotURL, url)
        context.startActivity(intent)
    }
}