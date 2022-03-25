package vsee.ndt.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import vsee.ndt.news.databinding.RecyclerListItemBinding
import vsee.ndt.news.models.News
import vsee.ndt.news.ui.main.RecyclerListItemViewHolder
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter


class ArticleAdapter(private val onItemClickListener: (News) -> Unit) :
    RecyclerView.Adapter<RecyclerListItemViewHolder>() {

    var articleList = mutableListOf<News>()
    val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")

    fun setList(items: List<News>) {
        this.articleList = items.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = RecyclerListItemBinding.inflate(inflater, parent, false)
        return RecyclerListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerListItemViewHolder, position: Int) {
        val news = articleList[position]
        holder.binding.tvTitle.text = news.title
        holder.binding.tvDescription.text = news.description
        val formattedDate = formatter.format(parser.parse(news.publishedAt))
        holder.binding.tvTimestamp.text = formattedDate
        Picasso.get().load(news.urlToImage)
            .resize(100, 100)
            .centerCrop().into(holder.binding.ivImage)
        holder.itemView.setOnClickListener {
            onItemClickListener(news)
        }
    }

    override fun getItemCount(): Int {
        return this.articleList.size
    }

    fun clear() {
        articleList.clear()
        notifyDataSetChanged()
    }
}