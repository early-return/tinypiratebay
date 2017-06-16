package info.zhiqing.tinypiratebay.functions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.design.widget.Snackbar
import android.widget.Toast
import info.zhiqing.tinypiratebay.activities.TorrentDetailActivity
import info.zhiqing.tinypiratebay.activities.TorrentsResultActivity

/**
 * Created by zhiqing on 17-6-15.
 */

val BASE_URL = "http://tpbapi.zhiqing.info"

fun startTorrentsResultActivity(context: Context, url: String, title: String) {
    val intent = Intent(context, TorrentsResultActivity::class.java)
    intent.putExtra(TorrentsResultActivity.EXTRA_URL, url)
    intent.putExtra(TorrentsResultActivity.EXTRA_TITLE, title)
    context.startActivity(intent)
}

fun startBrowse(context: Context, code: String, title: String) {
    val url = "$BASE_URL/top/$code/"
    startTorrentsResultActivity(context, url, title)

}

fun startSearch(context: Context, key: String) {
    val url = "$BASE_URL/search/$key/"
    startTorrentsResultActivity(context, url, "搜索：" + key)
}

fun startShowDetail(context: Context, code: String, title: String, backColorId: Int) {
    val intent = Intent(context, TorrentDetailActivity::class.java)
    intent.putExtra(TorrentDetailActivity.EXTRA_CODE, code)
    intent.putExtra(TorrentDetailActivity.EXTRA_TITLE, title)
    intent.putExtra(TorrentDetailActivity.EXTRA_BACK_COLOR_ID, backColorId)
    context.startActivity(intent)
}

fun openTorrent(context: Context, link: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    if(intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "你的手机上没有可以打开磁力链接的应用！", Toast.LENGTH_LONG).show()
    }
}