package com.letstwinkle.passport

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.AdapterView
import android.widget.Spinner
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class MainActivity : Activity() {
    private lateinit var rvAdapter: ProfileRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private var sortChangeListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            sort()
        }
    }
    private var filterChangeListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            filter()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById<RecyclerView>(R.id.recycler)
        val sortSpinner = findViewById<Spinner>(R.id.sortSpinner)
        sortSpinner.post {
            sortSpinner.onItemSelectedListener = sortChangeListener
        }
        sortSpinner.adapter = EnumAdapter(this, SortMode::class.java)
        val filterSpinner = findViewById<Spinner>(R.id.filterSpinner)
        filterSpinner.post {
            filterSpinner.onItemSelectedListener = filterChangeListener
        }
        filterSpinner.adapter = EnumAdapter(this, FilterMode::class.java)

        val query = FirebaseDatabase.getInstance().reference.child("users")
        updateAdapterWithQuery(query)
    }

    override fun onStart() {
        super.onStart()
        rvAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        rvAdapter.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_main, menu)
        return true
    }

    // Actions

    // NOTE: FirebaseDB queries don't support any sort of conjunction/compound condition
    // (e.g.: where gender==male order by name) -- since it is a pretty poor decision to do client side filtering
    // I restrict so only EITHER a sort OR a filter can be applied.
    // If the requirements were to be able to choose a sort and filter simultaneously (NOTE: the challenge
    // specs don't specify the details, it merely states 'should be able to'), I would not choose FbDB.
    // NOTE 2: Name sort is case sensitive, so 'a' and 'A' may be separated.

    fun sort() {
        val usersRef = FirebaseDatabase.getInstance().reference.child("users")
        val mode = findViewById<Spinner>(R.id.sortSpinner).selectedItem as SortMode
        val query = when (mode) {
            SortMode.NameAsc -> usersRef.orderByChild("name")
            SortMode.NameDesc -> usersRef.orderByChild("name")
            SortMode.AgeAsc -> usersRef.orderByChild("age")
            SortMode.AgeDesc -> usersRef.orderByChild("age")
            else -> usersRef
        }
        updateAdapterWithQuery(query)
        if (mode == SortMode.AgeDesc || mode == SortMode.NameDesc) {
            rvAdapter.invertOrder = true
        }
        findViewById<View>(R.id.filterSpinner).isEnabled = mode == SortMode.Default
    }

    fun startCreateProfile(menuItem: MenuItem) {
        val intent = Intent(this, CreateProfileActivity::class.java)
        startActivity(intent)
    }

    private fun filter() {
        val usersRef = FirebaseDatabase.getInstance().reference.child("users")
        val mode = findViewById<Spinner>(R.id.filterSpinner).selectedItem as FilterMode
        val query = when (mode) {
            FilterMode.Women -> usersRef.orderByChild("gender").equalTo(Profile.Gender.Female.toString())
            FilterMode.Men -> usersRef.orderByChild("gender").equalTo(Profile.Gender.Male.toString())
            FilterMode.All -> usersRef
        }
        updateAdapterWithQuery(query)
        findViewById<View>(R.id.sortSpinner).isEnabled = mode == FilterMode.All
    }

    // Internal

    private fun updateAdapterWithQuery(q: Query) {
        val adapterOpts = FirebaseRecyclerOptions.Builder<Profile>()
            .setQuery(q, Profile::class.java)
            .build()
        if (recyclerView.adapter != null) {
            rvAdapter.stopListening()
        }
        rvAdapter = ProfileRecyclerAdapter(adapterOpts)
        rvAdapter.startListening()
        recyclerView.adapter = rvAdapter
    }
}
