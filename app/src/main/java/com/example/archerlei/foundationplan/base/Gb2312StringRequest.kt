package com.example.archerlei.foundationplan.base

import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


/**
 * @description todo
 * Created by archerlei on 2021/5/10.
 */

class Gb2312StringRequest(url: String, listener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest(url, listener, errorListener) {
    override fun parseNetworkResponse(response: NetworkResponse): Response<String> {

        var parsed: String? = null
        try {
            parsed = String(
                response.data,
                Charset.forName("gb2312")
            )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return Response.success(
            parsed,
            HttpHeaderParser.parseCacheHeaders(response)
        )


    }
}