package pl.tapo24.twa


import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import pl.tapo24.twa.data.EnginesType
import pl.tapo24.twa.data.EnvironmentType
import pl.tapo24.twa.data.State
import pl.tapo24.twa.databinding.ActivityMainBinding
import pl.tapo24.twa.db.TapoDb
import pl.tapo24.twa.db.entity.Setting
import pl.tapo24.twa.dbData.DataTapoDb
import pl.tapo24.twa.infrastructure.NetworkClient
import pl.tapo24.twa.updater.AssetUpdater
import pl.tapo24.twa.updater.CheckVersion
import pl.tapo24.twa.updater.DataUpdater
import pl.tapo24.twa.utils.CheckConnection
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
class MainActivity: AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

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

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    val dialogNotification = DialogNotificationRequest()

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
        if (State.countChangeFragment > 10 && State.internetStatus.value != 0) {
            State.countChangeFragment  = 0
            //SessionProvider(tapoDb,networkClient).restoreSession()
            CheckVersion(tapoDb,networkClient,this).checkVersion()
        // TODO: this comment is for test
        // AssetUpdater(tapoDb,dataTapoDb,networkClient,applicationContext, supportFragmentManager).getAllData()

        }
        State.countChangeFragment += 1
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // granted
                createChannels()
                saveInitNotification()
            } else {
                // not granted
                saveInitNotification()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
       // firebaseAnalytics = Firebase.analytics
//        firebaseAnalytics.setUserProperty("favorite_food2", "food")
//        firebaseAnalytics.setUserProperty("favorite_food", "food")
       // FirebaseDatabase.getInstance().setPersistenceEnabled(false);



        //// START NOTIFICATION SECTION
        val activity: Activity = this


        MainScope().launch(Dispatchers.IO) {
            var settingNotificationInitialize: Setting? = null
            val settingAllowForNotification: Setting? = null
            val settingAllowForChannelAdv: Setting? = null
            async {
                settingNotificationInitialize = tapoDb.settingDb().getSettingByName("NotificationInit")  }.await()

            if (settingNotificationInitialize == null) {
                // show dialog
                withContext(Dispatchers.Main) {
                    if (!supportFragmentManager.isDestroyed) {
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
                                saveInitNotification()
                            }
                        }
                        dialogNotification.skipClick = {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                                createChannels(true)
                            }
                            saveInitNotification()
                            dialogNotification.dismiss()
                        }
                    }

                }

            }
        }
        // END NOTIFICATION SECTION


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create the NotificationChannel.
//            val name = "TEST"
//            val descriptionText =" getString(R.string.channel_description)"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val mChannel = NotificationChannel("CHANNEL_ID", name, importance)
//            mChannel.description = descriptionText
//            // Register the channel with the system. You can't change the importance
//            // or other notification behaviors after this.
//            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(mChannel)
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create the NotificationChannel.
//            val name = "TEST2"
//            val descriptionText =" getString(R.string.channel_description)"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val mChannel = NotificationChannel("CHANNEL_ID2", name, importance)
//            mChannel.description = descriptionText
//            // Register the channel with the system. You can't change the importance
//            // or other notification behaviors after this.
//            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(mChannel)
//            val ss = notificationManager.notificationChannels
//            println(ss)
//        }

        //        ActivityCompat.requestPermissions(
//            this, arrayOf(
//                android.Manifest.permission.POST_NOTIFICATIONS
//            ), 112
//        )

//        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
//            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
//            putExtra(Settings.EXTRA_CHANNEL_ID, "fcm_fallback_notification_channel")
//        }
//        startActivity(intent)


        // init notification







//        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
//            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
//            putExtra(Settings.EXTRA_CHANNEL_ID, "CHANNEL_ID2")
//        }
//        startActivity(intent)

/// TOKEEN

//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//               // Log.w(TAG, "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
//            //Log.d(TAG, msg)
//            println(token)
//           // Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
//        })
//        firebaseAnalytics = Firebase.analytics
//        val bundle = Bundle()
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id")
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name")
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
//        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        //FirebaseInAppMessaging.getInstance().setAutomaticDataCollectionEnabled(true);
//        DataUpdater(tapoDb,dataTapoDb,networkClient,this).getPDF()
//        DataUpdater(tapoDb,dataTapoDb,networkClient,this).getGraphics()

        //FirebaseInAppMessaging.getInstance().on
        MainScope().launch(Dispatchers.IO) {
            var settingUid: Setting? = null
            async { settingUid = tapoDb.settingDb().getSettingByName("uid") }.await()

            if (settingUid == null) {
                async {
                    val response = networkClient.getUid()
                    response.onSuccess {
                        val setting = Setting("uid",it.uid!!)
                        tapoDb.settingDb().insert(setting)
                    }

                }.await()
            } else {
                State.uid = settingUid!!.value
            }
        }

        MainScope().launch(Dispatchers.IO) {
            var settingEnvironment : Setting? = null
            var settingEngine : Setting? = null
            async { settingEnvironment = tapoDb.settingDb().getSettingByName("settingEnvironment") }.await()
            async { settingEngine = tapoDb.settingDb().getSettingByName("settingEngine") }.await()

            if (settingEnvironment == null) {
                State.environmentType = EnvironmentType.Master
                async {
                    val setting: Setting = Setting("settingEnvironment","", EnvironmentType.Master.ordinal)
                    tapoDb.settingDb().insert(setting)
                }.await()
                State.environmentType = EnvironmentType.Master

            } else {
                State.environmentType = EnvironmentType.values()[settingEnvironment!!.count]
            }
            if (settingEngine == null) {
                State.enginesType = EnginesType.New
                async {
                    val setting: Setting = Setting("settingEngine","",EnginesType.New.ordinal)
                    tapoDb.settingDb().insert(setting)
                }.await()
                State.enginesType = EnginesType.New
            } else {
                State.enginesType = EnginesType.values()[settingEngine!!.count]
            }
        }

