package com.itsthetom.apnabank.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "BankAccounts")
data class BankAccount(
    @PrimaryKey val accountNumber:Long,
    val accountHolderName:String,
    val address:String,
    val phoneNumber:String,
    val aadharNumber:String,
    val panCardNumber:String,
    var currentBalance:Double
):Parcelable
