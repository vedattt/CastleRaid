package xyz.vedat.castleraid.interfaces;

import org.bukkit.scheduler.BukkitTask;

public interface SprintAccelerable {
  
  float getMaxSpeed();
  float getDefaultSpeed();
  double getAccelerationRate();
  boolean hasOngoingSprintEvent();
  void setOngoingSprintEvent(BukkitTask sprintTask);
  
}
