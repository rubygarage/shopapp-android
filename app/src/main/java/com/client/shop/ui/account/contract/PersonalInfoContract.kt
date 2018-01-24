package com.client.shop.ui.account.contract

import com.domain.entity.Customer
import com.domain.interactor.account.EditCustomerUseCase
import com.domain.interactor.account.GetCustomerUseCase
import com.domain.validator.FieldValidator
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface PersonalInfoView : BaseLceView<Customer> {
    fun showEmailError()

    fun showUpdateProgress()

    fun hideUpdateProgress()

    fun onCustomerChanged(customer: Customer)
}

class PersonalInfoPresenter @Inject constructor(
    private val fieldValidator: FieldValidator,
    private val customerUseCase: GetCustomerUseCase,
    private val editCustomerUseCase: EditCustomerUseCase
) : BaseLcePresenter<Customer, PersonalInfoView>(customerUseCase, editCustomerUseCase) {

    fun getCustomer() {
        customerUseCase.execute(
            { view?.showContent(it) },
            { resolveError(it) },
            Unit
        )
    }

    fun editCustomer(name: String, lastName: String, email: String, phone: String) {
        var isError = false
        if (!fieldValidator.isEmailValid(email)) {
            isError = true
            view?.showEmailError()
        }

        if (!isError) {
            view?.showUpdateProgress()
            editCustomerUseCase.execute(
                {
                    view?.onCustomerChanged(it)
                    view?.hideUpdateProgress()
                },
                {
                    view?.hideUpdateProgress()
                    resolveError(it)
                },
                EditCustomerUseCase.Params(name, lastName, email, phone)
            )
        }

    }

}