package com.bivashy.auth.api.config.premium;

import java.util.List;

public interface PremiumSettings {
    boolean isEnabled();

    boolean getBlockOfflinePlayersWithPremiumName();

    ProfileConflictResolutionStrategy getProfileConflictResolutionStrategy();

    List<String> getAuthenticationSteps();
}
