package com.neonnet.vpn.dto

import com.neonnet.vpn.dto.entities.SubscriptionUsageInfo

data class GroupMapItem(
    var id: String,
    var remarks: String,
    val usageInfo: SubscriptionUsageInfo? = null,
)