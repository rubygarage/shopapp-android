package com.client.shop.ui.auth.contract

import com.client.shop.R
import com.client.shop.ext.isEmailValid
import com.client.shop.ext.isPasswordValid
import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.domain.entity.Customer
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SignUpView : BaseMvpView<Customer> {

    fun showEmailError()

    fun showPasswordError()

    fun onCheckPassed()

    fun onFailure()
}

class SignUpPresenter @Inject constructor(repository: Repository) :
        BasePresenter<Customer, SignUpView>(repository) {

    fun signUp(firstName: String, lastName: String, email: String, password: String) {

        if (firstName.isEmpty() || lastName.isEmpty()) {
            view?.showMessage(R.string.empty_fields_error_message)
        } else if (!email.isEmailValid()) {
            view?.showEmailError()
        } else if (!password.isPasswordValid()) {
            view?.showPasswordError()
        } else {
            view?.onCheckPassed()
            repository.signUp(firstName, lastName, email, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { view?.showContent(it) },
                            {
                                view?.onFailure()
                                resolveError(it)
                            })
        }
    }
}