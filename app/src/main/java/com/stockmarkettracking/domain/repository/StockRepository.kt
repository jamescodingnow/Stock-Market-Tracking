package com.stockmarkettracking.domain.repository

import com.stockmarkettracking.domain.model.CompanyListing
import com.stockmarkettracking.util.Resource
import kotlinx.coroutines.flow.Flow


interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>
}