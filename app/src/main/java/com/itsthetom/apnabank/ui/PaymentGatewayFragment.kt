package com.itsthetom.apnabank.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.itsthetom.apnabank.R
import com.itsthetom.apnabank.databinding.FragmentPaymentGatewayBinding
import com.itsthetom.apnabank.databinding.LayoutPaymentSuccessfulBinding
import com.itsthetom.apnabank.model.BankAccount
import com.itsthetom.apnabank.model.Transaction
import com.itsthetom.apnabank.util.Constants
import com.itsthetom.apnabank.util.Utilities.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PaymentGatewayFragment : Fragment()  {
    private lateinit var binding:FragmentPaymentGatewayBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var successfullDialog:AlertDialog
    private var fromAccount: BankAccount? = null
    private var toAccount: BankAccount?=null
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPaymentGatewayBinding.inflate(inflater)

        fromAccount=arguments?.getParcelable<BankAccount>(Constants.FROM_ACCOUNT)
        toAccount=arguments?.getParcelable<BankAccount>(Constants.TO_ACCOUNT)

        fromAccount?.let {
            binding.tvFromAccountHolderName.text=it.accountHolderName
            binding.tvFromBankAccountNo.text= getString(R.string.acc_no)+" "+ it.accountNumber
        }

        toAccount?.let {
            binding.tvAccountHolderName.text=getString(R.string.sending_to)+" "+it.accountHolderName
            binding.tvBankAccountNo.text=getString(R.string.acc_no)+" "+it.accountNumber

        }


        initUi()
        //Checking payment status, if fragment is restored
        //isPaymentAlreadyMade()

        return binding.root
    }

    private fun initUi() {
        binding.btnMakeTransaction.setOnClickListener{
            startTransaction(toAccount,fromAccount)
        }

        binding.btnBack.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.edAmount.setOnEditorActionListener { v, actionId, event ->
            if (actionId==EditorInfo.IME_ACTION_DONE){
                binding.edAmount.clearFocus()
                hideKeyboard(requireActivity())
                startTransaction(toAccount,fromAccount)
            }
            true
        }
    }

    private fun startTransaction(toAccount: BankAccount?, fromAccount: BankAccount?) {
        val amount=binding.edAmount.text.toString().trim()

        if (amount.isEmpty()){
            binding.edAmount.error="Enter Amount"
        }else if (amount.toInt()==0){
            binding.edAmount.error="Enter Valid Amount!!"
        }else if(amount.toDouble()>fromAccount!!.currentBalance) {
            showMessage("Amount exceed current balance.","Cr Bal : "+fromAccount.currentBalance.toString())
        }
        else {
        // make the transaction and store data into database
            makeNewTransaction(toAccount,fromAccount,amount)
        }
    }

    private fun isPaymentAlreadyMade() {
        if(viewModel.paymentSession.value!!){
            showPaymentSuccessfulDialog()
        }
        viewModel.paymentSession.observe(viewLifecycleOwner,{
        // Payment is already made and successfully transacted for this session
            if(it){
                showPaymentSuccessfulDialog()
            }
        })
    }

    private fun makeNewTransaction(toAccount: BankAccount?, fromAccount: BankAccount?, amount: String) {
        val transaction=Transaction(System.currentTimeMillis(),
            toAccount!!.accountNumber,
            toAccount.accountHolderName,
            fromAccount!!.accountNumber,
            fromAccount.accountHolderName,
            amount.toDouble())

        viewModel.makeNewTransaction(transaction,toAccount,fromAccount)
        showPaymentSuccessfulDialog()

    }

    private fun showPaymentSuccessfulDialog() {
        binding.btnMakeTransaction.visibility=View.GONE
        context?.let{
            successfullDialog=AlertDialog.Builder(it,R.style.CustomDialog).create()
            val dialogBinding=LayoutPaymentSuccessfulBinding.inflate(layoutInflater,binding.root,false)
            successfullDialog.setView(dialogBinding.root)
            successfullDialog.show();

            dialogBinding.btnClose.setOnClickListener{
                successfullDialog.dismiss()
                val bundle=Bundle()
                bundle.putParcelable(Constants.CUSTOMER_ACCOUNT,fromAccount)
                findNavController().navigate(R.id.action_paymentGatewayFragment_to_customerDetailFragment,bundle)
            }

        }

        binding.tvAccountHolderName.text="Sent to "+toAccount?.accountHolderName
        binding.edAmount.isEnabled=false
        binding.animationView.visibility=View.VISIBLE
        binding.edAmount.setTextColor(resources.getColor(R.color.green,null))
    }

    private fun showMessage(message:String,balance: String){
        Snackbar.make(binding.root,message,Snackbar.LENGTH_LONG)
            .setAction(balance,null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.paymentSession.value=false
        hideKeyboard(requireActivity())
    }





}

