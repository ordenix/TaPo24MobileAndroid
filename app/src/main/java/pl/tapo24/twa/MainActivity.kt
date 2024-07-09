package pl.tapo24.twa

import android.app.*
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import pl.tapo24.twa.data.*
import pl.tapo24.twa.data.survey.ResponseSurvey
import pl.tapo24.twa.databinding.ActivityMainBinding
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.module.FavouriteModule
import pl.tapo24.twa.module.InitializationModule
import pl.tapo24.twa.module.PremiumShopModule
import pl.tapo24.twa.ui.utils.SurveyDialog
import pl.tapo24.twa.updater.*
import pl.tapo24.twa.utils.CheckConnection
import pl.tapo24.twa.utils.IntentRouter
import pl.tapo24.twa.worker.UpdateWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@BindingMethods(
    value = [
        BindingMethod(
            type = ImageView::class,
            attribute = "app:srcCompat",
            method = "setImageDrawable"
        )
    ]
)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    @Inject
    lateinit var initializationModule: InitializationModule

    @Inject
    lateinit var sessionProvider: SessionProvider

    @Inject
    lateinit var tapoDb: TapoDb

    @Inject
    lateinit var dataTapoDb: DataTapoDb

    @Inject
    lateinit var networkClient: NetworkClient


    @Inject
    lateinit var premiumShopModule: PremiumShopModule

    @Inject
    lateinit var dataBaseUpdater: DataBaseUpdater

    @Inject
    lateinit var initPackageDownloader: InitPackageDownloader

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val dialogNotification = DialogNotificationRequest()

    private lateinit var sharedPreferences: SharedPreferences

    private var navController: NavController? = null

    private val listener = NavController.OnDestinationChangedListener { controller, destination, arguments ->

        val fireBase = Firebase.analytics

        val bundle = Bundle()
        val currentFragmentClassName = (controller.currentDestination as FragmentNavigator.Destination).className
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, destination.label.toString())
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, currentFragmentClassName)
        FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
        //fireBase.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)

        State.internetStatus.value = CheckConnection().getConnectionType(applicationContext)
        if (State.countChangeFragment > 10 && State.internetStatus.value != NetworkTypes.None) {
            State.countChangeFragment = 0
            //SessionProvider(tapoDb,networkClient).restoreSession()
            CheckVersion(tapoDb, networkClient, this).checkVersion()
        }
        State.countChangeFragment += 1
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // granted
                createChannels()
                initializationModule.saveInitNotification()
            } else {
                // not granted
                initializationModule.saveInitNotification()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
            "ThemePref",
            Context.MODE_PRIVATE
        )
        val funeralFromShared: Boolean = sharedPreferences.getBoolean("funeralTheme", false)
        val themeName: String = sharedPreferences.getString("mainTheme", "default") ?: "default"
        val fontName: String = sharedPreferences.getString("mainFont", "Itim") ?: "Itim"
        val themeType = initializationModule.returnStyleTheme(funeralFromShared, themeName)
        val fontType = initializationModule.returnFontStyle(fontName)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE
