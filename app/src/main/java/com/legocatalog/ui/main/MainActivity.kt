package com.legocatalog.ui.main

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.legocatalog.LegoCatalogApp
import com.legocatalog.R
import com.legocatalog.data.remote.firebase.FirebaseAuthentication
import com.legocatalog.ui.newset.NewSetActivity
import com.legocatalog.ui.partlist.PartListActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        val SIGN_IN_RESULT_CODE = 11
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var authentication: FirebaseAuthentication

    internal lateinit var viewModel: MainViewModel
    lateinit var tabAdapter: SetListPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as LegoCatalogApp).appComponent.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
        with(viewModel) { }

        authenticateWithGoogle()

        tabAdapter = SetListPageAdapter(this@MainActivity, supportFragmentManager)
        main_pager.adapter = tabAdapter
    }

    fun updateTitle(tabIndex: Int, itemsCount: Int) {
        tabAdapter.setCounter(tabIndex, itemsCount)
        val titleView =  main_pager_title_strip.getChildAt(tabIndex + 1)
        titleView?.let {
            if (it is TextView && !hasBracket(it)) {
                it.text = "${it.text} ($itemsCount)"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_all_parts -> openAllParts()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openAllParts() {
        startActivity(Intent(this, PartListActivity::class.java))
    }

    private fun hasBracket(it: TextView) = it.text.contains("(")

    private fun authenticateWithGoogle() {
        startActivityForResult(
                authentication.buildAuthIntent(),
                SIGN_IN_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SIGN_IN_RESULT_CODE -> handleAuthResponse(data, resultCode)
        }
    }

    private fun handleAuthResponse(data: Intent?, resultCode: Int) {
        authentication.handleAuthResponse(
                data,
                resultCode,
                { displayLoggedUser() },
                { displayAuthError(it) })
    }

    private fun displayLoggedUser() {
        val user = FirebaseAuth.getInstance().currentUser
        supportActionBar?.subtitle = user?.displayName
    }

    private fun displayAuthError(errorMessage: String) {
        Snackbar.make(add_button, errorMessage, Snackbar.LENGTH_LONG).show()
    }

    fun onAddSetClick(v: View) {
        startActivity(Intent(this, NewSetActivity::class.java))
    }
}