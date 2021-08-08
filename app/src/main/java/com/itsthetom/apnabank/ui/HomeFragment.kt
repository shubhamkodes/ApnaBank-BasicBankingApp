package com.itsthetom.apnabank.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.itsthetom.apnabank.R
import com.itsthetom.apnabank.adapter.AdapterListener
import com.itsthetom.apnabank.adapter.AllCustomerAccountAdapter
import com.itsthetom.apnabank.databinding.FragmentHomeBinding
import com.itsthetom.apnabank.model.BankAccount
import com.itsthetom.apnabank.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), AdapterListener{
    private lateinit var binding:FragmentHomeBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter:AllCustomerAccountAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(inflater)

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= AllCustomerAccountAdapter()
        adapter.setAdapterListener(this)
        binding.rvAllAcounts.adapter=adapter
        binding.rvAllAcounts.layoutManager=LinearLayoutManager(context)

        viewModel.customerAccountList?.observe(viewLifecycleOwner,{
            if (it!=null){
                adapter.submitList(it)
            }
        })
    }

    override fun onClickBankAccount(account: BankAccount) {
        val bundle=Bundle()
        bundle.putParcelable(Constants.CUSTOMER_ACCOUNT,account)
        findNavController().navigate(R.id.action_homeFragment_to_customerDetailFragment,bundle)
    }

}