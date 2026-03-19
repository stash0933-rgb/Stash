package uk.ac.tees.mad.stash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import uk.ac.tees.mad.stash.navigation.StashNavGraph
import uk.ac.tees.mad.stash.ui.theme.StashTheme

class MainActivity : ComponentActivity() {

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
