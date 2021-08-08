package com.itsthetom.apnabank.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.itsthetom.apnabank.model.BankAccount
import com.itsthetom.apnabank.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface BankDao {

    @Query("select * from bankaccounts;")
    fun getAllCustomersAccount():Flow<List<BankAccount>>

    @Query("select * from bankaccounts where accountNumber=:accountNo")
    fun getCustomerAccountDetail(accountNo: Long):Flow<List<BankAccount>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewCustomerAccount(account: BankAccount)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewTransaction(transaction:Transaction)

    @Query("select * from allTransactions where toAccount = :accountNumber OR fromAccount=:accountNumber;")
    fun getAccountTransaction(accountNumber:Long): Flow<List<Transaction>>

    @Update
    suspend fun updateAccount(bankAccount: BankAccount)
}