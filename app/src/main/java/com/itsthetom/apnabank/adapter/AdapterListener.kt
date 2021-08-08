package com.itsthetom.apnabank.adapter

import com.itsthetom.apnabank.model.BankAccount

interface AdapterListener {
    fun onClickBankAccount(account:BankAccount)
}