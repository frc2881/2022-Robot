// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.Catapult.kForwardLimitLeft;
import static frc.robot.Constants.Catapult.kForwardLimitRight;
import static frc.robot.Constants.Catapult.kLeftHighDist;
import static frc.robot.Constants.Catapult.kLeftHighLim;
import static frc.robot.Constants.Catapult.kLeftLowDist;
import static frc.robot.Constants.Catapult.kRightHighDist;
import static frc.robot.Constants.Catapult.kRightHighLim;
import static frc.robot.Constants.Catapult.kRightLowDist;

import com.revrobotics.CANSparkMax;

import org.photonvision.PhotonCamera;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionTracking extends SubsystemBase {


  private PhotonCamera m_camera = new PhotonCamera("photonvision");
  /** Creates a new VisionTracking. */
  public VisionTracking() {}

  @Override
  public void periodic() {

    double pitch; 
    double yaw; 

    if(m_camera.getLatestResult().getBestTarget() == null){

      pitch = 0;
      yaw = 0; 

    }
    else{

      pitch = m_camera.getLatestResult().getBestTarget().getPitch();
      yaw = m_camera.getLatestResult().getBestTarget().getYaw();

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

  if(m_camera.getLatestResult().getBestTarget() == null){

    pitch = 0;

  }
  else{

    pitch = m_camera.getLatestResult().getBestTarget().getPitch();

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
  for(int i = 0; i <= pitches.length-1; i++){
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

public double CatapultPitchToLim(){

  double pitch;

  if(m_camera.getLatestResult().getBestTarget() == null){

    pitch = 0;

  }
  else{

    pitch = m_camera.getLatestResult().getBestTarget().getPitch();

  } 

  double lowerPitch=0;
  double lowerLim=0;
  double higherPitch=0;
  double higherLim=0;
  double[][] pitches = {{4.5, 13.24},
                        {4.9, 0.9},
                        {5.3, -7.8},
                        {5.8, -13.5},
                        {6.9, -18.4},
                        {8, -20.2}};
  for(int i = 0; i <= pitches.length-1; i++){
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


public double getYaw(){

  double yaw;

  if(m_camera.getLatestResult().getBestTarget() == null){

    yaw = 0;

  }
  else{

    yaw = m_camera.getLatestResult().getBestTarget().getYaw();

  }

  return -yaw; 

}


}
