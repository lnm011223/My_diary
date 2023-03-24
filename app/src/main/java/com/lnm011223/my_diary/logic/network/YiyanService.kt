package com.lnm011223.my_diary.logic.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**

 * @Author liangnuoming
 * @Date 2023/3/25-02:31

 */
interface YiyanService {
    @GET("/")
    fun getYiyanData(@Query("c") category: String, @Query("encode") encodeType: String): Call<String>

}
