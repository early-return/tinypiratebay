package info.zhiqing.tinypiratebay.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import info.zhiqing.tinypiratebay.R
import info.zhiqing.tinypiratebay.activities.TorrentsResultActivity
import info.zhiqing.tinypiratebay.entities.Category
import info.zhiqing.tinypiratebay.entities.SubCategory
import info.zhiqing.tinypiratebay.functions.startBrowse

/**
 * Created by zhiqing on 17-6-14.
 */
class CategoryAdapter(val context: Context): RecyclerView.Adapter<CategoryViewHolder>() {

    val cates = listOf(
            Category("音频", "100", R.color.audioBack, listOf(
                    SubCategory("音乐", "101"),
                    SubCategory("有声读物", "102"),
                    SubCategory("声音片段", "103"),
                    SubCategory("FLAC", "104"),
                    SubCategory("其它", "199")
            )),
            Category("视频", "200", R.color.videoBack, listOf(
                    SubCategory("电影", "201"),
                    SubCategory("电影DVD", "202"),
                    SubCategory("MV", "203"),
                    SubCategory("电影片段", "204"),
                    SubCategory("电视节目", "205"),
                    SubCategory("手持设备", "206"),
                    SubCategory("高清电影", "207"),
                    SubCategory("高清电视", "208"),
                    SubCategory("3D", "209"),
                    SubCategory("其它", "299")
            )),
            Category("应用", "300", R.color.appBack, listOf(
                    SubCategory("Windows", "301"),
                    SubCategory("Mac", "302"),
                    SubCategory("Unix", "303"),
                    SubCategory("手持设备", "304"),
                    SubCategory("IOS", "305"),
                    SubCategory("Android", "306"),
                    SubCategory("其它系统", "399")
            )),
            Category("游戏", "400", R.color.gameBack, listOf(
                    SubCategory("PC", "401"),
                    SubCategory("Mac", "402"),
                    SubCategory("PSx", "403"),
                    SubCategory("XBOX", "404"),
                    SubCategory("Wii", "405"),
                    SubCategory("手持设备", "406"),
                    SubCategory("IOS", "407"),
                    SubCategory("Android", "408"),
                    SubCategory("其它", "499")
            )),
            /*Category("你懂的", "500", R.color.pornBack, listOf(
                    SubCategory("电影", "501"),
                    SubCategory("电影DVD", "502"),
                    SubCategory("图片", "503"),
                    SubCategory("游戏", "504"),
                    SubCategory("高清电影", "505"),
                    SubCategory("电影片段", "506"),
                    SubCategory("其它", "599")
            )),*/
            Category("其它", "600", R.color.otherBack, listOf(
                    SubCategory("电子书", "601"),
                    SubCategory("漫画", "602"),
                    SubCategory("图片", "603"),
                    SubCategory("封面", "604"),
                    SubCategory("物理学", "605"),
                    SubCategory("其它", "606")
            ))
    )

    override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item, p0, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder?, position: Int) {
        holder?.header?.setBackgroundColor(context.resources.getColor(cates[position].backColorId))
        holder?.title?.text = cates[position].title
        val layout = GridLayoutManager(context, 4)
        holder?.list?.layoutManager = layout
        holder?.list?.adapter = SubCateAdapter(context, cates[position].subCategory, cates[position].backColorId)

        holder?.item?.setOnClickListener{
            startBrowse(context, cates[position].code, cates[position].title)
        }
    }

    override fun getItemCount(): Int {
        return cates.size
    }

}

class CategoryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val item = view.findViewById(R.id.item_card) as CardView
    val header = view.findViewById(R.id.header)
    val title = view.findViewById(R.id.title) as TextView
    val list = view.findViewById(R.id.subCategoryListView) as RecyclerView

}

class SubCateAdapter(val context: Context, val subCateList: List<SubCategory>, val colorId: Int):
        RecyclerView.Adapter<SubCateViewHolder>() {

    override fun onBindViewHolder(holder: SubCateViewHolder?, position: Int) {
        holder?.button?.text = subCateList[position].title
        holder?.button?.setTextColor(context.resources.getColor(colorId))

        holder?.button?.setOnClickListener {
            startBrowse(context, subCateList[position].code, subCateList[position].title)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): SubCateViewHolder {
        return SubCateViewHolder(LayoutInflater.from(context).inflate(R.layout.sub_category_item, p0, false))
    }

    override fun getItemCount(): Int {
        return subCateList.size
    }

}


class SubCateViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val button = view.findViewById(R.id.button) as Button
}
