package com.stockmarkettracking.data.mapper

import com.stockmarkettracking.data.local.CompanyListingEntity
import com.stockmarkettracking.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name,
        symbol,
        exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name,
        symbol,
        exchange
    )
}