package vsee.ndt.news.repositories


class MainRepository constructor(private val retrofitService: RetrofitService) {
    suspend fun getResponse() = retrofitService.getResponse()
}