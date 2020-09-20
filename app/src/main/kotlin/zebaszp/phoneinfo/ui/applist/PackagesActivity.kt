package zebaszp.phoneinfo.ui.applist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.facebook.litho.ComponentContext
import com.facebook.litho.ComponentTree
import com.facebook.litho.LithoView
import com.facebook.litho.StateHandler

private const val STATE_HANDLER = "stateHandler"

class PackagesActivity : AppCompatActivity() {

    private lateinit var componentTree: ComponentTree

    private val viewModel: StateHandlerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val c = ComponentContext(this)
        componentTree = ComponentTree.create(c, PackagesRoot.create(c))
            .stateHandler(viewModel.stateHandler)
            .build()
        setContentView(LithoView(c).apply { componentTree = this@PackagesActivity.componentTree })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stateHandler = componentTree.acquireStateHandler()
    }

    class StateHandlerViewModel(private val state: SavedStateHandle) : ViewModel() {
        private val liveData: MutableLiveData<StateHandler>
            get() = state.getLiveData(STATE_HANDLER)

        var stateHandler: StateHandler?
            get() = liveData.value
            set(value) { liveData.value = value }
    }
}
