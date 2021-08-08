package com.itsthetom.apnabank.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.itsthetom.apnabank.R
import com.itsthetom.apnabank.databinding.CardviewAccountBinding
import com.itsthetom.apnabank.model.BankAccount
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class AllCustomerAccountAdapter:
    ListAdapter<BankAccount,AllCustomerAccountAdapter.AccountViewHolder>(AccountDiffUtil )  {
    private lateinit var listener: AdapterListener


    private var accountsList:List<BankAccount>?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.cardview_account,parent,false);
        return AccountViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val customerAccount=getItem(position)
        holder.binding.tvBankAccountNo.text=holder.binding.root.context.getString(R.string.acc_no)+" "+customerAccount.accountNumber
        holder.binding.tvAccountHolderName.text=customerAccount.accountHolderName

        holder.binding.cardViewAccount.setOnClickListener{
            listener.onClickBankAccount(customerAccount)
        }

    }

    override fun submitList(list: List<BankAccount>?) {
        super.submitList(list)
        if (accountsList==null)
             accountsList=list?.toList()
    }

    inner class AccountViewHolder(view: View):RecyclerView.ViewHolder(view){
        val binding=CardviewAccountBinding.bind(view)
    }

    object AccountDiffUtil:DiffUtil.ItemCallback<BankAccount>(){
            override fun areItemsTheSame(oldItem: BankAccount, newItem: BankAccount): Boolean {
                return oldItem.accountNumber==newItem.accountNumber
            }

            override fun areContentsTheSame(oldItem: BankAccount, newItem: BankAccount): Boolean {
                return oldItem.accountNumber==newItem.accountNumber
            }
        }



    fun setAdapterListener(adapterListener: AdapterListener){
        this.listener=adapterListener
    }



   fun filter(query:String){
       var filteredAccountsList:MutableList<BankAccount> = mutableListOf()
        if (!query.isNullOrEmpty()){
            val trimmedQuery=query.lowercase().trim()
            filteredAccountsList.clear()
            filteredAccountsList.addAll(
                accountsList!!.filter {
                    it.accountHolderName.lowercase().contains(trimmedQuery) || it.accountNumber.toString().contains(trimmedQuery)
                }
            )
            submitList(filteredAccountsList)

        }else{
            submitList(accountsList)
        }
    }


}