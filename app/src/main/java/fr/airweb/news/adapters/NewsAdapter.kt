package fr.airweb.news.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import fr.airweb.news.R
import fr.airweb.news.models.News

class NewsAdapter(val newsList: ArrayList<News>,val con: Context,private val listener: ItemClickListener) : RecyclerView.Adapter<NewsAdapter.ViewHolder>(), Filterable
{

    private var newsSearchList: List<News> = newsList

    override fun onCreateViewHolder(viewG: ViewGroup, index: Int): ViewHolder {
        val v = LayoutInflater.from(viewG?.context).inflate(R.layout.news_list_layout, viewG, false)
        return ViewHolder(v);
    }

    init {
        this.newsSearchList = newsList
    }

    override fun getItemCount(): Int {
        return newsSearchList.size
    }
    override fun onBindViewHolder(bindViewG: ViewHolder, bindIndex: Int) {

        Glide.with(con)
            //.load(mImageUri) s
            .load(newsList[bindIndex].picture)
            .centerCrop()
            .placeholder(R.drawable.ic_error_outline_black_24dp)
            .error(R.drawable.ic_error_outline_black_24dp)
            .into(bindViewG.imageNews)

        bindViewG.titleNews?.text = newsSearchList[bindIndex].title
        bindViewG.descNews?.text = newsSearchList[bindIndex].content
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageNews:ImageView = itemView.findViewById(R.id.news_image)
        val titleNews:TextView = itemView.findViewById(R.id.head_line)
        val descNews:TextView = itemView.findViewById(R.id.description)

        init {
            itemView.setOnClickListener {
                listener.onItemClicked(newsSearchList!![adapterPosition])
            }
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    newsSearchList = newsList
                } else {
                    val filteredList = ArrayList<News>()
                    for (row in newsList) {
                        if (row.title!!.toLowerCase().contains(charString.toLowerCase()) || row.content!!.contains(charSequence)||row.type.equals(charSequence)) {
                            filteredList.add(row)
                        }
                    }
                    newsSearchList = filteredList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = newsSearchList
                return filterResults
            }
            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                newsSearchList = filterResults.values as ArrayList<News>
                notifyDataSetChanged()
            }
        }
    }

    interface ItemClickListener {
        fun onItemClicked(news: News)
    }
}