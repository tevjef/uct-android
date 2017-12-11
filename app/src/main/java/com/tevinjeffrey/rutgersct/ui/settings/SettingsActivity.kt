package com.tevinjeffrey.rutgersct.ui.settings

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.tevinjeffrey.rutgersct.BuildConfig
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.data.model.License
import com.tevinjeffrey.rutgersct.ui.utils.AppCompatPreferenceActivity
import com.tevinjeffrey.rutgersct.utils.PreferenceUtils
import com.tevinjeffrey.rutgersct.utils.Utils
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class SettingsActivity : AppCompatPreferenceActivity() {

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.base_preference)
    Utils.setWindowColor(ContextCompat.getColor(this, R.color.primary_dark), this)
    setToolbar()
    fragmentManager
        .beginTransaction()
        .replace(R.id.content_frame, SettingsFragment())
        .commit()
  }

  private fun setToolbar() {
    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)
    toolbar.setNavigationOnClickListener { onBackPressed() }

    val actionBar = supportActionBar

    if (actionBar != null) {
      actionBar.title = getString(R.string.settings)
      actionBar.setDisplayHomeAsUpEnabled(true)
      actionBar.setDisplayShowHomeEnabled(true)
    }
    toolbar.setTitleTextAppearance(this, R.style.ToolbarTitleStyle)
    toolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitleStyle_TextApperance)
  }

  class SettingsFragment : PreferenceFragment() {
    @Inject lateinit var mPreferenceUtils: PreferenceUtils

    private var interval: Int
      get() = mPreferenceUtils.syncInterval
      set(intervalIndex) {
        mPreferenceUtils.syncInterval = intervalIndex
      }

    private val parentActivity: SettingsActivity
      get() = activity as SettingsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      addPreferencesFromResource(R.xml.settings)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
      setupAboutPref()
      setupLicensesPref()

      return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
      val list = view!!.findViewById<View>(android.R.id.list) as ListView
      list.divider = ColorDrawable(Color.parseColor("#f5f5f5"))
      list.dividerHeight = parentActivity.resources.displayMetrics.density.toInt()

      super.onActivityCreated(savedInstanceState)
    }

    private fun setSummary(preference: Preference, summary: CharSequence) {
      preference.summary = summary
    }

    private fun setupAboutPref() {
      val about = findPreference("about")
      about.summary = "v" + BuildConfig.VERSION_NAME
      about.onPreferenceClickListener = Preference.OnPreferenceClickListener {
        val tv = TextView(parentActivity).apply {
          movementMethod = LinkMovementMethod()
          setLinkTextColor(ContextCompat.getColor(parentActivity, R.color.accent))
          textSize = 18f
          setTextColor(ContextCompat.getColor(parentActivity, R.color.secondary_text))
          text = Html.fromHtml("Designed and developed by <b> Tevin Jeffrey</b> <br><br>" +
              "<a href=\"http://tevinjeffrey.me/\">Website</a>"
              + "       <a href=\"mailto:tev.jeffrey@gmail.com\">Email</a>")
        }

        MaterialDialog.Builder(parentActivity)
            .title(resources.getString(R.string.application_name))
            .iconRes(R.mipmap.ic_launcher)
            .positiveColorRes(R.color.accent)
            .customView(tv, true)
            .positiveText("OK")
            .show()
        true
      }
    }

    private fun setupLicensesPref() {
      val licensePref = findPreference("licenses")

      licensePref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
        val moshi = Moshi.Builder().build()
        val inflater = LayoutInflater.from(parentActivity)
        try {
          val licenses = Utils.parseResource(parentActivity, R.raw.open_source_licenses)
          val type = Types.newParameterizedType(List::class.java, License::class.java)
          val adapter: JsonAdapter<List<License>> = moshi.adapter(type)
          val licenseList = adapter.fromJson(licenses).orEmpty()

          val linearLayout = LinearLayout(parentActivity)
          linearLayout.orientation = LinearLayout.VERTICAL
          linearLayout.layoutParams = LinearLayout.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.WRAP_CONTENT
          )

          for (license in licenseList) {
            val licenseView = inflater.inflate(R.layout.license, null)
            val name = licenseView.findViewById<TextView>(R.id.name)
            val author = licenseView.findViewById<TextView>(R.id.author)
            val content = licenseView.findViewById<TextView>(R.id.content)
            val openInBrowser = licenseView.findViewById<View>(R.id.open_in_browser)
            openInBrowser.setOnClickListener {
              Utils.openLink(parentActivity,
                  license.website.orEmpty())
            }
            name.text = license.name
            author.text = license.author
            content.text = license.content
            linearLayout.addView(licenseView)
          }

          MaterialDialog.Builder(parentActivity)
              .title("Open Source Licenses")
              .titleColor(ContextCompat.getColor(parentActivity, R.color.primary))
              .positiveText("Ok")
              .positiveColor(ContextCompat.getColor(parentActivity, R.color.primary))
              .customView(linearLayout, true)
              .show()
        } catch (e: IOException) {
          Timber.e(e)
        }
        true
      }
    }
  }
}
