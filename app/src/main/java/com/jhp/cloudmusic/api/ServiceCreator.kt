package com.jhp.cloudmusic.api

import com.jhp.cloudmusic.BuildConfig
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
            .addQueryParameter("timestamp", "1503019930000")
            .addQueryParameter("realIP","116.25.146.177")
            .build()
        //4.新的url创建新的request
        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()
        //5.返回
        return chain.proceed(newRequest)
    }

}