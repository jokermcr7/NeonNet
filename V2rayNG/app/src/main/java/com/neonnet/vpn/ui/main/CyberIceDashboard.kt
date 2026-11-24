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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neonnet.vpn.dto.TrafficStats
import com.neonnet.vpn.dto.entities.ProfileItem
import java.util.Locale

private val CyberBackground = Color(0xFF030405)
private val CyberPanel = Color(0xFF111315)
private val CyberIce = Color(0xFF20D9FF)
private val CyberGold = Color(0xFFFF7138)
private val CyberMuted = Color(0xFF9AA0A6)
private val CyberWhite = Color(0xFFF4F6F8)

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
    onOpenHome: () -> Unit,
    onOpenStatistics: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenMenu: () -> Unit,
    selectedPage: String = "home",
    modifier: Modifier = Modifier,
) {
    val (pulse, ringRotation) = if (isRunning) CyberEnergyMotion() else 1f to 0f
    val serverName = selectedProfile?.remarks?.takeIf { it.isNotBlank() } ?: "No server selected"
    val serverAddress = selectedProfile?.server?.takeIf { it.isNotBlank() }?.let {
        if (selectedProfile.serverPort.isNullOrBlank()) it else "$it:${selectedProfile.serverPort}"
    } ?: "Choose a server to connect"
    val statusColor = if (isRunning) CyberIce else CyberGold

    Column(
        modifier = modifier
            .background(CyberBackground)
            .padding(horizontal = 22.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(34.dp).clip(CircleShape)
                    .border(1.dp, statusColor.copy(alpha = 0.75f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(if (isRunning) "✓" else "⛨", color = statusColor, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Text("NEONNET  /  SECURE CORE", color = CyberIce, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                Text(statusText, color = CyberWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Text("⚙", color = CyberWhite, fontSize = 20.sp, modifier = Modifier.clickable(onClick = onOpenSettings).padding(horizontal = 8.dp, vertical = 5.dp))
            Text("♛", color = CyberGold, fontSize = 22.sp, modifier = Modifier.clickable(onClick = onOpenMenu).padding(start = 5.dp, end = 2.dp))
        }

        if (selectedPage == "home") {
            Spacer(Modifier.height(4.dp))
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Canvas(Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val maxRadius = size.minDimension * 0.49f
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = if (isRunning) listOf(CyberIce.copy(alpha = 0.34f), CyberIce.copy(alpha = 0.10f), Color.Transparent)
                            else listOf(CyberGold.copy(alpha = 0.28f), CyberGold.copy(alpha = 0.08f), Color.Transparent),
                            center = center,
                            radius = maxRadius
                        ),
                        radius = maxRadius,
                        center = center
                    )
                    for (i in 1..34) {
                        val radius = maxRadius * (0.30f + i * 0.019f)
                        drawCircle(
                            color = (if (isRunning) CyberIce else CyberGold).copy(alpha = (0.19f - i * 0.0042f).coerceAtLeast(0.018f)),
                            radius = radius,
                            center = center,
                            style = Stroke(width = if (i % 4 == 0) 1.35.dp.toPx() else 0.55.dp.toPx())
                        )
                    }
                    drawCircle(
                        color = if (isRunning) CyberIce.copy(alpha = 0.8f) else CyberGold.copy(alpha = 0.75f),
                        radius = maxRadius * 0.33f,
                        center = center,
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                    if (isRunning) {
                        drawArc(
                            color = CyberIce,
                            startAngle = ringRotation,
                            sweepAngle = 95f,
                            useCenter = false,
                            topLeft = Offset(center.x - maxRadius * 0.33f, center.y - maxRadius * 0.33f),
                            size = androidx.compose.ui.geometry.Size(maxRadius * 0.66f, maxRadius * 0.66f),
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }
                Box(
                    Modifier.size(174.dp)
                        .graphicsLayer { scaleX = pulse; scaleY = pulse }
                        .shadow(24.dp, CircleShape, ambientColor = if (isRunning) CyberIce else CyberGold, spotColor = if (isRunning) CyberIce else CyberGold)
                        .clip(CircleShape)
                        .background(
                            if (isRunning) Brush.verticalGradient(listOf(Color(0xFF22DFFF), Color(0xFF087D9B), Color(0xFF07313D)))
                            else Brush.verticalGradient(listOf(Color(0xFF542313), Color(0xFF24130F), Color(0xFF090C0E)))
                        )
                        .border(3.dp, (if (isRunning) CyberIce else CyberGold).copy(alpha = 0.85f), CircleShape)
                        .clickable(onClick = onToggleConnection),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⏻", color = Color.White, fontSize = 58.sp, lineHeight = 62.sp, fontWeight = FontWeight.Light)
                        Text(if (isRunning) "DISCONNECT TUNNEL" else "ACTIVATE NEONNET", color = Color.White.copy(alpha = 0.92f), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                    }
                }
            }

            Column(
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(21.dp))
                    .background(Color(0xFF10171C))
                    .border(1.dp, CyberIce.copy(alpha = 0.24f), RoundedCornerShape(21.dp))
                    .clickable(onClick = onOpenServers)
                    .padding(horizontal = 13.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(34.dp).clip(CircleShape).background(Color(0xFF17252D)), contentAlignment = Alignment.Center) {
                        Text("◎", color = CyberIce, fontSize = 23.sp)
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(serverName, color = CyberWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("NEON NODE  •  $serverAddress", color = CyberMuted, fontSize = 9.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Text("◆  ◆  ◆", color = CyberIce, fontSize = 12.sp, letterSpacing = 1.sp)
                    Spacer(Modifier.width(6.dp))
                    Text(if (pingMillis > 0L) "${pingMillis}ms" else "—", color = CyberMuted, fontSize = 9.sp)
                    Spacer(Modifier.width(8.dp))
                    Text("›", color = CyberWhite, fontSize = 24.sp)
                }
            }

        } else if (selectedPage == "stats") {
            Box(
                Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("LIVE TRAFFIC", color = CyberWhite, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    CyberTrafficCard(
                        Modifier.fillMaxWidth(),
                        "Download",
                        formatCyberRate(trafficStats.downloadSpeed),
                        formatCyberSize(trafficStats.totalDownload),
                        CyberIce,
                        "↓"
                    )
                    CyberTrafficCard(
                        Modifier.fillMaxWidth(),
                        "Upload",
                        formatCyberRate(trafficStats.uploadSpeed),
                        formatCyberSize(trafficStats.totalUpload),
                        CyberGold,
                        "↑"
                    )
                }
            }
        } else if (selectedPage == "profile") {
            Box(
                Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFF10171C))
                        .border(1.dp, Color(0xFF26343C), RoundedCornerShape(24.dp))
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Text("SERVER PROFILE", color = CyberIce, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(
                        selectedProfile?.remarks?.takeIf { it.isNotBlank() } ?: "No server selected",
                        color = CyberWhite,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text("Address", color = CyberMuted, fontSize = 11.sp)
                    Text(selectedProfile?.server ?: "—", color = CyberWhite, fontSize = 14.sp)
                    Text("Port", color = CyberMuted, fontSize = 11.sp)
                    Text(selectedProfile?.serverPort ?: "—", color = CyberWhite, fontSize = 14.sp)
                    Text("Protocol", color = CyberMuted, fontSize = 11.sp)
                    Text(selectedProfile?.configType?.name ?: "—", color = CyberWhite, fontSize = 14.sp)
                    selectedProfile?.description?.takeIf { it.isNotBlank() }?.let {
                        Text(it, color = CyberMuted, fontSize = 12.sp)
                    }
                }
            }
        } else {
            Spacer(Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))
        CyberBottomNavigation(
            selectedPage = selectedPage,
            onOpenHome = onOpenHome,
            onOpenStatistics = onOpenStatistics,
            onOpenServers = onOpenServers,
            onOpenProfile = onOpenProfile
        )
    }
}

@Composable
fun CyberBottomNavigation(
    selectedPage: String,
    onOpenHome: () -> Unit,
    onOpenStatistics: () -> Unit,
    onOpenServers: () -> Unit,
    onOpenProfile: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth().height(58.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFF080A0C))
            .border(1.dp, Color(0xFF26343A), RoundedCornerShape(32.dp))
            .padding(horizontal = 5.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CyberNavItem("⌂", "HOME", selectedPage == "home", onClick = onOpenHome)
        CyberNavItem("◉", "STATS", selectedPage == "stats", onClick = onOpenStatistics)
        CyberNavItem("◎", "SERVERS", selectedPage == "servers", onClick = onOpenServers)
        CyberNavItem("♙", "PROFILE", selectedPage == "profile", onClick = onOpenProfile)
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
        modifier = modifier.height(68.dp)
            .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomStart = 25.dp, bottomEnd = 25.dp))
            .background(Color(0xFF191B1D))
            .border(1.dp, accent.copy(alpha = 0.48f), RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomStart = 25.dp, bottomEnd = 25.dp))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, color = Color(0xFFD1D4D7), fontSize = 11.sp)
        Spacer(Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(arrow, color = accent, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(5.dp))
            Text(speed, color = CyberWhite, fontSize = 15.sp, fontWeight = FontWeight.Medium, maxLines = 1)
        }
        Text("TOTAL $total", color = CyberMuted, fontSize = 8.sp, maxLines = 1)
    }
}

@Composable
private fun CyberNavItem(icon: String, label: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        Modifier.size(width = 62.dp, height = 48.dp)
            .clip(CircleShape)
            .background(if (selected) Color(0xFF3A2118) else Color.Transparent)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(icon, color = if (selected) CyberGold else CyberMuted, fontSize = 22.sp)
        if (label.isNotEmpty()) Text(label, color = if (selected) CyberGold else CyberMuted, fontSize = 8.sp)
    }
}

@Composable
private fun CyberEnergyMotion(): Pair<Float, Float> {
    val transition = rememberInfiniteTransition(label = "neon-energy-motion")
    val pulse by transition.animateFloat(
        initialValue = 1f, targetValue = 1.025f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "connect-pulse"
    )
    val rotation by transition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(4200, easing = LinearEasing), RepeatMode.Restart),
        label = "energy-ring-rotation"
    )
    return pulse to rotation
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
