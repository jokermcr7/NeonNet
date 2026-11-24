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

private val CyberBackground = Color(0xFF05070B)
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
) {
    val scrollState = rememberScrollState()
    val energyTransition = rememberInfiniteTransition(label = "neon-energy-motion")
    val pulse by energyTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isRunning) 1.025f else 1f,
        animationSpec = if (isRunning) {
            infiniteRepeatable(
                animation = tween(2600, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        } else {
            tween(durationMillis = 0)
        },
        label = "connect-pulse"
    )
    val ringRotation by energyTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isRunning) 360f else 0f,
        animationSpec = if (isRunning) {
            infiniteRepeatable(
                animation = tween(4200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        } else {
            tween(durationMillis = 0)
        },
        label = "energy-ring-rotation"
    )
    val connectButtonScale = if (isRunning) pulse else 1f

    val statusColor = if (isRunning) CyberGreen else CyberMuted
    val serverName = selectedProfile?.remarks?.takeIf { it.isNotBlank() } ?: "No server selected"
    val protocol = selectedProfile?.configType?.name?.replace('_', ' ') ?: "—"
    val address = selectedProfile?.server?.takeIf { it.isNotBlank() }?.let {
        "$it:${selectedProfile.serverPort.orEmpty()}"
    } ?: "Select a server to begin"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .padding(bottom = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp, bottom = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(listOf(CyberOrange, Color(0xFFB84D25), CyberIce))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("N", color = Color.Black, fontSize = 25.sp, fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("NEON", color = CyberWhite, fontSize = 19.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
                Text("CYBER ICE NETWORK", color = CyberMuted, fontSize = 9.sp, letterSpacing = 2.sp)
            }
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(CyberPanelAlt)
                    .border(1.dp, Color(0xFF263544), CircleShape)
                    .clickable(onClick = onOpenMenu),
                contentAlignment = Alignment.Center
            ) {
                Text("☰", color = CyberIce, fontSize = 20.sp)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("SECURE CONNECTION", color = CyberMuted, fontSize = 10.sp, letterSpacing = 1.8.sp)
                Text(
                    if (isRunning) "YOU ARE PROTECTED" else "READY TO CONNECT",
                    color = CyberWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(statusColor.copy(alpha = 0.12f))
                    .border(1.dp, statusColor.copy(alpha = 0.35f), RoundedCornerShape(30.dp))
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(7.dp).clip(CircleShape).background(statusColor))
                    Spacer(Modifier.width(7.dp))
                    Text(if (isRunning) "ONLINE" else "OFFLINE", color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(statusText, color = CyberMuted, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp, bottom = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .aspectRatio(1f)
            ) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.minDimension * 0.39f
                val outerRadius = radius * 1.13f

                // Soft orange energy halo
                drawCircle(
                    color = CyberOrange.copy(alpha = 0.075f),
                    radius = outerRadius,
                    center = center,
                    style = Stroke(width = 12.dp.toPx())
                )
                drawCircle(
                    color = CyberOrange.copy(alpha = 0.28f),
                    radius = radius,
                    center = center,
                    style = Stroke(width = 5.dp.toPx())
                )

                // Main neon ring
                drawCircle(
                    brush = Brush.sweepGradient(
                        listOf(
                            Color(0xFFFFC18C),
                            CyberOrange,
                            Color(0xFF8F3515),
                            Color(0xFFFFA45E),
                            CyberOrange
                        )
                    ),
                    radius = radius,
                    center = center,
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )

                // Inner icy accent
                drawCircle(
                    color = CyberIce.copy(alpha = 0.22f),
                    radius = radius * 0.84f,
                    center = center,
                    style = Stroke(width = 1.dp.toPx())
                )

                // Outer energy track
                drawCircle(
                    color = CyberOrange.copy(alpha = 0.42f),
                    radius = outerRadius,
                    center = center,
                    style = Stroke(width = 1.dp.toPx())
                )

                val arcTopLeft = Offset(
                    center.x - outerRadius,
                    center.y - outerRadius
                )
                val arcSize = androidx.compose.ui.geometry.Size(
                    outerRadius * 2f,
                    outerRadius * 2f
                )

                // Wide orange glow behind the moving energy arc
                drawArc(
                    color = CyberOrange.copy(alpha = 0.16f),
                    startAngle = ringRotation - 72f,
                    sweepAngle = 112f,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(width = 13.dp.toPx(), cap = StrokeCap.Round)
                )

                // Bright rotating orange energy
                drawArc(
                    brush = Brush.sweepGradient(
                        listOf(
                            Color.Transparent,
                            CyberOrange.copy(alpha = 0.35f),
                            Color(0xFFFFD0A1),
                            Color(0xFFFF8A32),
                            CyberOrange.copy(alpha = 0.1f)
                        )
                    ),
                    startAngle = ringRotation - 72f,
                    sweepAngle = 112f,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                )

                // Thin icy-blue counter accent
                drawArc(
                    color = CyberIce.copy(alpha = 0.8f),
                    startAngle = ringRotation + 145f,
                    sweepAngle = 24f,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.48f)
                    .aspectRatio(1f)
                    .graphicsPulse(connectButtonScale)
                    .shadow(22.dp, CircleShape, ambientColor = CyberOrange.copy(alpha = 0.35f), spotColor = CyberIce.copy(alpha = 0.3f))
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF202A35), Color(0xFF090D13), Color(0xFF18232E))
                        )
                    )
                    .border(2.dp, Brush.linearGradient(listOf(CyberOrange, CyberIce)), CircleShape)
                    .clickable(onClick = onToggleConnection),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (isRunning) "Ⅱ" else "⏻", color = if (isRunning) CyberOrange else CyberIce, fontSize = 49.sp, fontWeight = FontWeight.Light)
                    Text(if (isRunning) "DISCONNECT" else "CONNECT", color = CyberWhite, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 1.4.sp)
                    Text("TAP TO ${if (isRunning) "STOP" else "START"}", color = CyberMuted, fontSize = 8.sp, letterSpacing = 1.sp)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CyberMetricCard(
                modifier = Modifier.weight(1f),
                title = "PING",
                value = if (pingMillis > 0L) "$pingMillis ms" else "—",
                accent = CyberIce,
                icon = "◉"
            )
            CyberMetricCard(
                modifier = Modifier.weight(1f),
                title = "PROTOCOL",
                value = protocol,
                accent = CyberOrange,
                icon = "⌘"
            )
        }

        Spacer(Modifier.height(10.dp))
        CyberServerCard(
            name = serverName,
            address = address,
            onClick = onOpenServers
        )

        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CyberTrafficCard(
                modifier = Modifier.weight(1f),
                title = "DOWNLOAD",
                speed = formatCyberRate(trafficStats.downloadSpeed),
                total = formatCyberSize(trafficStats.totalDownload),
                accent = CyberIce,
                arrow = "↓"
            )
            CyberTrafficCard(
                modifier = Modifier.weight(1f),
                title = "UPLOAD",
                speed = formatCyberRate(trafficStats.uploadSpeed),
                total = formatCyberSize(trafficStats.totalUpload),
                accent = CyberOrange,
                arrow = "↑"
            )
        }

        Spacer(Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CyberActionButton("TEST CONNECTION", CyberIce, Modifier.weight(1f), onTestConnection)
            CyberActionButton("SERVERS", CyberOrange, Modifier.weight(1f), onOpenServers)
        }

        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(CyberPanel)
                .border(1.dp, Color(0xFF1C2B38), RoundedCornerShape(18.dp))
                .padding(vertical = 13.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CyberNavItem("⌂", "HOME", true) {}
            CyberNavItem("▤", "SERVERS", false, onOpenServers)
            CyberNavItem("⚙", "SETTINGS", false, onOpenSettings)
        }
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
  (selected) CyberOrange else CyberMuted, fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
    }
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
