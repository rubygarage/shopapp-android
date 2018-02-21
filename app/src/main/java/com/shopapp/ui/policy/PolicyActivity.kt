package com.shopapp.ui.policy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.shopapp.gateway.entity.Policy
import com.shopapp.R
import kotlinx.android.synthetic.main.activity_policy.*

class PolicyActivity : AppCompatActivity() {

    private lateinit var policy: Policy

    companion object {

        const val EXTRA_POLICY = "EXTRA_POLICY"

        fun getStartIntent(context: Context, policy: Policy): Intent {
            val intent = Intent(context, PolicyActivity::class.java)
            intent.putExtra(EXTRA_POLICY, policy)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)

        setSupportActionBar(toolbar)
        policy = intent.getParcelableExtra(EXTRA_POLICY)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
        }

        policy = intent.getParcelableExtra(EXTRA_POLICY)
        toolbar.setTitle(policy.title)
        body.text = policy.body
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}