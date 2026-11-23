package com.neonnet.vpn.ui.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.neonnet.vpn.R
import com.neonnet.vpn.dto.TrafficStats
import com.neonnet.vpn.ui.compose.AppDivider
import com.neonnet.vpn.ui.compose.colorFabActive
import com.neonnet.vpn.ui.compose.colorFabInactiveDark
import com.neonnet.vpn.ui.compose.colorFabInactiveLight
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun MainBottomBar(
    displayText: String,
    isRunning: Boolean,
    isDarkTheme: Boolean,
    trafficStats: TrafficStats,
    onAction: (MainAction) -> Unit
) {
    val scope = rememberCoroutineScope()
    val rotationAnim = remember { Animatable(0f) }
    val panelColor = Color(0xFF080B10)
    val orange = Color(0xFFFF9638)
    val iceBlue = Color(0xFF55D8FF)
    val muted = Color(0xFF8995A5)

    LaunchedEffect(isRunning) {
        if (!isRunning) rotationAnim.snapTo(0f)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isDarkTheme) panelColor else MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        AppDivider()
        Text(
            text = displayText,
            style = MaterialTheme.typography.labelMedium,
            color = if (isRunning) orange else muted,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onAction(MainAction.TestCurrentServer) }
                .padding(start = 20.dp, top = 9.dp, bottom = 3.dp)
                .semantics { contentDescription = displayText }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "↓",
                    color = iceBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    formatTrafficRate(trafficStats.downloadSpeed),
                    color = iceBlue,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Σ ${formatTrafficSize(trafficStats.totalDownload)}",
                    color = muted,
                    fontSize = 10.sp
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "↑",
                    color = orange,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    formatTrafficRate(trafficStats.uploadSpeed),
                    color = orange,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Σ ${formatTrafficSize(trafficStats.totalUpload)}",
                    color = muted,
                    fontSize = 10.sp
                )
            }
            FloatingActionButton(
                onClick = {
                    if (!isRunning) {
                        scope.launch {
                            rotationAnim.animateTo(
                                targetValue = 720f,
                                animationSpec = tween(durationMillis = 900)
                            )
                        }
                    }
                    onAction(MainAction.ToggleService)
                },
                modifier = Modifier.size(54.dp),
                containerColor = if (isRunning) Color(0xFFFF3D55) else orange
            ) {
                Icon(
                    painter = if (isRunning) painterResource(R.drawable.ic_stop_24dp)
                    else painterResource(R.drawable.ic_play_24dp),
                    contentDescription = stringResource(
                        if (isRunning) R.string.acc_stop else R.string.acc_start
                    ),
                    tint = Color.Black,
                    modifier = Modifier
                        .size(24.dp)
                        .graphicsLayer { rotationZ = rotationAnim.value }
                )
            }
        }
    }
}

private fun formatTrafficRate(bytesPerSecond: Long): String {
    val value = bytesPerSecond.coerceAtLeast(0L).toDouble()
    return when {
        value >= 1024.0 * 1024.0 ->
            String.format(Locale.getDefault(), "%.1f MB/s", value / (1024.0 * 1024.0))
        value >= 1024.0 ->
            String.format(Locale.getDefault(), "%.1f KB/s", value / 1024.0)
        else -> String.format(Locale.getDefault(), "%.0f B/s", value)
    }
}

private fun formatTrafficSize(bytes: Long): String {
    val value = bytes.coerceAtLeast(0L).toDouble()
    return when {
        value >= 1024.0 * 1024.0 * 1024.0 ->
            String.format(Locale.getDefault(), "%.1f GB", value / (1024.0 * 1024.0 * 1024.0))
        value >= 1024.0 * 1024.0 ->
            String.format(Locale.getDefault(), "%.1f MB", value / (1024.0 * 1024.0))
        value >= 1024.0 ->
            String.format(Locale.getDefault(), "%.1f KB", value / 1024.0)
        else -> String.format(Locale.getDefault(), "%.0f B", value)
    }
}
