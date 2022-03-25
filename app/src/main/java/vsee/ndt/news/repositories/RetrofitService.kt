package vsee.ndt.news.repositories

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import vsee.ndt.news.models.ArticleResponse


interface RetrofitService {
    @GET("top-headlines?country=us&apiKey=$API_KEY")
    suspend fun getResponse() : Response<ArticleResponse>

    companion object {
        private const val URL = "https://newsapi.org/v2/"
        private const val API_KEY = "c31eee99753d4d97b56568e4c1a4186b"
        var retrofitService: RetrofitService? = null

        fun getInstance(): RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}