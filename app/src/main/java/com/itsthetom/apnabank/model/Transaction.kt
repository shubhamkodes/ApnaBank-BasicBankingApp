package com.itsthetom.apnabank.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "allTransactions")
data class Transaction(
    @PrimaryKey
    val transactionId:Long,
    val toAccount:Long,
    val toAccountHolderName:String,
    val fromAccount:Long,
    val fromAccountHolderName:String,
    val amount:Double
):Parcelable