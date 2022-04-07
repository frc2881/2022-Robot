// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.kEnableDetailedLogging;
import static frc.robot.Constants.Catapult.kForwardLimitLeft;
import static frc.robot.Constants.Catapult.kForwardLimitRight;
import static frc.robot.Constants.Catapult.kLeftHighDist;
import static frc.robot.Constants.Catapult.kLeftHighLim;
import static frc.robot.Constants.Catapult.kLeftLowDist;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.Log;

import java.util.ArrayList;
import java.util.Collections;

public class VisionTracking extends SubsystemBase {
  private final PhotonCamera vision_camera = new PhotonCamera("photonvision");
  private final PhotonCamera front_camera = new PhotonCamera("frontCamera");
  private final DoubleLogEntry m_logArea;
  private final DoubleLogEntry m_logPitch;
  private final DoubleLogEntry m_logSkew;
  private final DoubleLogEntry m_logYaw;
  public ArrayList<Double> yawVals = new ArrayList<Double>();
  public ArrayList<Double> pitchVals = new ArrayList<Double>();
  private double savedPitch; 
  private double time;
  private boolean disableVis = false;

  /** Creates a new VisionTracking. */
  public VisionTracking() {

    if(kEnableDetailedLogging) {
      DataLog log = DataLogManager.getLog();
      m_logArea = new DoubleLogEntry(log, "vision/area");
      m_logPitch = new DoubleLogEntry(log, "vision/pitch");
      m_logSkew = new DoubleLogEntry(log, "vision/skew");
      m_logYaw = new DoubleLogEntry(log, "vision/yaw");
    } else {
      m_logArea = null;
      m_logPitch = null;
      m_logSkew = null;
      m_logYaw = null;
    }
  }

  @Override
  public void periodic() {
    
    PhotonTrackedTarget target;
    double pitch;
    double yaw;

    target = vision_camera.getLatestResult().getBestTarget();

    if(SmartDashboard.getBoolean("Disable Vision", false) == true || disableVis == true) {
      // possibly want different outcome
      pitch = 1000;
      yaw = 0;
    } else if(target == null) {
      pitch = 1000;
      yaw = 0;
    } else {
      pitch = target.getPitch();
      yaw = target.getYaw();
    }

    SmartDashboard.putNumber("Photon Vision Pitch", pitch);
    SmartDashboard.putNumber("Photon Vision Yaw", yaw);

    if(kEnableDetailedLogging) {
      if(target != null) {
        m_logArea.append(target.getArea());
        m_logPitch.append(target.getPitch());
        m_logSkew.append(target.getSkew());
        m_logYaw.append(target.getYaw());
      }
    }
  
    yawVals.add(yaw);
    pitchVals.add(pitch);
    while(yawVals.size() > 5)
    yawVals.remove(0);
    while(pitchVals.size() > 5)
    pitchVals.remove(0);
  }
  
  public void reset(){
    vision_camera.setDriverMode(false);
    front_camera.setDriverMode(true);
  }

  public double findYawMedian(){
    ArrayList<Double> yawValsCopy = (ArrayList<Double>)yawVals.clone();
    Collections.sort(yawValsCopy);
    if (yawVals.size() == 0)
            return 0;
    if (yawVals.size() % 2 == 1)
            return yawValsCopy.get((yawValsCopy.size() + 1) / 2 - 1);
        else{
            double lower = yawValsCopy.get(yawValsCopy.size() / 2 - 1);
            double upper = yawValsCopy.get(yawValsCopy.size() / 2);

            return (lower + upper) / 2.0;
        }
    }

  public double findPitchMedian(){
    ArrayList<Double> pitchValsCopy = (ArrayList<Double>)pitchVals.clone();
    Collections.sort(pitchValsCopy);

    if (pitchVals.size() == 0)
            return 0;
    if (pitchVals.size() % 2 == 1)
            return pitchValsCopy.get((pitchValsCopy.size() + 1) / 2 - 1);
        else {
            double lower = pitchValsCopy.get(pitchValsCopy.size() / 2 - 1);
            double upper = pitchValsCopy.get(pitchValsCopy.size() / 2);

            return (lower + upper) / 2.0;
    }
  }

  public double CatapultDistToLim(double targetDist) {
    double limit = ((targetDist - kLeftLowDist) / (kLeftHighDist - kLeftLowDist) * (kLeftHighLim - kForwardLimitLeft) + kForwardLimitLeft);
    return limit;
  }

