package com.rajit.foodies.data

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

// common repo for declaring data source (local/remote)
@ViewModelScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {

    val remote = remoteDataSource
    val local = localDataSource

}