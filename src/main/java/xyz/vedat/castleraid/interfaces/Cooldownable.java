package xyz.vedat.castleraid.interfaces;

import xyz.vedat.castleraid.classes.CastleRaidCooldown;

public interface Cooldownable {
    
    long getCooldown(CastleRaidCooldown cooldown);
    boolean isOnCooldown(CastleRaidCooldown cooldown);
    void setCooldown(CastleRaidCooldown cooldown, long cooldownDuration);
    
}
