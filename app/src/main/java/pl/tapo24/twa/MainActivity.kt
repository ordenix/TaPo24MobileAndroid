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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
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
import pl.tapo24.twa.databinding.ActivityMainBinding
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.module.FavouriteModule
import pl.tapo24.twa.module.InitializationModule
import pl.tapo24.twa.module.PremiumShopModule
import pl.tapo24.twa.updater.AssetUpdater
import pl.tapo24.twa.updater.CheckVersion
import pl.tapo24.twa.updater.DataBaseUpdater
import pl.tapo24.twa.useCase.checkList.GetCheckListAllTypeUseCase
import pl.tapo24.twa.useCase.checkList.GetCheckListDictionaryUseCase
import pl.tapo24.twa.useCase.checkList.GetCheckListMapUseCase
import pl.tapo24.twa.utils.CheckConnection
import pl.tapo24.twa.utils.IntentRouter
import pl.tapo24.twa.worker.MourningWorker
import pl.tapo24.twa.worker.ShowNotifyForTariffIconWorker
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
    lateinit var favouriteModule: FavouriteModule

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
            //
            val activity: Activity = this
            AssetUpdater(
                tapoDb,
                dataTapoDb,
                networkClient,
                applicationContext,
                supportFragmentManager,
                activity
            ).getAllData()

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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
            "ThemePref",
            Context.MODE_PRIVATE
        )
        val funeralFromShared: Boolean = sharedPreferences.getBoolean("funeralTheme", false)
        val themeName: String = sharedPreferences.getString("mainTheme", "default") ?: "default"
        val themeType = initializationModule.returnStyleTheme(funeralFromShared, themeName)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE
