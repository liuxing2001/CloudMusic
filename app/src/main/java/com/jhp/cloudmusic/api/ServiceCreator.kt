package com.jhp.cloudmusic.api

import com.jhp.cloudmusic.BuildConfig
import com.jhp.cloudmusic.dao.UserInfoDao
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 创建Singleton Retrofit对象,对url追加参数
 * @author : jhp
 * @date : 2022-04-01 15:00
 */
object ServiceCreator {
    private var okHttpClient: OkHttpClient

    init {
        val client = OkHttpClient.Builder()
        client.addInterceptor(AppendUrlParamInterceptor())
        //在Debug模式下设置日志拦截器
        if (BuildConfig.DEBUG) {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            client.addInterceptor(logger)
        }
        okHttpClient = client.build()
    }

    //项目已经部署在vercel上
    private const val BASE_URL = "https://music.gspguoguo.top"

    // Retrofit构造器 生成Retrofit实例
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 对外提供的方法
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

}

/**
 * url追加参数
 * 追加时间戳,忽视接口缓存2分钟机制,解决301问题
 */
class AppendUrlParamInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //1.获取到请求
        val request = chain.request()
        //2.得到之前的url
        val builder = request.url.newBuilder()
        //3.追加信息时间戳,忽略缓存,但可能会触发 503 错误或者 ip 高频错误,目前还没遇到此问题
        val newUrl = builder
//            .addQueryParameter("cookie","MUSIC_R_T=1467388284904; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/weapi/clientlog;;MUSIC_R_T=1467388284904; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/openapi/clientlog;;MUSIC_U=058c7545d00cc9452293d1ac0dd68e75d730fe3dcfd8c3660e1da51cf5feb039993166e004087dd363d1eb07e3e9e5fce6ee7c75d2fd90e2fb82824c0a48689b47b770c72602ceb1a89fe7c55eac81f3; Max-Age=1296000; Expires=Wed, 25 May 2022 10:26:44 GMT; Path=/;;MUSIC_A_T=1467388266170; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/api/feedback;;MUSIC_R_T=1467388284904; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/api/clientlog;;MUSIC_A_T=1467388266170; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/eapi/clientlog;;MUSIC_R_T=1467388284904; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/neapi/feedback;;MUSIC_R_T=1467388284904; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/api/feedback;;__remember_me=true; Max-Age=1296000; Expires=Wed, 25 May 2022 10:26:44 GMT; Path=/;;MUSIC_A_T=1467388266170; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/neapi/feedback;;MUSIC_A_T=1467388266170; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/api/clientlog;;MUSIC_R_T=1467388284904; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/eapi/feedback;;MUSIC_A_T=1467388266170; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/weapi/clientlog;;MUSIC_R_T=1467388284904; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/wapi/feedback;;MUSIC_A_T=1467388266170; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/wapi/feedback;;MUSIC_A_T=1467388266170; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/neapi/clientlog;;MUSIC_R_T=1467388284904; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/weapi/feedback;;MUSIC_SNS=; Max-Age=0; Expires=Tue, 10 May 2022 10:26:44 GMT; Path=/;MUSIC_A_T=1467388266170; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/openapi/clientlog;;MUSIC_R_T=1467388284904; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/wapi/clientlog;;MUSIC_R_T=1467388284904; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/neapi/clientlog;;MUSIC_A_T=1467388266170; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/weapi/feedback;;MUSIC_R_T=1467388284904; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/eapi/clientlog;;MUSIC_A_T=1467388266170; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/wapi/clientlog;;MUSIC_A_T=1467388266170; Max-Age=2147483647; Expires=Sun, 28 May 2090 13:40:51 GMT; Path=/eapi/feedback;;__csrf=b025a8d0e59f98e82f4270edd2935bea; Max-Age=1296010; Expires=Wed, 25 May 2022 10:26:54 GMT; Path=/;")
//            .addQueryParameter("token", "058c7545d00cc9452293d1ac0dd68e75d730fe3dcfd8c3660e1da51cf5feb039993166e004087dd363d1eb07e3e9e5fce6ee7c75d2fd90e2fb82824c0a48689b47b770c72602ceb1a89fe7c55eac81f3")
            .addQueryParameter("cookie",UserInfoDao.getCookie())
            .addQueryParameter("token",UserInfoDao.getToken())
            .build()
        //4.新的url创建新的request
        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()
        //5.返回
        return chain.proceed(newRequest)
    }

}