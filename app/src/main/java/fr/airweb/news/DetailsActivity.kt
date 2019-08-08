package fr.airweb.news

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class DetailsActivity : AppCompatActivity() {

    lateinit var imageNews: ImageView
    lateinit var titleNews: TextView
    lateinit var descNews: TextView
    lateinit var backGo: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        imageNews = findViewById(R.id.newsPhoto)
        titleNews = findViewById(R.id.newsTitle)
        descNews = findViewById(R.id.newsContent)
        backGo = findViewById(R.id.backGo)

        val bundle:Bundle = intent.extras
        titleNews.text = bundle.get("news_title").toString()
        descNews.text = bundle.get("news_content").toString()

        Glide.with(this)
            //.load(mImageUri) s
            .load(bundle.get("news_image").toString())
            .centerCrop()
            .placeholder(R.drawable.ic_error_outline_black_24dp)
            .error(R.drawable.ic_error_outline_black_24dp)
            .into(imageNews)

        backGo.setOnClickListener{
            onBackPressed()
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