//        )
        theme.applyStyle(themeType.themeName, true)

        val workManager = WorkManager.getInstance(this)
        val constraints = Constraints.Builder()
            //.setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<MourningWorker>(5, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag("Mourning")
            .build()
        workManager.enqueueUniquePeriodicWork("Mourning", ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, workRequest)
        val constraints2 = Constraints.Builder()
            .build()
        val workRequest2 = PeriodicWorkRequestBuilder<ShowNotifyForTariffIconWorker>(10, TimeUnit.DAYS)
            .setConstraints(constraints2)
            .setInitialDelay(10, TimeUnit.DAYS)
            .addTag("ShowNotifyForTariffIcon")
            .build()
        workManager.enqueueUniquePeriodicWork("ShowNotifyForTariffIcon", ExistingPeriodicWorkPolicy.KEEP, workRequest2)
        val constraints3 = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest3 = PeriodicWorkRequestBuilder<UpdateWorker>(10, TimeUnit.HOURS)
            .setConstraints(constraints3)
            .addTag("Mourning")
            .build()

        workManager.enqueueUniquePeriodicWork("DataUpdate", ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, workRequest3)
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

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_road, R.id.nav_tariff, R.id.nav_law, R.id.nav_helpers
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

        initializationModule.getUid()
        initializationModule.getSetting()

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Pobieranie danych")
            .setMessage("Proszę czekać")
            // .setCancelable(false)
            .create()

        State.dialogDownloadMessage.observe(this, Observer {
            if (it.isNotEmpty()) {
                if (!supportFragmentManager.isStateSaved && !supportFragmentManager.isDestroyed && !activity.isFinishing && !activity.isDestroyed) {
                    if (dialog.isShowing) {
                        dialog.setMessage(it)
                    } else {
                        dialog.setMessage(it)
                        dialog.show()
                    }
                }
            } else {
                if (dialog.isShowing && !supportFragmentManager.isStateSaved && !supportFragmentManager.isDestroyed && !activity.isFinishing && !activity.isDestroyed) {
                    dialog.dismiss()
                }
            }
        })
        dataBaseUpdater.update()

        CheckVersion(tapoDb, networkClient, this).checkVersion()
        val dialogTypeDownloadData = MaterialAlertDialogBuilder(this)
            .setTitle("Wybór połączenia do aktualizacji danych")
            .setCancelable(false)
            .setMessage("Nasza aplikacja może pobierać duże ilości danych do aktualizacji zmian przepisów. Z racji, że widzisz ten komunikat to uruchamiasz aplikację po raz pierwszy :). Za chwilę zostaną pobrane wszystkie dane przepisów, grafik etc. - będzie to wynosić około 250MB. Aby zapewnić spójność z obowiązującymi przepisami aplikacja może pobierać w ciągu działania aktualizacje przepisów które nie powinny przekraczać 10MB. Pamiętaj, że wybierając dane sieci mogą zostać naliczone dodatkowe opłaty przez operatora w zależności od rodzaju taryfy połączenia")
            .setNegativeButton("Tylko WiFi") { dialog, which ->
                // Respond to negative button press
                MainScope().launch(Dispatchers.IO) {
                    async {
                        val setting: Setting = Setting("settingNetwork", "WiFi")
                        tapoDb.settingDb().insert(setting)
                    }.await()
                }
                State.networkType = "WiFi"
                AssetUpdater(
                    tapoDb,
                    dataTapoDb,
                    networkClient,
                    this,
                    this.supportFragmentManager,
                    activity
                ).getAllData()


            }
            .setPositiveButton("WiFi oraz dane sieci") { dialog, which ->
                // Respond to positive button press
                MainScope().launch(Dispatchers.IO) {
                    async {
                        val setting: Setting = Setting("settingNetwork", "All")
                        tapoDb.settingDb().insert(setting)
                    }.await()

                }
                State.networkType = "All"
                AssetUpdater(
                    tapoDb,
                    dataTapoDb,
                    networkClient,
                    this,
                    this.supportFragmentManager,
                    activity
                ).getAllData()
            }
        // Add customization options here


        State.internetStatus.value = CheckConnection().getConnectionType(applicationContext)
        if (State.isLogin.value == false && !State.isSessionRestored) {
            sessionProvider.restoreSession()
        }
        State.internetStatus.observe(this, Observer {
            if (it != NetworkTypes.None) {
                // to do execute offline stacks
                if (!State.isSessionConfirm && State.isLogin.value == true) {
                    sessionProvider.restoreSession()

                }
                if (State.isLogin.value == true) {
                    favouriteModule.synchronizeOnSessionCreatedOrInternetAvailable()
                }
            }
        })
        State.isLogin.observe(this, Observer {
            if (it && State.internetStatus.value != NetworkTypes.None) {
                favouriteModule.synchronizeOnSessionCreatedOrInternetAvailable()
            }
        })
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


        val context = this
        MainScope().launch(Dispatchers.IO) {
            var settingNetwork: Setting? = null
            async { settingNetwork = tapoDb.settingDb().getSettingByName("settingNetwork") }.await()
            if (settingNetwork == null) {
                withContext(Dispatchers.Main) {
                    dialogTypeDownloadData.show()
                }
            } else {
                State.networkType = settingNetwork!!.value
                // DOWNLOAD DATA
                AssetUpdater(
                    tapoDb,
                    dataTapoDb,
                    networkClient,
                    context,
                    context.supportFragmentManager,
                    activity
                ).getAllData()
            }
        }
        IntentRouter().route(intent, navController)
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
        State.isLogin.observe(this, Observer {
            if (it == true) {
                menuLogOut.isVisible = true
                menuLogin.isVisible = false
                menuShop.isVisible = true
            } else {
                menuLogOut.isVisible = false
                menuLogin.isVisible = true
                menuShop.isVisible = false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            // DataUpdater(tapoDb,dataTapoDb,networkClient,this, getCheckListDictionaryUseCase, getCheckListAllTypeUseCase, getCheckListMapUseCase).getData()
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.nav_settings)
        }
        if (item.itemId == R.id.action_info) {
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.nav_about_application)
        }
        if (item.itemId == R.id.action_shop) {
            val navController = findNavController(R.id.nav_host_fragment_content_main)
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
            val navController = findNavController(R.id.nav_host_fragment_content_main)
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