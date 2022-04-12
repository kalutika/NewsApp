package vsee.ndt.news.repositories

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.fragment.app.FragmentActivity
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import vsee.ndt.news.models.ArticleResponse


interface RetrofitService {
    @GET("top-headlines?country=us&apiKey=$API_KEY")
    suspend fun getResponse(): Response<ArticleResponse>

    //https://newsapi.org/v2/top-headlines?q=2022&country=us&apiKey=c31eee99753d4d97b56568e4c1a4186b
    @GET("top-headlines")
    suspend fun getResponse(
        @Query("q") text: String,
        @Query("country") country: String = COUNTRY,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<ArticleResponse>

    companion object {
        private const val URL = "https://newsapi.org/v2/"
        private const val API_KEY = "c31eee99753d4d97b56568e4c1a4186b"
        private const val COUNTRY = "us"

        var retrofitService: RetrofitService? = null
        var okHttpClient: OkHttpClient? = null

        fun getInstance(context: FragmentActivity?): RetrofitService {
            if (context != null) {
                val cacheSize = (10 * 1024 * 1024).toLong()
                val myCache = Cache(context.cacheDir, cacheSize)

                var onlineInterceptor: Interceptor? = Interceptor { chain ->
                    val response = chain.proceed(chain.request())
                    // read cache: 60s, if there is internet connection
                    val maxAge = 60
                    response.newBuilder()
                        .header(
                            "Cache-Control",
                            "public, max-age=$maxAge"
                        )
                        .removeHeader("Pragma")
                        .build()
                }

                var offlineInterceptor = Interceptor { chain ->
                    var request: Request = chain.request()
                    if (!context?.let { hasNetwork(it) }!!) {
                        // Offline cache: 7 days
                        val maxStale = 60 * 60 * 24 * 7
                        request = request.newBuilder()
                            .header(
                                "Cache-Control",
                                "public, only-if-cached, max-stale=$maxStale"
                            )
                            .removeHeader("Pragma")
                            .build()
                    }
                    chain.proceed(request)
                }

                okHttpClient = OkHttpClient.Builder()
                    .cache(myCache)
                    .addInterceptor(offlineInterceptor)
                    .addNetworkInterceptor(onlineInterceptor)
                    .build()
            }

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

        fun hasNetwork(context: Context): Boolean? {
            var isConnected: Boolean? = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected)
                isConnected = true
            return isConnected
        }
    }
}