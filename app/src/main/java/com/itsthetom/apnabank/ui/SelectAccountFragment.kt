package com.itsthetom.apnabank.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.itsthetom.apnabank.R
import com.itsthetom.apnabank.adapter.AdapterListener
import com.itsthetom.apnabank.adapter.AllCustomerAccountAdapter
import com.itsthetom.apnabank.databinding.FragmentSelectAccountBinding
import com.itsthetom.apnabank.model.BankAccount
import com.itsthetom.apnabank.util.Constants
import com.itsthetom.apnabank.util.Utilities.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SelectAccountFragment : Fragment(),AdapterListener, SearchView.OnQueryTextListener{
    private lateinit var binding:FragmentSelectAccountBinding
    private lateinit var customerAccountAdapter: AllCustomerAccountAdapter
    private val viewModel: MainViewModel by viewModels()
    private lateinit var fromAccount: BankAccount
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding= FragmentSelectAccountBinding.inflate(inflater)

        fromAccount= arguments?.getParcelable<BankAccount>(Constants.FROM_ACCOUNT)!!

        customerAccountAdapter= AllCustomerAccountAdapter()
        customerAccountAdapter.setAdapterListener(this)
        binding.rvAllAcounts.adapter=customerAccountAdapter
        binding.rvAllAcounts.layoutManager=LinearLayoutManager(context)

        viewModel.customerAccountList?.observe(viewLifecycleOwner,{
            customerAccountAdapter.submitList(it)
        })


        binding.btnBack.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.searchView.setOnQueryTextListener(this)


        return binding.root
    }

    override fun onClickBankAccount(toAccount: BankAccount) {
            val bundle=Bundle();
            bundle.putParcelable(Constants.TO_ACCOUNT,toAccount)
            bundle.putParcelable(Constants.FROM_ACCOUNT,fromAccount)
            findNavController().navigate(R.id.action_selectAccountFragment_to_paymentGatewayFragment,bundle)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
                customerAccountAdapter.filter(newText)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        hideKeyboard(requireActivity())
    }


}