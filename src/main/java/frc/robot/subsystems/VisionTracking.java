// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.Catapult.kForwardLimitLeft;
import static frc.robot.Constants.Catapult.kForwardLimitRight;
import static frc.robot.Constants.Catapult.kLeftHighDist;
import static frc.robot.Constants.Catapult.kLeftHighLim;
import static frc.robot.Constants.Catapult.kLeftLowDist;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionTracking extends SubsystemBase {

  private PhotonCamera m_camera = new PhotonCamera("photonvision");
  
  /** Creates a new VisionTracking. */
  public VisionTracking() {
    
  }

  @Override
  public void periodic() {

    double pitch; 
    double yaw; 

    PhotonTrackedTarget target;

    target = m_camera.getLatestResult().getBestTarget();

    if(SmartDashboard.getBoolean("Disable Vision", false) == true){
      //possib want diff outcome
      pitch = 1000;
      yaw = 0;

    }
    else if(target == null){

      pitch = 1000;
      yaw = 0; 

    }
    else{

      pitch = target.getPitch();
      yaw = target.getYaw();

    }

    SmartDashboard.putNumber("Photon Vision Pitch", pitch);
    SmartDashboard.putNumber("Photon Vision Yaw", yaw);

    // This method will be called once per scheduler run

  
  }

public double CatapultDistToLim(double targetDist){

    double limit = ((targetDist - kLeftLowDist)/(kLeftHighDist - kLeftLowDist) * (kLeftHighLim - kForwardLimitLeft) + kForwardLimitLeft);
    return limit;
}


public void CatapultPitchToDist(){

  double pitch;

  PhotonTrackedTarget target;

  target = m_camera.getLatestResult().getBestTarget();

  if(SmartDashboard.getBoolean("Disable Vision", false) == true){
    //possib want diff outcome
    pitch = 1000;

  }
  else if(target == null){

    pitch = 1000;

  }
  else{

    pitch = target.getPitch();

  }

  double lowerPitch=0;
  double lowerDist=0;
  double higherPitch=0;
  double higherDist=0;
  double[][] pitches = {{3.0, 13.1},
                        {5.0, 0.78},
                        {7.0, -7.8},
                        {9.0, -13.75},
                        {11.0, -18.1},
                        {12, -20.45}};
  for(int i = 1; i <= pitches.length-1; i++){
    if(pitches[i][1] < pitch){
      lowerPitch = pitches[i][1];
      lowerDist = pitches[i][0];
      higherPitch = pitches[i-1][1];
      higherDist = pitches[i-1][0];

      break;
    }

  }
  this.CatapultDistToLim(((pitch - lowerPitch)/(higherPitch - lowerPitch) * (higherDist - lowerDist) + lowerDist));
}

public double LeftCatapultPitchToLim(){

  double pitch;

  PhotonTrackedTarget target = m_camera.getLatestResult().getBestTarget();
  if(target == null){

    return kForwardLimitLeft;

  }
  else{

    pitch = target.getPitch();

  } 

  double lowerPitch=0;
  double lowerLim=0;
  double higherPitch=0;
  double higherLim=0;
  double[][] pitches = {{4.2, 13.24},
                        {4.7, 0.9},
                        {5.1, -7.8},
                        {5.7, -13.5},
                        {6.9, -18.4},
                        {8, -20.2}};
  for(int i = 1; i <= pitches.length-1; i++){
    if(pitches[i][1] < pitch){
      lowerPitch = pitches[i][1];
      lowerLim = pitches[i][0];
      higherPitch = pitches[i-1][1];
      higherLim = pitches[i-1][0];
    
      break;
    }

  }
  double limit = (pitch - lowerPitch)/(higherPitch - lowerPitch) * (higherLim - lowerLim) + lowerLim;
  return limit;
}

public double RightCatapultPitchToLim(){

  double pitch;

  PhotonTrackedTarget target = m_camera.getLatestResult().getBestTarget();
  if(target == null){

    return kForwardLimitRight;

  }
  else{

    pitch = target.getPitch();

  } 

  double lowerPitch=0;
  double lowerLim=0;
  double higherPitch=0;
  double higherLim=0;
  double[][] pitches = {{4.2, 13.24},
                        {4.7, 0.9},
                        {5.15, -7.8},
                        {5.7, -13.5},
                        {6.8, -18.4},
                        {7.9, -20.2}};
  for(int i = 1; i <= pitches.length-1; i++){
    if(pitches[i][1] < pitch){
      lowerPitch = pitches[i][1];
      lowerLim = pitches[i][0];
      higherPitch = pitches[i-1][1];
      higherLim = pitches[i-1][0];
    
      break;
    }

  }
  double limit = (pitch - lowerPitch)/(higherPitch - lowerPitch) * (higherLim - lowerLim) + lowerLim;
  return limit;
}

//directly change the pitch :)
public double shuffleboardPitch(double limit){
return limit + SmartDashboard.getNumber("Catapult Soft Limit", 0);
}


public double getYaw(){

  double yaw;

  PhotonTrackedTarget target;

  target = m_camera.getLatestResult().getBestTarget();

  if(SmartDashboard.getBoolean("Disable Vision", false) == true){
    //possib want diff outcome
    yaw = 0;

  }
  else if(target == null){

    yaw = 0;

  }
  else{

    yaw = target.getYaw();

  }

  return -yaw; 

}


}
