package xyz.vedat.castleraid.interfaces;

public interface ClassAccelerable {
  
  boolean CAN_ACCELERATE = true;
  
  float getMaxSpeed();
  float getDefaultSpeed();
  double getAccelerationRate();
  
}
