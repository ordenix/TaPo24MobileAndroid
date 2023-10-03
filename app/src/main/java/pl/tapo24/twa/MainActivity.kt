package pl.tapo24.twa


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
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
import pl.tapo24.twa.utils.CheckConnection
import pl.tapo24.twa.updater.DataUpdater
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
    var test = MutableLiveData<Boolean>(false)

    private val listener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
        State.internetStatus.value = CheckConnection().getConnectionType(applicationContext)
        if (State.countChangeFragment > 10 && State.internetStatus.value != 0) {
            State.countChangeFragment  = 0
            //SessionProvider(tapoDb,networkClient).restoreSession()
            CheckVersion(tapoDb,networkClient,this).checkVersion()
            AssetUpdater(tapoDb,dataTapoDb,networkClient,this, this.supportFragmentManager).getAllData()
        }
        State.countChangeFragment += 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

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
                AssetUpdater(tapoDb,dataTapoDb,networkClient,this, this.supportFragmentManager).getAllData()


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
                AssetUpdater(tapoDb,dataTapoDb,networkClient,this, this.supportFragmentManager).getAllData()
            }
            // Add customization options here


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        State.internetStatus.value = CheckConnection().getConnectionType(applicationContext)

        State.internetStatus.observe(this, Observer {
            if (it != 0 ) {
                // to do execute offline stacks
                sessionProvider.restoreSession()
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
               CheckVersion(tapoDb,networkClient, context).checkVersion()
               AssetUpdater(tapoDb,dataTapoDb,networkClient,context, context.supportFragmentManager).getAllData()

           }

       }
        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_road, R.id.nav_tariff, R.id.nav_law, R.id.nav_helpers
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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
                        navController.navigate(it.itemId)
                        drawerLayout.removeDrawerListener(this)
                    }
                })
                drawerLayout.closeDrawer(GravityCompat.START)

            } else {
                navController.navigate(it.itemId)
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

}


