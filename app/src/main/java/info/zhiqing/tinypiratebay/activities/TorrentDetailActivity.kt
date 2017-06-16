package info.zhiqing.tinypiratebay.activities

import android.content.ClipboardManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import info.zhiqing.tinypiratebay.R
import info.zhiqing.tinypiratebay.entities.TorrentDetail
import info.zhiqing.tinypiratebay.entities.TorrentDetailResponse
import info.zhiqing.tinypiratebay.functions.openTorrent
import info.zhiqing.tinypiratebay.services.TorrentDetailService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TorrentDetailActivity : AppCompatActivity() {

    companion object {
        val TAG = "TorrentDetailActivity"

        val EXTRA_TITLE = "info.zhiqing.TorrentDetailActivity.EXTRA_TITLE"
        val EXTRA_CODE = "info.zhiqing.TorrentDetailActivity.EXTRA_CODE"
        val EXTRA_BACK_COLOR_ID = "info.zhiqing.TorrentDetailActivity.EXTRA_BACK_COLOR_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_torrent_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val title = intent.getStringExtra(EXTRA_TITLE)
        supportActionBar?.title = title

        val code = intent.getStringExtra(EXTRA_CODE)
        initData(code)

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

    fun initData(code: String) {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://tpbapi.zhiqing.info/torrent/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(TorrentDetailService::class.java)
        val call = service.getDetail(code)
        call.enqueue(object :Callback<TorrentDetailResponse> {
            override fun onFailure(call: Call<TorrentDetailResponse>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<TorrentDetailResponse>?, response: Response<TorrentDetailResponse>?) {
                showDetail(response?.body()?.detail)
            }

        })
    }

    fun showInfo() {
        findViewById(R.id.loading_view).visibility = View.INVISIBLE
        findViewById(R.id.not_exist_view).visibility = View.INVISIBLE
        findViewById(R.id.info).visibility = View.VISIBLE
    }


    fun showNotExist() {
        findViewById(R.id.loading_view).visibility = View.INVISIBLE
        findViewById(R.id.info).visibility = View.INVISIBLE
        findViewById(R.id.not_exist_view).visibility = View.VISIBLE
    }

    fun showDetail(torrent: TorrentDetail?) {
        val loadingView = findViewById(R.id.loading_view)
        val notExistView = findViewById(R.id.not_exist_view)


        if(torrent == null || torrent.link.equals("")) {
            showNotExist()
            return
        }

        val infoHeader = findViewById(R.id.info_card_header)
        val infoTitle = findViewById(R.id.info_card_title) as TextView
        val infoTextView = findViewById(R.id.info_card_text) as TextView

        val linkHeader = findViewById(R.id.link_card_header)
        val linkTextView = findViewById(R.id.link_card_text) as TextView
        val copyButton = findViewById(R.id.copy_button) as Button
        val openButton = findViewById(R.id.open_button) as Button

        val introHeader = findViewById(R.id.intro_card_header)
        val introTextView = findViewById(R.id.intro_card_text) as TextView

        val backColor = intent.getIntExtra(EXTRA_BACK_COLOR_ID, R.color.colorAccent)
        infoHeader.setBackgroundColor(resources.getColor(backColor))
        linkHeader.setBackgroundColor(resources.getColor(backColor))
        introHeader.setBackgroundColor(resources.getColor(backColor))
        copyButton.setTextColor(resources.getColor(backColor))
        openButton.setTextColor(resources.getColor(backColor))

        infoTitle.text = supportActionBar?.title
        var infoText : String = ""
        torrent?.info?.forEach{ entry ->
            infoText += "${tranInfoKey(entry.key)} : ${entry.value} \n"
        }
        infoTextView.text = infoText

        linkTextView.text = torrent?.link.substring(0, torrent?.link.indexOf("&"))

        introTextView.text = torrent?.intro


        copyButton.setOnClickListener { view ->
            val clip = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clip.text = torrent.link
            Snackbar.make(view, "已复制到剪切板！", Snackbar.LENGTH_LONG).show()
        }

        openButton.setOnClickListener {
            openTorrent(this, torrent.link)
        }

        showInfo()
    }

    fun tranInfoKey(key: String): String {
        return when(key.toLowerCase()) {
            "comment" -> "评论数"
            "type" -> "类型"
            "uploaded" -> "上传时间"
            "size" -> "大小"
            "by" -> "上传者"
            "seeders" -> "做种数"
            "leechers" -> "下载数"
            "files" -> "文件数"
            "info" -> "信息"
            "spoken language(s)" -> "语言"
            "texted language(s)" -> "文字"
            else -> key
        }
    }
}
