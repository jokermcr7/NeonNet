package com.neonnet.vpn.ui.shortcut

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.neonnet.vpn.core.CoreServiceManager
import com.neonnet.vpn.core.LauncherManager
import com.neonnet.vpn.ui.base.BaseComponentActivity

class ScSwitchActivity : BaseComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Composable
    override fun ScreenContent() {
        LaunchedEffect(Unit) {
            moveTaskToBack(true)
            if (CoreServiceManager.isRunning()) {
                LauncherManager.stopService(this@ScSwitchActivity)
            } else {
                LauncherManager.startServiceFromToggle(this@ScSwitchActivity)
            }
            finish()
        }
    }
}
