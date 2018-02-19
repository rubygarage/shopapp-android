package com.shopify

import org.joda.time.DateTimeZone
import org.joda.time.tz.Provider
import org.joda.time.tz.UTCProvider
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class JodaTimeAndroidRule : TestRule {

    private val provider: Provider = UTCProvider()

    override fun apply(base: Statement, description: Description) = object : Statement() {
        override fun evaluate() {
            DateTimeZone.setProvider(provider)
            base.evaluate()
        }
    }
}