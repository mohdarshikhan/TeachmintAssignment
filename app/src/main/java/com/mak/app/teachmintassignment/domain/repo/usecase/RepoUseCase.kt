package com.mak.app.teachmintassignment.domain.repo.usecase

import android.content.Context
import com.mak.app.teachmintassignment.data.ApiResult
import com.mak.app.teachmintassignment.data.model.ApiError
import com.mak.app.teachmintassignment.domain.repo.model.RepoListResponse
import com.mak.app.teachmintassignment.domain.repo.repository.RepoRepository
import com.mak.app.teachmintassignment.utils.NetworkUtility
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepoUseCase @Inject constructor(
    private val context: Context,
    private val repository: RepoRepository
) {
    /**
     * This will get call from home screen
     */
    operator fun invoke(token: String, q: String?, page: Int): Flow<ApiResult<RepoListResponse>> =
        flow {
            if (q.isNullOrBlank() || q.isEmpty()) {
                emit(
                    ApiResult.Error(
                        ApiError(
                            NetworkUtility.NetworkError.VALIDATION_ERROR.code,
                            "Please enter search query"
                        )
                    )
                )
            } else {
                repository.getRepoList(token, q, page).collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {
                            emit(ApiResult.Loading())
                        }

                        is ApiResult.Error -> {
                            emit(ApiResult.Error(result.error))
                        }

                        is ApiResult.Success -> {
                            emit(ApiResult.Success(result.data))
                        }
                    }
                }
            }
        }
}