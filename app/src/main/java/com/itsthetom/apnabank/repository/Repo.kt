package com.itsthetom.apnabank.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.itsthetom.apnabank.model.BankAccount
import com.itsthetom.apnabank.model.Transaction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


class Repo @Inject constructor(private val bankDao: BankDao?) {

    val bankCustmersAccountList: Flow<List<BankAccount>>? = bankDao?.getAllCustomersAccount()

     fun getCustomerAccountTransactions(accountNumber: Long): Flow<List<Transaction>>? {
        return bankDao?.getAccountTransaction(accountNumber)
    }


    suspend fun insertNewTransaction(transaction: Transaction) {
        bankDao?.insertNewTransaction(transaction)
    }

    fun getCustomerAccountDetail(accountNumber: Long):Flow<List<BankAccount>>?{
        return bankDao?.getCustomerAccountDetail(accountNumber)
    }

    suspend fun updateAccountData(bankAccount: BankAccount){
        bankDao?.updateAccount(bankAccount)
    }


}