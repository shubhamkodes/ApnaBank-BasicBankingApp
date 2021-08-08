package com.itsthetom.apnabank.ui

import androidx.lifecycle.*
import com.itsthetom.apnabank.model.BankAccount
import com.itsthetom.apnabank.model.Transaction
import com.itsthetom.apnabank.repository.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo:Repo):ViewModel() {


    val customerAccountList= repo.bankCustmersAccountList?.asLiveData()
    val paymentSession:MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        this.value = false
    }

    fun getCustomerAccountTransaction(accountNumber:Long):LiveData<List<Transaction>>?{
        return repo.getCustomerAccountTransactions(accountNumber)?.asLiveData()
    }

    fun makeNewTransaction(transaction: Transaction,toAccount: BankAccount,fromAccount: BankAccount){
       viewModelScope.launch {
           repo.insertNewTransaction(transaction)

           //Update balance in both account after transaction
           toAccount.currentBalance+=transaction.amount
           fromAccount.currentBalance-=transaction.amount

            updateAccountData(toAccount);
            updateAccountData(fromAccount);

       }
    }

    fun getCustomerAccountDetail(accountNumber: Long):LiveData<List<BankAccount>>?{
        return repo.getCustomerAccountDetail(accountNumber)?.asLiveData()
    }

   private suspend fun updateAccountData(bankAccount: BankAccount){
            repo.updateAccountData(bankAccount)
    }
}