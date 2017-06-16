package info.zhiqing.tinypiratebay.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import info.zhiqing.tinypiratebay.R
import info.zhiqing.tinypiratebay.entities.Torrent
import info.zhiqing.tinypiratebay.functions.startShowDetail

/**
 * Created by zhiqing on 17-6-15.
 */

class TorrentsAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEWTYPE_FOOTER = 2

    var isLoading = false


    private val torrents: MutableList<Torrent> = java.util.ArrayList<Torrent>()

    override fun onCreateViewHolder(root: ViewGroup?, type: Int):  RecyclerView.ViewHolder{
        val inflater = LayoutInflater.from(context)
        return when(type) {
            VIEWTYPE_FOOTER -> TorrentsFooterViewHolder(inflater.inflate(R.layout.torrents_item_footer, root, false))
            else -> TorrentsViewHolder(inflater.inflate(R.layout.torrents_item, root, false))
        }
    }

    override fun getItemCount(): Int {
        if(isLoading) {
            return torrents.size + 1
        } else {
            return torrents.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position == torrents.size) {
            return VIEWTYPE_FOOTER
        } else {
            return super.getItemViewType(position)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, p: Int) {
        when(getItemViewType(p)) {
            VIEWTYPE_FOOTER -> {}
            else -> {
                holder as TorrentsViewHolder
                holder?.title?.text = torrents[p].title
                holder?.info?.text = "${torrents[p].size}・${torrents[p].typeTitle}・⬆${torrents[p].seeders}・⬇${torrents[p].leechers}"

                holder?.item?.setOnClickListener {
                    startShowDetail(context, torrents[p].code, torrents[p].title, typeCodeToColorId(torrents[p].typeCode))
                }
            }
        }
    }

    private fun typeCodeToColorId(code: String): Int {
        return when(code.substring(0, 1)) {
            "1" -> R.color.audioBack
            "2" -> R.color.videoBack
            "3" -> R.color.appBack
            "4" -> R.color.gameBack
            "5" -> R.color.pornBack
            "6" -> R.color.otherBack
            else -> R.color.colorAccent
        }
    }

    fun addTorrents(torrents: MutableList<Torrent>?) {
        torrents?.forEach { torrent ->
            this.torrents.add(torrent)
        }
        this.notifyDataSetChanged()
    }

}

class TorrentsViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val item = view.findViewById(R.id.list_item)
    val title = view.findViewById(R.id.title_text) as TextView
    val info = view.findViewById(R.id.info_text) as TextView
}

class TorrentsFooterViewHolder(view: View): RecyclerView.ViewHolder(view)


