package zigzaggroup.schain.mobile.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import zigzaggroup.schain.mobile.R
import zigzaggroup.schain.mobile.databinding.ActivityMainBinding
import zigzaggroup.schain.mobile.utils.DataHolder


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleIntent(intent)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupActionBarWithNavController(navController)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    fun reinitializeApp() {
        runOnUiThread {
            navController.popBackStack(R.id.homeFragment, true)
            navController.navigate(R.id.homeFragment)
        }
    }

    // TODO fix bug when opening from link when app is already running
    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            appLinkData?.lastPathSegment?.also { id ->
                DataHolder.setItemId(id)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}