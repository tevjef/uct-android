package com.tevinjeffrey.rutgersct.dagger

import android.content.Context
import android.os.Build
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.moshi.Moshi
import com.tevinjeffrey.rmp.common.RMPModule
import com.tevinjeffrey.rutgersct.BuildConfig
import com.tevinjeffrey.rutgersct.RutgersCTApp
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.security.cert.CertificateException
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module(includes = [
  DataModule::class,
  RMPModule::class]
)
class RutgersAppModule(val app: RutgersCTApp) {

  @Provides
  fun providesMoshi(): Moshi {
    return RutgersAppModule.moshi
  }

  @Provides
  @PerApp
  fun provideFirebaseAnalytics(): FirebaseAnalytics {
    return FirebaseAnalytics.getInstance(app.applicationContext)
  }

  @Provides
  @PerApp
  fun provideFirebaseMessaging(): FirebaseMessaging {
    return FirebaseMessaging.getInstance()
  }

  @Provides
  fun providesOkHttpClient(builder: OkHttpClient.Builder): OkHttpClient {
    return builder.build()
  }

  @Provides
  fun providesOkHttpClientBuilder(userAgentInterceptor: UserAgentInterceptor): OkHttpClient.Builder {
    var client = OkHttpClient.Builder()

    if (BuildConfig.DEBUG) {
      client = unsafeOkHttpClient
      client.addNetworkInterceptor(StethoInterceptor())
    }

    client.addNetworkInterceptor(userAgentInterceptor)
    return client
  }

  @Provides
  fun providesPreferenceUtils(context: Context): PreferenceUtils {
    return PreferenceUtils(context)
  }

  @Provides
  fun providesUserAgentInterceptor(context: Context): UserAgentInterceptor {
    val str = context.getString(context.applicationInfo.labelRes)
    val sb = StringBuilder()
    sb.append(str)
        .append("/")
        .append(BuildConfig.APPLICATION_ID)
        .append(" ")
        .append("(")
        .append(BuildConfig.VERSION_NAME)
        .append("; Android ")
        .append(Build.VERSION.SDK_INT)
        .append(")")
    return UserAgentInterceptor(sb.toString())
  }

  @Provides internal fun provideContext(application: RutgersCTApp): Context {
    return application.applicationContext
  }

  /* This interceptor adds a custom User-Agent. */
  inner class UserAgentInterceptor(private val userAgent: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
      val originalRequest = chain.request()
      val requestWithUserAgent = originalRequest.newBuilder()
          .header("User-Agent", userAgent)
          .build()
      return chain.proceed(requestWithUserAgent)
    }
  }

  companion object {
    val moshi: Moshi by lazy {
      Moshi.Builder().build()
    }

    private val CONNECT_TIMEOUT_MILLIS: Long = 15000
    private val READ_TIMEOUT_MILLIS: Long = 20000

    private// Create a trust manager that does not validate certificate chains
        // Install the all-trusting trust manager
        // Create an ssl socket factory with our all-trusting manager
    val unsafeOkHttpClient: OkHttpClient.Builder
      get() {
        try {
          val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<java.security.cert.X509Certificate>,
                authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<java.security.cert.X509Certificate>,
                authType: String) {
            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
              return arrayOf()
            }
          })
          val sslContext = SSLContext.getInstance("SSL")
          sslContext.init(null, trustAllCerts, java.security.SecureRandom())
          val sslSocketFactory = sslContext.socketFactory

          val builder = OkHttpClient.Builder()
          builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
          builder.hostnameVerifier { _, _ -> true }

          val interceptor = HttpLoggingInterceptor()
          interceptor.level = HttpLoggingInterceptor.Level.HEADERS
          builder.addInterceptor(interceptor)

          return builder
        } catch (e: Exception) {
          throw RuntimeException(e)
        }
      }
  }
}
