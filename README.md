
[![Build Status](https://travis-ci.org/rubygarage/shopapp-android.svg?branch=master)](https://travis-ci.org/rubygarage/shopapp-android)
[![codecov](https://codecov.io/gh/rubygarage/shopapp-android/branch/master/graph/badge.svg)](https://codecov.io/gh/rubygarage/shopapp-android)

# ShopApp for Android
ShopApp connects with popular ecommerce platforms like Shopify, Magento, BigCommerce, and WooCommerce to transfer them into a mobile app for iOS and Android.
So far, we’ve developed a [Shopify provider for Android](https://github.com/rubygarage/shopapp-shopify-android) and [Shopify provider for iOS](https://github.com/rubygarage/shopapp-shopify-ios).
Currently we’re working on adding more providers and extending the features list, so stay in touch with our updates. 

![](https://github.com/rubygarage/shopapp-android/blob/master/assets/shopapp-main-screen.gif)

## Installation
To install the dependencies required for an application build, ShopApp uses [Gradle](https://gradle.org). Connect one of the providers for an online store and install all the dependencies. As an example, let's use Shopify.

1. Add the following line to **app/build.gradle**:
```
implementation "com.github.rubygarage:shopapp-shopify-android:1.0.4"
```

Also, you'll have to configure the provider to get an access to your online store. To configure the provider, follow the instructions on a provider's page <link>. For a Shopify provider, the flow is following:

2. Add the following code: 
```
val api = ShopifyApi(this, "BASE DOMAIN", "STOREFRONT ACCESS TOKEN", "API KEY", "API PASSWORD") //Initialize your api here. 
appComponent = buildAppComponent(api, dao)
```
to the file **app/src/main/java/com/shopapp/ShopApplication.kt**. 

3. You'll also have to configure the provider to receive an access to your store. To get an access, follow the instructions on a provider's page. 

Check out how to configure the [Shopify provider](https://github.com/rubygarage/shopapp-shopify-android).

4. After that, you'll be able to open the project file and launch an app.

To create a client for another SaaS provider, you have to add the following dependency to the dependencies:
```
implementation 'com.github.rubygarage:shopapp-android:1.0.5'
```

5. Next, create a class that'll interact with the main application and implement the API interface. 

6. After that you'll be able to add a new client to the main application: <link>

# Requirements
* Android 4.4 (API 19) - a minimum supported version
* Android Studio for application build
* Gradle to install all the dependencies   

## License
The ShopApp for Android is licensed under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0)
***
<a href="https://rubygarage.org/"><img src="https://github.com/rubygarage/shopapp-shopify-ios/blob/master/assets/rubygarage.png?raw=true" alt="RubyGarage Logo" width="415" height="128"></a>

RubyGarage is a leading software development and consulting company in Eastern Europe. Our main expertise includes Ruby and Ruby on Rails, but we successfully employ other technologies to deliver the best results to our clients. [Check out our portfolio](https://rubygarage.org/portfolio) for even more exciting works!
