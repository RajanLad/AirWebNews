package fr.airweb.news

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import fr.airweb.news.session_management.APIRelatedStuff
import fr.airweb.news.adapters.NewsAdapter
import fr.airweb.news.interfaces.AirWeb_Client
import fr.airweb.news.models.News
import io.realm.Realm
import io.realm.RealmConfiguration
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity(),NewsAdapter.ItemClickListener {

    lateinit var newsRecycler:RecyclerView
    lateinit var search_bar:EditText
    lateinit var option_menu:ImageView
    lateinit var airweb_client:AirWeb_Client
    lateinit var  rvAdapter:NewsAdapter

    lateinit var realm: Realm
    lateinit var config: RealmConfiguration

    lateinit var realmBasedRepo: java.util.ArrayList<News>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()

        initializeLibrarires()

        if(APIRelatedStuff.isInternetAvailable(this)) {
            hitAPI()
        }
        else
        {
            offlineAccess()
        }
        //fillTheList()
    }


    private fun offlineAccess() {

        newsRecycler.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        realmBasedRepo =
            realm.copyFromRealm(realm.where(News::class.java!!).findAll()) as java.util.ArrayList<News>

        if (!realmBasedRepo.isEmpty()) {
            for (j in realmBasedRepo.indices)
                Log.d("CRYMAN", realmBasedRepo[j].title + "")
        }

        rvAdapter = NewsAdapter(realmBasedRepo,this,this)

        newsRecycler.adapter = rvAdapter

        rvAdapter!!.notifyDataSetChanged()
    }


    fun initializeLibrarires()
    {
        fun retrofit() : Retrofit = Retrofit.Builder()
            .baseUrl(APIRelatedStuff.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        airweb_client = retrofit().create(AirWeb_Client::class.java)

        //Realm
        //Init Realm
        // Initialize Realm (just once per application)
        Realm.init(applicationContext)

        config = RealmConfiguration.Builder()
            .name("default2")
            .schemaVersion(3)
            .deleteRealmIfMigrationNeeded()
            .build()

        realm = Realm.getInstance(config)
    }

    fun initializeViews()
    {
        newsRecycler=findViewById(R.id.news_recycler_list)
        search_bar=findViewById(R.id.search_bar)
        option_menu=findViewById(R.id.options_icon)

        //
        var dataList = ArrayList<News>()

        rvAdapter = NewsAdapter(dataList,this,this)

        newsRecycler.adapter = rvAdapter;

        newsRecycler.setOnClickListener{

        }

        option_menu.setOnClickListener {
            when (it.id) {
                R.id.options_icon -> {
                    showPopup(it)
                }
            }
        }

        search_bar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                rvAdapter!!.filter.filter(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    private fun showPopup(view: View) {
        var popup: PopupMenu? = null;
        popup = PopupMenu(this, view)
        popup.inflate(R.menu.filter_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.tout -> {
                    rvAdapter!!.filter.filter("")
                }
                R.id.hot -> {
                    rvAdapter!!.filter.filter("hot")
                }
                R.id.news -> {
                    rvAdapter!!.filter.filter("news")
                }
                R.id.actualité -> {
                    rvAdapter!!.filter.filter("actualité")
                }
            }

            true
        })

        popup.show()
    }

    fun hitAPI()
    {
        val call = airweb_client.getNews()

        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {

                var jsonresponse:String =  response!!.body().toString()

                decodeToAdapter(jsonresponse)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }
        })
    }

    fun decodeToAdapter(jsonString:String)
    {
        newsRecycler.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        try {
            //getting the whole json object from the response
            val obj = JSONObject(jsonString)

            realm.beginTransaction()

                val retroModelArrayList = java.util.ArrayList<News>()
                val dataArray = obj.getJSONArray("news")

                for (i in 0 until dataArray.length()) {

                    val newsRetroModel = News()
                    val dataobj = dataArray.getJSONObject(i)

                    newsRetroModel.nid = dataobj.getString("nid")
                    newsRetroModel.type = dataobj.getString("type")
                    newsRetroModel.date = dataobj.getString("date")
                    newsRetroModel.title = dataobj.getString("title")
                    newsRetroModel.picture = dataobj.getString("picture")
                    newsRetroModel.content = dataobj.getString("content")
                    newsRetroModel.dateformated= dataobj.getString("dateformated")

                    retroModelArrayList.add(newsRetroModel)

                }

            realm.insertOrUpdate(retroModelArrayList)

            rvAdapter = NewsAdapter(retroModelArrayList,this,this)

            newsRecycler.adapter = rvAdapter;

            realm.commitTransaction()

            }
        catch (e:Exception)
        {

        }


    }

    override fun onItemClicked(news: News) {
        intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("news_title", news.title)
        intent.putExtra("news_image", news.picture)
        intent.putExtra("news_content", news.content)
        startActivity(intent)
    }
}
