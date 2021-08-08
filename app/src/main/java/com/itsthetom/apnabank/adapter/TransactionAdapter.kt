package com.itsthetom.apnabank.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.itsthetom.apnabank.R
import com.itsthetom.apnabank.databinding.LayoutTransactionBinding
import com.itsthetom.apnabank.model.Transaction
import com.itsthetom.apnabank.util.Utilities
import java.util.*

class TransactionAdapter(val customerAccountNumber:Long):ListAdapter<Transaction,TransactionAdapter.TransactionViewHolder>(TransactionDiffUtil()) {

    private val calendar=Calendar.getInstance()
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction=getItem(position)
        transaction?.let{
            if(transaction.toAccount==customerAccountNumber){
                    holder.setData(it.transactionId,
                                    it.fromAccountHolderName,
                                    holder.RECEIVED_AMT,
                                    it.amount)
            }else{
                holder.setData(it.transactionId,
                    it.toAccountHolderName,
                    holder.SENT_AMT,
                    it.amount)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.layout_transaction,parent,false);
        return TransactionViewHolder(view)
    }

    inner class TransactionViewHolder(view : View):RecyclerView.ViewHolder(view){
        val binding:LayoutTransactionBinding= LayoutTransactionBinding.bind(view)
        val SENT_AMT="SentAmount"
        val RECEIVED_AMT="ReceivedAmount"

        fun setData(timeInMillis:Long,accountName:String,exchange:String,amount:Double){
            binding.tvAccountHolderName.text=accountName

            calendar.timeInMillis=timeInMillis

            binding.tvTransactionDateTime.text=Utilities.getPrettyTime(binding.root.context,timeInMillis);

            when(exchange){
                SENT_AMT ->{
                    binding.tvTransactionAmount.text= "- "+amount
                    binding.tvTransactionAmount.setTextColor(binding.root.resources.getColor(R.color.red,null))
                }
                RECEIVED_AMT -> {
                    binding.tvTransactionAmount.text = "+ " + amount
                    binding.tvTransactionAmount.setTextColor(binding.root.resources.getColor(R.color.green,null))
                }
            }


        }
    }

    companion object{
        public class TransactionDiffUtil : DiffUtil.ItemCallback<Transaction>(){
            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
               return oldItem.transactionId==newItem.transactionId
            }

            override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem.transactionId==newItem.transactionId
            }
        }

    }

}