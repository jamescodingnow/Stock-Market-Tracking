# Stock-Market-Tracking
Modern Android Application with Jetpack Compose.

Stock Market Data Resource: https://www.alphavantage.co


1. Firstly created data/local/CompanyListingEntity to get server response with remote/StockApi.
2. Crated domain/model/CompanyListing data class to use server response in app.
3. However, we cannot use CompanyListingEntity in app pages or ViewModels. So, we need to have a converter or mapper.
4. data/mapper/CompanyMapper is a mapper for the converting between CompanyListingEntity and CompanyListing each other.
5. There is a database as StockDatabase.
6. Our database functions are in StockDao.