package info.zhiqing.tinypiratebay.entities

import android.support.v7.widget.DialogTitle

/**
 * Created by zhiqing on 17-6-14.
 */

class Category(
        val title: String,
        val code: String,
        val backColorId: Int,
        val subCategory: List<SubCategory>
)