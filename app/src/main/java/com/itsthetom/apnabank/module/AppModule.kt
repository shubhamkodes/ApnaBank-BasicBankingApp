package com.itsthetom.apnabank.module

import android.content.Context
import com.itsthetom.apnabank.adapter.AllCustomerAccountAdapter
import com.itsthetom.apnabank.repository.BankDao
import com.itsthetom.apnabank.repository.BankDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun providesBankDao(@ApplicationContext context: Context): BankDao? {
        return BankDatabase.getRoomDatabase(context)?.BankDao()
    }



}