package com.mak.app.teachmintassignment.domain.repo.model

import com.google.gson.annotations.SerializedName

data class RepoListResponse(
    @SerializedName("total_count") var totalCount: Int? = null,
    @SerializedName("incomplete_results") var incompleteResults: Boolean? = null,
    @SerializedName("items") var items: ArrayList<Items> = arrayListOf()
)
