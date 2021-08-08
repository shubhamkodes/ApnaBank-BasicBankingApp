package com.itsthetom.apnabank.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.itsthetom.apnabank.R
import com.itsthetom.apnabank.adapter.TransactionAdapter
import com.itsthetom.apnabank.databinding.FragmentCustomerDetailBinding
import com.itsthetom.apnabank.model.BankAccount
import com.itsthetom.apnabank.model.Transaction
import com.itsthetom.apnabank.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class CustomerDetailFragment : Fragment() {

    private lateinit var binding:FragmentCustomerDetailBinding;
    private val viewModel: MainViewModel by viewModels()
    private var bankAccount:BankAccount?=null
    private lateinit var transactionAdapter:TransactionAdapter
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentCustomerDetailBinding.inflate(inflater)
        bankAccount=arguments?.getParcelable(Constants.CUSTOMER_ACCOUNT)

        bankAccount?.let { it ->
            // Observing changes in Current Balance of Customer Account
            viewModel.getCustomerAccountDetail(it.accountNumber)?.observe(viewLifecycleOwner,{
                binding.tvCurrentBalance.text=ruppesCurrencyFormatter(it[0].currentBalance)
                binding.tvAccountHolderName.text=it[0].accountHolderName
                binding.tvBankAccountNo.text=getString(R.string.acc_no)+" "+it[0].accountNumber.toString()
            })

            // Observing all the recents transaction related to this customer account
            transactionAdapter= TransactionAdapter(it.accountNumber)
            binding.rvTransactions.adapter=transactionAdapter
            binding.rvTransactions.layoutManager=LinearLayoutManager(context)

            viewModel.getCustomerAccountTransaction(it.accountNumber)?.observe(viewLifecycleOwner,{list->
                list?.let {
                    val dateFilteredTransactions= ArrayList<Transaction>()
                    dateFilteredTransactions.addAll(it)
                    Collections.reverse(dateFilteredTransactions)
                    transactionAdapter.submitList(dateFilteredTransactions.toList())

                    if (it.size!=0)
                        binding.ivNoTransaction.visibility=View.GONE
                    else binding.ivNoTransaction.visibility=View.VISIBLE
                }

            })
        }

        binding.btnMakeTransaction.setOnClickListener{
            val bundle=Bundle()
            bundle.putParcelable(Constants.FROM_ACCOUNT,bankAccount)
            findNavController().navigate(R.id.action_customerDetailFragment_to_selectAccountFragment,bundle)
        }

        binding.btnBack.setOnClickListener{
            findNavController().popBackStack()
        }
        return binding.root
    }

    fun ruppesCurrencyFormatter( amount:Double):String{
        val format=NumberFormat.getCurrencyInstance(Locale("en", "in"))
        return format.format(amount)
    }
}