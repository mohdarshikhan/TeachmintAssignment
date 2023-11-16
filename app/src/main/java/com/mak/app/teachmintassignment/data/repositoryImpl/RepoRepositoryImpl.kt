package com.mak.app.teachmintassignment.data.repositoryImpl

import com.mak.app.teachmintassignment.data.ApiResult
import com.mak.app.teachmintassignment.data.remote.AppApis
import com.mak.app.teachmintassignment.domain.repo.model.RepoListResponse
import com.mak.app.teachmintassignment.domain.repo.repository.RepoRepository
import com.mak.app.teachmintassignment.utils.NetworkUtility
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val networkUtility: NetworkUtility,
    private val appApis: AppApis
) : RepoRepository {

    override fun getRepoList(token: String, q: String, page: Int): Flow<ApiResult<RepoListResponse>> =
        flow {
            networkUtility.safeApiCall { appApis.getRepoList(token, q, page, 10) }
                .collect { result -> emit(result) }
        }

}