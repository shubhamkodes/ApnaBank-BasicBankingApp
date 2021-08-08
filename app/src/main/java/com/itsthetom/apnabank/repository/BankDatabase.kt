package com.itsthetom.apnabank.repository


import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.itsthetom.apnabank.model.BankAccount
import com.itsthetom.apnabank.model.Transaction
import kotlinx.coroutines.*
import java.util.*

@Database(entities = arrayOf(BankAccount::class,Transaction::class),version = 1,exportSchema = false)
abstract class BankDatabase: RoomDatabase() {

    abstract fun BankDao():BankDao

    companion object{
        @Volatile
        private var INSTANCE:BankDatabase?=null

         fun getRoomDatabase(context: Context): BankDatabase? {
            var instance= INSTANCE
            if (instance==null){
                synchronized(this){
                    instance= Room.databaseBuilder(
                                        context.applicationContext,
                                        BankDatabase::class.java,
                                        "bankdatabase")
                                    .addCallback(BankDatabaseCallBack(CoroutineScope(SupervisorJob())))
                                    .build()

                }
                INSTANCE=instance
            }
            return instance
        }

        // Initliazing dummy data
        private class BankDatabaseCallBack(val scope:CoroutineScope):RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val calendar=Calendar.getInstance()

                INSTANCE?.let {
                    scope.launch {
                        val id=getRandomAccountNo()
                        val id2=getRandomAccountNo()
                        it.BankDao().insertNewCustomerAccount(BankAccount(id,"Bhuvan Bam","219/3, Lakhmi Vihar, New Delhi, India","+919994001414","4321-8907-5432","BQPT099RT",998763.89))
                        it.BankDao().insertNewCustomerAccount(BankAccount(id2,"Ashish Chanchlani","424/3, Sarojni Vihar, New Delhi, India","+919809099014","3321-8967-5532","AGDFGKJ08",8763.89))
                        it.BankDao().insertNewTransaction(Transaction(calendar.timeInMillis,id2,"Ashish Chanchlani",id,"Bhuvan Bam",4342.42))



                        it.BankDao().insertNewCustomerAccount(BankAccount(getRandomAccountNo(),"Rahul Khanna","535/4, Gangoti Vihar, New Delhi, India","+919098450903","4321-8537-5282","BQPT099RT",998763.89))
                        it.BankDao().insertNewCustomerAccount(BankAccount(getRandomAccountNo(),"Vikas Puri","253/3, Vedpuri Vihar, New Delhi, India","+919839853985","4991-1407-9932","BQPT099RT",998763.89))
                        it.BankDao().insertNewCustomerAccount(BankAccount(getRandomAccountNo(),"Deepak Tijori","9453-5, Sunflower Colony, New Delhi, India","+918539853053","1321-4307-4332","BQPT099RT",998763.89))
                        it.BankDao().insertNewCustomerAccount(BankAccount(getRandomAccountNo(),"Brhamanand Rathore","33-B, Sunshine Park, New Delhi, India","+918953535030","5321-5227-0132","BQPT099RT",998763.89))
                        it.BankDao().insertNewCustomerAccount(BankAccount(getRandomAccountNo(),"Suraj Pancholi","843-AC, Kiriti Mehal, New Delhi, India","+919835598398","5921-8935-5394","BQPT099RT",998763.89))
                        it.BankDao().insertNewCustomerAccount(BankAccount(getRandomAccountNo(),"Inderjeet Chanchard","42/2, Chillumpur, New Delhi, India","+918935395395","5949-8357-5902","BQPT099RT",998763.89))
                        it.BankDao().insertNewCustomerAccount(BankAccount(getRandomAccountNo(),"Anjali Wafa","99/9, Gangtok Nagri, New Delhi, India","+919535309539","1492-8484-8342","BQPT099RT",998763.89))
                        it.BankDao().insertNewCustomerAccount(BankAccount(getRandomAccountNo(),"Nik Compan","69-CL, Mohab Vihar, New Delhi, India","+918593583589","4358-8947-4432","BQPT099RT",998763.89))
                        Log.d("TAG","Dummy data inserted into database")
                    }
                }
            }
            private suspend fun getRandomAccountNo():Long{
                return (1111111111111111..9999999999999999).random()
            }

        }

    }

}

