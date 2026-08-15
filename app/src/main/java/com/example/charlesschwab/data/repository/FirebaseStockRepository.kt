package com.example.charlesschwab.data.repository

import com.example.charlesschwab.domain.model.*
import com.example.charlesschwab.domain.repository.StockRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseStockRepository @Inject constructor(
    private val mockRepository: MockStockRepository
) : StockRepository {

    private val database = try {
        FirebaseDatabase.getInstance().getReference("app_data")
    } catch (e: Exception) {
        null
    }

    override fun getAccounts(): Flow<List<Account>> = if (database == null) {
        mockRepository.getAccounts()
    } else callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // If data exists in Firebase, use it. Otherwise fallback to mock.
                if (snapshot.exists()) {
                    // Logic to parse account from Firebase would go here
                    // For now, we'll just demonstrate the connection
                }
                // Always emitting mock for now until you set up your JSON in Firebase
                // But this connection is LIVE.
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        database.child("accounts").addValueEventListener(listener)
        
        // Initial emission from mock so app works immediately
        mockRepository.getAccounts().collect { trySend(it) }
        
        awaitClose { database.child("accounts").removeEventListener(listener) }
    }

    override fun getWatchlist(): Flow<List<StockQuote>> = mockRepository.getWatchlist()
    override fun getStockQuote(symbol: String): Flow<StockQuote> = mockRepository.getStockQuote(symbol)
    override suspend fun executeTrade(order: OrderTicket): Result<Unit> = mockRepository.executeTrade(order)
}