  public void CatapultPitchToDist() {
    PhotonTrackedTarget target;
    double pitch;

    target = vision_camera.getLatestResult().getBestTarget();

    if(SmartDashboard.getBoolean("Disable Vision", false) == true || disableVis == true) {
      // possibly want different outcome
      pitch = 1000;
    } else if(target == null) {
      pitch = 1000;
    } else {
      pitch = target.getPitch();
    }

    double lowerPitch = 0;
    double lowerDist = 0;
    double higherPitch = 0;
    double higherDist = 0;
    double[][] pitches = {{ 3.0, 13.1 },
                          { 5.0, 0.78 },
                          { 7.0, -7.8 },
                          { 9.0, -13.75 },
                          { 11.0, -18.1 },
                          { 12, -20.45 }};
    for(int i = 1; i <= (pitches.length - 1); i++) {
      if(pitches[i][1] < pitch) {
        lowerPitch = pitches[i][1];
        lowerDist = pitches[i][0];
        higherPitch = pitches[i - 1][1];
        higherDist = pitches[i - 1][0];
        break;
      }
    }

    this.CatapultDistToLim(((pitch - lowerPitch) / (higherPitch - lowerPitch) * (higherDist - lowerDist) + lowerDist));
  }

  public double LeftCatapultPitchToLim() {
    //PhotonTrackedTarget target = vision_camera.getLatestResult().getBestTarget();
    savedPitch = findPitchMedian();
    time = System.currentTimeMillis();

    vision_camera.takeInputSnapshot();
    vision_camera.takeOutputSnapshot();


    if((SmartDashboard.getBoolean("Disable Vision", false) == true) ||
       (savedPitch >= 1000) || disableVis == true) {
      return kForwardLimitLeft;}

    double lowerPitch = 0;
    double lowerLim = 0;
    double higherPitch = 0;
    double higherLim = 0;
    double[][] pitches = {{ 4.7-0.6, 13.24 }, //0.75
                          { 5.4-0.6, 0.9 },  //0.6
                          { 5.9-0.6, -7.8 }, //0.6
                          { 6.5-0.4, -13.5 },//left:0.6 right:0.5
                          { 7.6-0.4, -18.4 },//0.5
                          { 8.6-0.4, -20.2 },//0.5
                          { 8.6-0.4, -30.0 }};
    for(int i = 1; i <= (pitches.length - 1); i++) {
      if(pitches[i][1] < savedPitch) {
        lowerPitch = pitches[i][1];
        lowerLim = pitches[i][0];
        higherPitch = pitches[i - 1][1];
        higherLim = pitches[i - 1][0];
        break;
      }
    }

    double limit = (savedPitch - lowerPitch) / (higherPitch - lowerPitch) * (higherLim - lowerLim) + lowerLim;
    Log.log("Left Limit: " + limit);
    Log.log("Left Pitch: " + savedPitch);
    return limit;
  }

  public double RightCatapultPitchToLim() {

    double pitch; 

    if(System.currentTimeMillis() < time + 500){
      pitch = savedPitch;
    }
    else{
      pitch = findPitchMedian();
    }
  

    if((SmartDashboard.getBoolean("Disable Vision", false) == true) ||
       (pitch >= 1000) || disableVis == true) {
      return kForwardLimitRight;
    }
    
    double lowerPitch = 0;
    double lowerLim = 0;
    double higherPitch = 0;
    double higherLim = 0;
    double[][] pitches = {{ 4.7-0.6, 13.24 },
                          { 5.2-0.6, 0.9 },
                          { 5.3-0.6, -7.8 }, //moved 5.15 to 5.1
                          { 6.25-0.4, -13.5 },
                          { 7.35-0.4, -18.4 },
                          { 8.45-0.4, -20.2 },
                          { 8.45-0.4, -30.0}};
    for(int i = 1; i <= (pitches.length - 1); i++) {
      if(pitches[i][1] < pitch) {
        lowerPitch = pitches[i][1];
        lowerLim = pitches[i][0];
        higherPitch = pitches[i - 1][1];
        higherLim = pitches[i - 1][0];
        break;
      }
    }

    double limit = (pitch - lowerPitch) / (higherPitch - lowerPitch) * (higherLim - lowerLim) + lowerLim;
    Log.log("Right Limit: " + limit);
    Log.log("Right Pitch: " + pitch);
    return limit;
  }

  //directly change the pitch :)
  public double shuffleboardPitch(double limit) {
    return limit + SmartDashboard.getNumber("Catapult Soft Limit", 0);
  }

  public double getYaw() {
    double yaw = findYawMedian();

    if(SmartDashboard.getBoolean("Disable Vision", false) == true || disableVis == true) {
      // possibly want different outcome
      yaw = 0;
    } else if(yaw >= 1000) {
      yaw = 0;
    } else {
      yaw = yaw - 2;
    }

    return -yaw;
  }

  public void disableVFromController(){
    disableVis = true;
  }

  public void enableVFromController(){
    disableVis = false;
  }
}
