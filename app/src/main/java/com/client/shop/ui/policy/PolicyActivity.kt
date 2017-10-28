package com.client.shop.ui.policy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.client.shop.R
import com.domain.entity.Policy
import kotlinx.android.synthetic.main.activity_policy.*

class PolicyActivity : AppCompatActivity() {

    private lateinit var policy: Policy

    companion object {

        private const val EXTRA_POLICY = "EXTRA_POLICY"

        fun getStartIntent(context: Context, policy: Policy): Intent {
            val intent = Intent(context, PolicyActivity::class.java)
            intent.putExtra(PolicyActivity.EXTRA_POLICY, policy)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        policy = intent.getParcelableExtra(EXTRA_POLICY)
        title = policy.title
        body.text = policy.body
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            return when (item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}