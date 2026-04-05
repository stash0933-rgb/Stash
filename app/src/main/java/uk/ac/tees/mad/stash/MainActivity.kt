package uk.ac.tees.mad.stash

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import uk.ac.tees.mad.stash.navigation.StashNavGraph
import uk.ac.tees.mad.stash.ui.theme.StashTheme

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            StashTheme {
                StashNavGraph()
            }
        }
    }
}
