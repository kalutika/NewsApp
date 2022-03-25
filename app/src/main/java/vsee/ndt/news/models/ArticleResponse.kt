package vsee.ndt.news.models

import com.google.gson.annotations.SerializedName


data class ArticleResponse (
    val status: String,
    @SerializedName("totalResults")
    val total: String,
    @SerializedName("articles")
    val articleList: List<News>,
)