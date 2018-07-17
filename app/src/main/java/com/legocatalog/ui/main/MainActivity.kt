package com.legocatalog.ui.main

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.legocatalog.LegoCatalogApp
import com.legocatalog.R
import com.legocatalog.extensions.safeContext
import com.legocatalog.firebase.FirebaseAuthentication
import com.legocatalog.ui.set.NewSetActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as LegoCatalogApp).appComponent.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
        with(viewModel) { }

        authenticateWithGoogle()

        main_pager.adapter = SetListPageAdapter(this, supportFragmentManager)
    }

    private fun authenticateWithGoogle() {
        startActivityForResult(
                authentication.buildAuthIntent(),
                SIGN_IN_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            authentication.handleAuthResponse(
                    data,
                    resultCode,
                    { displayLoggedUser() },
                    { displayAuthError(it) })
        }
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