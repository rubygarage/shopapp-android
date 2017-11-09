package com.client.shop.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.client.shop.R
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, AuthActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        signUpButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SignUpFragment())
                    .addToBackStack(null)
                    .commit()
        }
        signInButton.setOnClickListener {

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setTitle(R.string.app_name)
    }
}