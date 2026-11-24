package com.neonnet.vpn.ui.main

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neonnet.vpn.dto.TrafficStats
import com.neonnet.vpn.dto.entities.ProfileItem
import java.util.Locale

private val CyberBackground = Color(0xFF000000)
private val CyberPanel = Color(0xFF0D121A)
private val CyberPanelAlt = Color(0xFF111A24)
private val CyberOrange = Color(0xFFFF8738)
private val CyberIce = Color(0xFF55DFFF)
private val CyberMuted = Color(0xFF81909F)
private val CyberWhite = Color(0xFFEAF5FC)
private val CyberGreen = Color(0xFF62E6B5)
private val CyberRed = Color(0xFFFF5268)

@Composable
fun CyberIceDashboard(
    isRunning: Boolean,
    statusText: String,
    selectedProfile: ProfileItem?,
    pingMillis: Long,
    trafficStats: TrafficStats,
    onToggleConnection: () -> Unit,
    onTestConnection: () -> Unit,
    onOpenServers: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenMenu: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (pulse, ringRotation) = if (isRunning) CyberEnergyMotion() else 1f to 0f
    val statusColor = if (isRunning) CyberGreen else CyberMuted
    val serverName = selectedProfile?.remarks?.takeIf { it.isNotBlank() } ?: "No server selected"
    val protocol = selectedProfile?.configType?.name?.replace('_', ' ') ?: "—"
    val address = selectedProfile?.server?.takeIf { it.isNotBlank() }?.let {
        "$it:${selectedProfile.serverPort.orEmpty()}"
    } ?: "Select a server to begin"

    Column(
        modifier = modifier.background(Color.Black).padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(36.dp).clip(RoundedCornerShape(11.dp))
                    .background(Brush.linearGradient(listOf(CyberOrange, Color(0xFFB84D25), CyberIce))),
                contentAlignment = Alignment.Center
            ) {
                Text("N", color = Color.Black, fontSize = 22.sp, fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("NEON", color = CyberWhite, fontSize = 17.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
                Text("CYBER ICE NETWORK", color = CyberMuted, fontSize = 8.sp, letterSpacing = 1.7.sp)
            }
            Text(if (isRunning) "● ONLINE" else "● OFFLINE", color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text("⚙", color = CyberIce, fontSize = 19.sp, modifier = Modifier.clickable(onClick = onOpenSettings).padding(4.dp))
            Text("☰", color = CyberIce, fontSize = 20.sp, modifier = Modifier.clickable(onClick = onOpenMenu).padding(4.dp))
        }

        Row(
            Modifier.fillMaxWidth().height(154.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(Modifier.size(150.dp), contentAlignment = Alignment.Center) {
                Canvas(Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val radius = size.minDimension * 0.39f
                    drawCircle(
                        brush = Brush.sweepGradient(listOf(CyberOrange, CyberIce, Color(0xFF17364A), CyberOrange)),
                        radius = radius, center = center,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawCircle(
                        color = CyberIce.copy(alpha = 0.16f), radius = radius * 0.84f,
                        center = center, style = Stroke(width = 1.dp.toPx())
                    )
                    drawArc(
                        brush = Brush.sweepGradient(listOf(CyberIce, CyberOrange, CyberIce)),
                        startAngle = ringRotation - 72f, sweepAngle = 105f, useCenter = false,
                        topLeft = Offset(center.x - radius * 1.13f, center.y - radius * 1.13f),
                        size = androidx.compose.ui.geometry.Size(radius * 2.26f, radius * 2.26f),
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Box(
                    Modifier.fillMaxSize(0.69f).graphicsPulse(pulse)
                        .shadow(18.dp, CircleShape, ambientColor = CyberOrange.copy(alpha = 0.3f), spotColor = CyberIce.copy(alpha = 0.25f))
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(Color(0xFF202A35), Color(0xFF090D13), Color(0xFF18232E))))
                        .border(2.dp, Brush.linearGradient(listOf(CyberOrange, CyberIce)), CircleShape)
                        .clickable(onClick = onToggleConnection),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(if (isRunning) "Ⅱ" else "⏻", color = if (isRunning) CyberOrange else CyberIce, fontSize = 39.sp)
                        Text(if (isRunning) "DISCONNECT" else "CONNECT", color = CyberWhite, fontSize = 9.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                CyberMetricCard(Modifier.fillMaxWidth(), "PING", if (pingMillis > 0L) "$pingMillis ms" else "—", CyberIce, "◉")
                CyberMetricCard(Modifier.fillMaxWidth(), "PROTOCOL", protocol, CyberOrange, "⌘")
                Text(serverName, color = CyberWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(address, color = CyberMuted, fontSize = 9.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CyberTrafficCard(Modifier.weight(1f), "DOWNLOAD", formatCyberRate(trafficStats.downloadSpeed), formatCyberSize(trafficStats.totalDownload), CyberIce, "↓")
            CyberTrafficCard(Modifier.weight(1f), "UPLOAD", formatCyberRate(trafficStats.uploadSpeed), formatCyberSize(trafficStats.totalUpload), CyberOrange, "↑")
        }

        Spacer(Modifier.height(8.dp))
        CyberActionButton("TEST CONNECTION", CyberIce, Modifier.fillMaxWidth(), onTestConnection)

        Spacer(Modifier.height(12.dp))
        CyberActionButton(
            "◈  SERVERS  ›",
            CyberOrange,
            Modifier.fillMaxWidth(),
            onOpenServers
        )
}
    }

@Composable
private fun CyberMetricCard(
    modifier: Modifier,
    title: String,
    value: String,
    accent: Color,
    icon: String
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.linearGradient(listOf(CyberPanelAlt, CyberPanel)))
            .border(1.dp, Color(0xFF1D2B38), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, color = accent, fontSize = 16.sp)
            Spacer(Modifier.width(7.dp))
            Text(title, color = CyberMuted, fontSize = 9.sp, letterSpacing = 1.2.sp)
        }
        Spacer(Modifier.height(8.dp))
        Text(value, color = CyberWhite, fontSize = 17.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun CyberServerCard(name: String, address: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF142331), CyberPanel)))
            .border(1.dp, CyberIce.copy(alpha = 0.22f), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("◈", color = CyberOrange, fontSize = 20.sp)
            Spacer(Modifier.width(9.dp))
            Text("SELECTED SERVER", color = CyberMuted, fontSize = 9.sp, letterSpacing = 1.4.sp)
            Spacer(Modifier.weight(1f))
            Text("CHANGE  ›", color = CyberIce, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(11.dp))
        Text(name, color = CyberWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.height(4.dp))
        Text(address, color = CyberMuted, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun CyberTrafficCard(
    modifier: Modifier,
    title: String,
    speed: String,
    total: String,
    accent: Color,
    arrow: String
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(CyberPanel)
            .border(1.dp, accent.copy(alpha = 0.24f), RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(arrow, color = accent, fontSize = 17.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.width(6.dp))
            Text(title, color = CyberMuted, fontSize = 9.sp, letterSpacing = 1.sp)
        }
        Spacer(Modifier.height(8.dp))
        Text(speed, color = accent, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        Text("TOTAL  $total", color = CyberMuted, fontSize = 9.sp, maxLines = 1)
    }
}

@Composable
private fun CyberActionButton(
    label: String,
    accent: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(accent.copy(alpha = 0.09f))
            .border(1.dp, accent.copy(alpha = 0.55f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.7.sp)
    }
}

@Composable
private fun CyberNavItem(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, color = if (selected) CyberOrange else CyberMuted, fontSize = 21.sp)
        Text(label, color = if (selected) CyberOrange else CyberMuted, fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
    }
}

@Composable
private fun CyberEnergyMotion(): Pair<Float, Float> {
    val transition = rememberInfiniteTransition(label = "neon-energy-motion")
    val pulse by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.025f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "connect-pulse"
    )
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "energy-ring-rotation"
    )
    return pulse to rotation
}

private fun Modifier.graphicsPulse(scale: Float): Modifier =
    this.then(Modifier.graphicsLayerScale(scale))

private fun Modifier.graphicsLayerScale(scale: Float): Modifier =
    this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }

private fun formatCyberRate(bytesPerSecond: Long): String {
    val value = bytesPerSecond.coerceAtLeast(0L).toDouble()
    return when {
        value >= 1024.0 * 1024.0 -> String.format(Locale.getDefault(), "%.1f MB/s", value / (1024.0 * 1024.0))
        value >= 1024.0 -> String.format(Locale.getDefault(), "%.1f KB/s", value / 1024.0)
        else -> String.format(Locale.getDefault(), "%.0f B/s", value)
    }
}

private fun formatCyberSize(bytes: Long): String {
    val value = bytes.coerceAtLeast(0L).toDouble()
    return when {
        value >= 1024.0 * 1024.0 * 1024.0 -> String.format(Locale.getDefault(), "%.1f GB", value / (1024.0 * 1024.0 * 1024.0))
        value >= 1024.0 * 1024.0 -> String.format(Locale.getDefault(), "%.1f MB", value / (1024.0 * 1024.0))
        value >= 1024.0 -> String.format(Locale.getDefault(), "%.1f KB", value / 1024.0)
        else -> String.format(Locale.getDefault(), "%.0f B", value)
    }
}