//        )
        theme.applyStyle(themeType.themeName, true)
        theme.applyStyle(fontType.themeName, true)



        val typed1 = TypedValue()
        val typed2 = TypedValue()
        theme.resolveAttribute(R.attr.tapoSecondColor, typed1, true)
        theme.resolveAttribute(R.attr.tapoBackgroundColor, typed2, true)
        State.colorSpan1 = typed1.data
        State.colorSpan2 = typed2.data

        // get theme
        val activity: Activity = this
        initializationModule.getThemeParameters()
        initializationModule.setThemeObserver(this)
        State.requireRestart.observe(this, Observer {
            if (it) {
                State.requireRestart.value = false
                activity.finish()
                activity.startActivity(Intent(activity, MainActivity::class.java))
                activity.finishAffinity()
            }
        })

        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.appBarMain.toolbar)
        setContentView(binding.root)
        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
        initializeNaveMenu(navView)
        // show admin panel
        val menu = navView.menu
        State.isAdmin.observe(this, Observer {
            val item  = menu.findItem(R.id.nav_admin)
            item.isVisible = it
            item.isChecked = false
            if (it) {
                menu.findItem(R.id.nav_home).isChecked = true
            }
        })

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_road, R.id.nav_tariff, R.id.nav_law, R.id.nav_helpers, R.id.nav_admin
            ), drawerLayout
        )
        setupActionBarWithNavController(navController!!, appBarConfiguration)
        navView.setupWithNavController(navController!!)
        drawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(view: View, v: Float) {}
            override fun onDrawerOpened(view: View) {}
            override fun onDrawerClosed(view: View) {
                if (!supportFragmentManager.isDestroyed) {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .commit();
                }
            }

            override fun onDrawerStateChanged(i: Int) {}
        })
        navView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.nav_tariff) {
                drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
                    override fun onDrawerOpened(drawerView: View) {}
                    override fun onDrawerStateChanged(newState: Int) {}
                    override fun onDrawerClosed(drawerView: View) {
                        navController?.navigate(it.itemId)
                        drawerLayout.removeDrawerListener(this)
                    }
                })
                drawerLayout.closeDrawer(GravityCompat.START)

            } else {
                navController?.navigate(it.itemId)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            true
        }


        //// START NOTIFICATION SECTION

        MainScope().launch(Dispatchers.IO) {
            var settingNotificationInitialize: Setting? = null
            val settingAllowForNotification: Setting? = null
            val settingAllowForChannelAdv: Setting? = null
            async {
                settingNotificationInitialize = tapoDb.settingDb().getSettingByName("NotificationInit")
            }.await()

            if (settingNotificationInitialize == null) {
                // show dialog
                withContext(Dispatchers.Main) {
                    if (!supportFragmentManager.isDestroyed && !activity.isFinishing && !activity.isDestroyed && !supportFragmentManager.isStateSaved) {
                        dialogNotification.show(supportFragmentManager, "dialog_Permissions")
                        dialogNotification.allowClick = {
                            dialogNotification.dismiss()
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                ActivityCompat.requestPermissions(
                                    activity, arrayOf(
                                        android.Manifest.permission.POST_NOTIFICATIONS
                                    ), 1123
                                )
                            } else {
                                createChannels()
                                initializationModule.saveInitNotification()
                            }
                        }
                        dialogNotification.skipClick = {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                                createChannels(true)
                            }
                            initializationModule.saveInitNotification()
                            dialogNotification.dismiss()
                        }
                    }
                }
            }
        }
        // END NOTIFICATION SECTION
        initializationModule.initializeDialogForDataUpdater(this, supportFragmentManager)
        CheckVersion(tapoDb, networkClient, this).checkVersion()
        // Add customization options here


        State.internetStatus.value = CheckConnection().getConnectionType(applicationContext)
        State.paymentId.observe(this, Observer {
            if (it.isNotEmpty()) {
                Purchases.configure(
                    PurchasesConfiguration.Builder(this, "goog_myXkyrkSAFApGeVWzHSEfmvDiiK")
                        .appUserID(it)
                        .build()
                )
                Purchases.logLevel = LogLevel.DEBUG
                State.revenuecatInitialize = true
                premiumShopModule.checkPermissionOnInit()
            }
        })

        State.showInfoGovernmentDialog.observe(this, Observer {
            if (it) {
                State.showInfoGovernmentDialog.value = false
                val infoDialog = MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("Uwaga")
                    .setMessage("Aplikacja TaPo24 nie reprezentuje żadnej instytucji rządowej." +
                            "\nDane zawarte w aplikacji pochodzą między innymi z oficjalnych domen rządowych." +
                            "\nPoniżej znajduje się wykaz domen, z których pochodzą niektóre dane:" +
                            "\nhttps://isap.sejm.gov.pl/" +
                            "\nhttps://www.ufg.pl/infoportal/faces/pages_home-page/Page_4d98135c_14e2b8ace27__7ff1/Pagee0e22f3_14efe6adc05__7ff1/Page4d024e07_14f0a824115__7ff6?_afrLoop=3753003479910681&_afrWindowMode=0&_adf.ctrl-state=182qsvy3xd_29" +
                            "\nhttps://moj.gov.pl/uslugi/engine/ng/index?xFormsAppName=UprawnieniaKierowcow&xFormsOrigin=EXTERNAL" +
                            "\nhttps://historiapojazdu.gov.pl/" +
                            "\nhttps://www.gov.pl/web/infrastruktura/ograniczenia-w-ruchu" +
                            "\nhttps://opendata.hsc.gov.ua/")
                    .setPositiveButton("OK") { dialog, which ->
                        // Respond to positive button press
                        MainScope().launch(Dispatchers.IO) {
                            val settingDialogGovernment = Setting("settingDialogGovernment", state = true)
                            tapoDb.settingDb().insert(settingDialogGovernment)
                        }
                        dialog.dismiss()
                    }
                    .show()
            }

        })

        IntentRouter().route(intent, navController)

        // survey
        MainScope().launch(Dispatchers.IO) {
            val responseSurvey = async { networkClient.getAllSurveyList() }.await()
            responseSurvey.onSuccess {
                try {
                    if (it.isNotEmpty()) {
                        val survey = it[0]
                        val dialogSurvey = SurveyDialog(survey)
                        dialogSurvey.show(supportFragmentManager, "dialogSurvey")
                        dialogSurvey.onRejectSurveyClick = {
                            dialogSurvey.dismiss()
                            networkClient.responseSurvey(ResponseSurvey(emptyList(), survey.id!!))
                        }
                        dialogSurvey.onNonSelectedAnswerSurveyClick = {
                            val infoDialog = MaterialAlertDialogBuilder(this@MainActivity)
                                .setTitle("Błąd")
                                .setMessage("Wybierz odpowiedź")
                                .setPositiveButton("OK") { dialog, which ->
                                    // Respond to positive button press
                                    dialog.dismiss()
                                }
                                .show()
                            //Snackbar.make(window.decorView.rootView, "Wybierz odpowiedź", Snackbar.LENGTH_LONG).show()
                        }
                        dialogSurvey.onSendSurveyClick = {el->
                            MainScope().launch(Dispatchers.IO) {
                                networkClient.responseSurvey(el)
                            }
                            dialogSurvey.dismiss()
                        }

                    }
                } catch (e: Exception) {
                    println(e)
                }


            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        IntentRouter().route(intent, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val menuLogin = menu.findItem(R.id.action_login)
        val menuLogOut = menu.findItem(R.id.action_logout)
        val menuShop = menu.findItem(R.id.action_shop)
        val menuEnterPromoCode = menu.findItem(R.id.action_enter_promo_code)
        menuEnterPromoCode.isVisible = false
        State.isLogin.observe(this, Observer {
            if (it == true) {
                menuLogOut.isVisible = true
                menuLogin.isVisible = false
                menuShop.isVisible = true
                State.isPremiumCodeActive.observe(this, Observer{premiumCodeActive->
                    if (premiumCodeActive && !State.premiumVersion) {
                        menuEnterPromoCode.isVisible = true
                    } else {
                        menuEnterPromoCode.isVisible = false
                    }
                })
            } else {
                menuLogOut.isVisible = false
                menuLogin.isVisible = true
                menuShop.isVisible = false
                menuEnterPromoCode.isVisible = false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        if (item.itemId == R.id.action_settings) {
            navController.navigate(R.id.nav_settings)
        }
        if (item.itemId == R.id.action_info) {
            navController.navigate(R.id.nav_about_application)
        }
        if (item.itemId == R.id.action_enter_promo_code) {
            navController.navigate(R.id.promoCodeFragment)
        }
        if (item.itemId == R.id.action_shop) {
            var shopAvailable = false
            MainScope().launch(Dispatchers.IO) {
                val checkShopStatus = async { networkClient.getShopStatus() }.await()
                checkShopStatus.onSuccess {
                    if (it.state) {
                        shopAvailable = true
                    }
                }
                withContext(Dispatchers.Main) {
                    if (shopAvailable && State.revenuecatInitialize && !State.beta) {
                        navController.navigate(R.id.nav_shop)
                    } else {
                        Snackbar.make(window.decorView.rootView, "Sklep jest niedostępny", Snackbar.LENGTH_LONG).show()
                    }
                }
            }

        }
        if (item.itemId == R.id.action_login) {
            navController.navigate(R.id.nav_login)
        }
        if (item.itemId == R.id.action_logout) {
            val dialogMaterialAlertDialogBuilder = MaterialAlertDialogBuilder(this)
                .setTitle("Wylogowanie")
                .setMessage("Czy na pewno chcesz się wylogować z serwisu?")
                .setNegativeButton("Nie") { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton("Tak") { dialog, which ->
                    // Respond to positive button press
                    sessionProvider.clearSession()
                    Snackbar.make(window.decorView.rootView, "Wylogowano z serwisu", Snackbar.LENGTH_LONG).show()
                }
                .show()
//            sessionProvider.clearSession()
//            Snackbar.make(window.decorView.rootView, "Wylogowano z serwisu", Snackbar.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.removeOnDestinationChangedListener(listener)
        super.onPause()
        if (State.dialogUpdater.isVisible && !supportFragmentManager.isStateSaved
            && !supportFragmentManager.isDestroyed && !this.isFinishing && !this.isDestroyed) {
            State.dialogUpdater.dismiss()
        }

    }

    private fun createChannels(block: Boolean = false) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = "Powiadomienia ogólne"
            val descriptionText =
                "W tym kanale są przesyłane powiadomienia dotyczące aplikacji i jej nowości w funkcjonowaniu"
            var importance = NotificationManager.IMPORTANCE_DEFAULT
            if (block) {
                importance = NotificationManager.IMPORTANCE_NONE
            }
            val mChannel = NotificationChannel("fcm_fallback_notification_channel", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        navController?.removeOnDestinationChangedListener(listener)
        if (State.dialogUpdater.isVisible && !supportFragmentManager.isStateSaved
            && !supportFragmentManager.isDestroyed && !this.isFinishing && !this.isDestroyed) {
            State.dialogUpdater.dismiss()
        }
    }

    private fun initializeNaveMenu(navView: NavigationView) {

        navView.getHeaderView(0).findViewById<ImageView>(R.id.imageViewInstagram).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/tapo24.pl/"))
            intent.setPackage("com.instagram.android")
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                val intent2 = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/tapo24.pl/"))
                startActivity(intent2)
            }
        }
        navView.getHeaderView(0).findViewById<ImageView>(R.id.imageViewFacebook).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/TaPo24/"))
            intent.setPackage("com.facebook.katana")
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                val intent2 = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/TaPo24/"))
                startActivity(intent2)
            }
        }
    }
}
// 781