package pl.tapo24


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
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.tapo24.data.EnginesType
import pl.tapo24.data.EnvironmentType
import pl.tapo24.data.State
import pl.tapo24.databinding.ActivityMainBinding
import pl.tapo24.db.TapoDb
import pl.tapo24.db.entity.Setting
import pl.tapo24.dbData.DataTapoDb
import pl.tapo24.infrastructure.NetworkClient
import pl.tapo24.ui.tariff.TariffFragment
import pl.tapo24.utils.CheckConnection
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
    lateinit var tapoDb: TapoDb
    
    @Inject
    lateinit var dataTapoDb: DataTapoDb

    @Inject
    lateinit var networkClient: NetworkClient

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var test = MutableLiveData<Boolean>(false)

    private var downloadDataDialogClose = MutableLiveData<Boolean>(false)
    private var downloadText = MutableLiveData<String>("Proszę czekać")
    private val listener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
        State.internetStatus = CheckConnection().getConnectionType(applicationContext)
        println("change destination")
    }


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        DataUpdater(tapoDb,dataTapoDb,networkClient,this).getData()
        DataUpdater(tapoDb,dataTapoDb,networkClient,this).getPDF()


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
                    val setting: Setting = Setting("settingEnvironment","",EnvironmentType.Master.ordinal)
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


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_road, R.id.nav_tariff,R.id.nav_law ,R.id.nav_helpers
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
        val sss = menu.findItem(R.id.action_settings)
        test.observe(this, Observer {
                sss.isVisible = !it
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


