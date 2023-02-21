package com.stockmarkettracking.data.repository

import com.stockmarkettracking.data.csv.CSVParser
import com.stockmarkettracking.data.local.StockDatabase
import com.stockmarkettracking.data.mapper.toCompanyListing
import com.stockmarkettracking.data.mapper.toCompanyListingEntity
import com.stockmarkettracking.data.remote.StockApi
import com.stockmarkettracking.domain.model.CompanyListing
import com.stockmarkettracking.domain.repository.StockRepository
import com.stockmarkettracking.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val api: StockApi,
    val db: StockDatabase,
    val csvParser: CSVParser<CompanyListing>
): StockRepository {

    val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {

            // Start loading is true.
            emit(Resource.Loading(true))

            // First get company list from database.
            val localListing = dao.searchCompanyListing(query)

            // Emit database list as converting domain model.
            emit(Resource.Success(
                data = localListing.map { it.toCompanyListing() }
            ))

            val isDbEmpty = localListing.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListing = try {
                val response = api.getListings()
                csvParser.parse(response.byteStream())
            }
            catch (e: IOException) { // Parsing go wrong.
                e.printStackTrace()
                emit(Resource.Error("IOException, could not load data."))
                null
            }
            catch (e: HttpException) { // Invalid response.
                e.printStackTrace()
                emit(Resource.Error("HttpException, could not load data."))
                null
            }

            remoteListing?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )
                emit(Resource.Success(
                    data = dao.searchCompanyListing("").map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }
}














