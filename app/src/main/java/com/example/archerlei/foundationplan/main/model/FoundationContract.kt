package com.example.archerlei.foundationplan.main.model

import android.provider.BaseColumns

class FoundationContract {


    open class FoundationItem : BaseColumns {
        companion object {
            const val TABLE_NAME = "foundation"
            const val _ID = "_id"
            const val COLUMN_NAME_FOUNDATION_DATA = "foundation_data"
        }
    }
}