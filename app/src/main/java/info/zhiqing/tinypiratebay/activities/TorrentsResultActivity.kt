package info.zhiqing.tinypiratebay.activities

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import info.zhiqing.tinypiratebay.R
import info.zhiqing.tinypiratebay.adapters.TorrentsAdapter
import info.zhiqing.tinypiratebay.entities.TorrentsResponse
import info.zhiqing.tinypiratebay.services.TorrentsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TorrentsResultActivity : AppCompatActivity() {

    companion object {
        val EXTRA_URL = "info.zhiqing.tinypiratebay.TorrentsResultActivity.EXTRA_URL"
        val EXTRA_TITLE = "info.zhiqing.tinypiratebay.TorrentsResultActivity.EXTRA_TITLE"
    }

    var torrentsService: TorrentsService? = null
    var adapter: TorrentsAdapter? = null
    var endLessListener: EndLessRecyclerOnScrollListener? = null

    var isNetAvailable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_torrents_result)
        val title = intent.getStringExtra(EXTRA_TITLE)
        supportActionBar?.title = title

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val url = intent.getStringExtra(EXTRA_URL)

        val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        torrentsService = retrofit.create(TorrentsService::class.java)

        val listEmptyView = findViewById(R.id.empty_list_view)
        val listLoadingView = findViewById(R.id.loading_view)
        val listView = findViewById(R.id.list_view) as RecyclerView

        val layoutManager = LinearLayoutManager(this)
        listView.layoutManager = layoutManager

        listView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect?.set(0, 0, 0, 1)
            }
        })
        adapter = TorrentsAdapter(this)
        listView.adapter = adapter

        endLessListener = object : EndLessRecyclerOnScrollListener(layoutManager) {
            override fun onLoadMore(page: Int) {
                adapter?.isLoading = true
                adapter?.notifyDataSetChanged()
                fetchTorrents(page)
            }

        }
        listView.setOnScrollListener(endLessListener)


        initTorrents()

    }

    override fun onResume() {
        super.onResume()

        val snackBar = Snackbar.make(findViewById(R.id.list_view), "你的手机没有链接到互联网!", Snackbar.LENGTH_INDEFINITE)
                .setAction("设置", {
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                })

        val conManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conManager.activeNetworkInfo
        if((netInfo == null || !netInfo.isAvailable) && isNetAvailable) {
            isNetAvailable = false
            snackBar.show()
            showEmpty()
        }
        if(netInfo != null && netInfo.isAvailable && !isNetAvailable) {
            isNetAvailable = true
            snackBar.dismiss()
            showLoading()
            initTorrents()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun initTorrents() {
        fetchTorrents(0)
    }


    private fun fetchTorrents(page: Int) {
        val call = torrentsService?.getTorrents(page)
        call?.enqueue(object :Callback<TorrentsResponse>{
            override fun onFailure(call: Call<TorrentsResponse>?, t: Throwable?) {
                t?.printStackTrace()
            }

            override fun onResponse(call: Call<TorrentsResponse>?, response: Response<TorrentsResponse>?) {
                val torrents = response?.body()?.torrents
                if(endLessListener?.getCurrentPage() == 0 && (torrents == null || torrents?.size == 0)) {
                    showEmpty()
                    return
                }
                adapter?.isLoading = false
                adapter?.addTorrents(torrents)
                showList()
            }

        })
    }


    private fun showList() {
        val loadingView = findViewById(R.id.loading_view)
        loadingView.visibility = View.INVISIBLE
        val emptyView = findViewById(R.id.empty_list_view)
        emptyView.visibility = View.INVISIBLE
        findViewById(R.id.list_view).visibility = View.VISIBLE
    }

    private fun showEmpty() {
        findViewById(R.id.list_view).visibility = View.INVISIBLE
        val loadingView = findViewById(R.id.loading_view)
        loadingView.visibility = View.INVISIBLE
        val emptyView = findViewById(R.id.empty_list_view)
        emptyView.visibility = View.VISIBLE
    }

    private fun showLoading() {
        findViewById(R.id.empty_list_view).visibility = View.INVISIBLE
        findViewById(R.id.list_view).visibility = View.INVISIBLE
        findViewById(R.id.loading_view).visibility = View.VISIBLE
    }

}

abstract class EndLessRecyclerOnScrollListener(val layoutManager: LinearLayoutManager)
    : RecyclerView.OnScrollListener() {
    private var preTotal = 0
    private var isLoading = false
    private var visibleThreshold = 3
    private var firstVisibleItem = 0
    private var visibleItem = 0
    private var totalItem = 0
    private var currentPage = 0

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if(recyclerView != null) {
            visibleItem = recyclerView.childCount
        }

        totalItem = layoutManager.itemCount
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if(isLoading) {
            if(totalItem > preTotal) {
                isLoading = false
                preTotal = totalItem
            }
        }
        if(!isLoading && (totalItem - visibleItem) < (firstVisibleItem + visibleThreshold)) {
            currentPage++
            onLoadMore(currentPage)
            isLoading = true
        }
    }

    fun getCurrentPage(): Int {
        return currentPage
    }

    abstract fun onLoadMore(page: Int)
}