//        val settingUid = tapoDb.settingDb().getSettingByName("uid")
//        if (settingUid == null) {
//            MainScope().launch(Dispatchers.IO) {
//                networkClient.getUid()
//            }
//        }
//        val uid: Uid = Uid()




        DataUpdater(tapoDb,dataTapoDb,networkClient,this).getData()

        val dialogTypeDownloadData = MaterialAlertDialogBuilder(this)
            .setTitle("Wybór połączenia do aktualizacji danych")
            .setCancelable(false)
            .setMessage("Nasza aplikacja może pobierać duże ilości danych do aktualizacji zmian przepisów. Z racji, że widzisz ten komunikat to uruchamiasz aplikację po raz pierwszy :). Za chwilę zostaną pobrane wszystkie dane przepisów, grafik etc. - będzie to wynosić około 250MB. Aby zapewnić spójność z obowiązującymi przepisami aplikacja może pobierać w ciągu działania aktualizacje przepisów które nie powinny przekraczać 10MB. Pamiętaj, że wybierając dane sieci mogą zostać naliczone dodatkowe opłaty przez operatora w zależności od rodzaju taryfy połączenia")
            .setNegativeButton("Tylko WiFi") { dialog, which ->
                // Respond to negative button press
                MainScope().launch(Dispatchers.IO) {
                    async {
                        val setting: Setting = Setting("settingNetwork","WiFi")
                        tapoDb.settingDb().insert(setting)
                    }.await()
                }
                State.networkType = "WiFi"
                AssetUpdater(tapoDb,dataTapoDb,networkClient,this, this.supportFragmentManager, activity).getAllData()


            }
            .setPositiveButton("WiFi oraz dane sieci") { dialog, which ->
                // Respond to positive button press
                MainScope().launch(Dispatchers.IO) {
                    async {
                        val setting: Setting = Setting("settingNetwork","All")
                        tapoDb.settingDb().insert(setting)
                    }.await()

                }
                State.networkType = "All"
                AssetUpdater(tapoDb,dataTapoDb,networkClient,this, this.supportFragmentManager, activity).getAllData()
            }
            // Add customization options here


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        State.internetStatus.value = CheckConnection().getConnectionType(applicationContext)
        if (State.isLogin.value == false && !State.isSessionRestored) {
            sessionProvider.restoreSession()
        }
        State.internetStatus.observe(this, Observer {
            if (it != 0 ) {
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
            if (it && State.internetStatus.value != 0) {
                favouriteModule.synchronizeOnSessionCreatedOrInternetAvailable()
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
               // DOWNOLOAD DATA
               AssetUpdater(tapoDb,dataTapoDb,networkClient,context, context.supportFragmentManager, activity).getAllData()

           }

       }
        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
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

        navController = findNavController(R.id.nav_host_fragment_content_main)
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
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .commit();

            }
            override fun onDrawerStateChanged(i: Int) {}
        })



        navView.setNavigationItemSelectedListener{
            if (it.itemId == R.id.nav_tariff) {
                drawerLayout?.addDrawerListener(object : DrawerLayout.DrawerListener {
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



    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val menuLogin = menu.findItem(R.id.action_login)
        val menuLogOut = menu.findItem(R.id.action_logout)
        State.isLogin.observe(this, Observer {
            if (it == true) {
                menuLogOut.isVisible = true
                menuLogin.isVisible = false
            } else {
                menuLogOut.isVisible = false
                menuLogin.isVisible = true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.nav_settings)

        }
        if (item.itemId == R.id.action_info) {
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.nav_about_application)
        }
        if (item.itemId == R.id.action_login) {
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.nav_login)
        }
        if (item.itemId == R.id.action_logout) {
            sessionProvider.clearSession()
            Snackbar.make(window.decorView.rootView, "Wylogowano z serwisu", Snackbar.LENGTH_LONG).show()
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

    private fun saveInitNotification() {
        MainScope().launch(Dispatchers.IO){
            val settingToInsert = Setting("NotificationInit","",0,state = true)
            async { tapoDb.settingDb().insert(settingToInsert) }.await()
        }
    }

    private fun createChannels(block: Boolean = false) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = "Powiadomienia ogólne"
            val descriptionText ="W tym kanale są przesyłane powiadomienia dotyczące aplikacji i jej nowości w funkcjonowaniu"
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

//            val ss = notificationManager.notificationChannels
//            println(ss)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        navController?.removeOnDestinationChangedListener(listener)
    }

}


