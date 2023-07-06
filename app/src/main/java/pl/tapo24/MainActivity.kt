package pl.tapo24

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import pl.tapo24.data.EnginesType
import pl.tapo24.data.EnvironmentType
import pl.tapo24.data.State
import pl.tapo24.data.Uid
import pl.tapo24.databinding.ActivityMainBinding
import pl.tapo24.db.TapoDb
import pl.tapo24.db.entity.Setting
import pl.tapo24.infrastructure.NetworkClient
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    @Inject
    lateinit var tapoDb: TapoDb

    @Inject
    lateinit var networkClient: NetworkClient

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var test = MutableLiveData<Boolean>(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainScope().launch(Dispatchers.IO) {
            var settingUid: Setting? = null
            async { settingUid = tapoDb.settingDb().getSettingByName("uid") }.await()

            if (settingUid == null) {
                async {
                    val response = networkClient.getUid()
                    println(response)
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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_tariff
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}