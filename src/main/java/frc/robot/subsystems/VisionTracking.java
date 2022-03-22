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

public class VisionTracking extends SubsystemBase {
  private final PhotonCamera m_camera = new PhotonCamera("photonvision");
  private final DoubleLogEntry m_logArea;
  private final DoubleLogEntry m_logPitch;
  private final DoubleLogEntry m_logSkew;
  private final DoubleLogEntry m_logYaw;

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

    target = m_camera.getLatestResult().getBestTarget();

    if(SmartDashboard.getBoolean("Disable Vision", false) == true) {
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
  }

  public double CatapultDistToLim(double targetDist) {
    double limit = ((targetDist - kLeftLowDist) / (kLeftHighDist - kLeftLowDist) * (kLeftHighLim - kForwardLimitLeft) + kForwardLimitLeft);
    return limit;
  }

  public void CatapultPitchToDist() {
    PhotonTrackedTarget target;
    double pitch;

    target = m_camera.getLatestResult().getBestTarget();

    if(SmartDashboard.getBoolean("Disable Vision", false) == true) {
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
    double pitch;
    PhotonTrackedTarget target = m_camera.getLatestResult().getBestTarget();

    if((SmartDashboard.getBoolean("Disable Vision", false) == true) ||
       (target == null)) {
      return kForwardLimitLeft;
    } else {
      pitch = target.getPitch();
    }

    double lowerPitch = 0;
    double lowerLim = 0;
    double higherPitch = 0;
    double higherLim = 0;
    double[][] pitches = {{ 4.2, 13.24 },
                          { 4.7, 0.9 },
                          { 5.1, -7.8 },
                          { 5.7, -13.5 },
                          { 6.85, -18.4 },
                          { 7.95, -20.2 }};
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
    System.out.println("Left Limit: " + limit);
    System.out.println("Left Pitch: " + pitch);
    return limit;
  }

  public double RightCatapultPitchToLim() {
    double pitch;
    PhotonTrackedTarget target = m_camera.getLatestResult().getBestTarget();

    if((SmartDashboard.getBoolean("Disable Vision", false) == true) ||
       (target == null)) {
      return kForwardLimitRight;
    } else {
      pitch = target.getPitch();
    }

    double lowerPitch = 0;
    double lowerLim = 0;
    double higherPitch = 0;
    double higherLim = 0;
    double[][] pitches = {{ 4.2, 13.24 },
                          { 4.7, 0.9 },
                          { 5.1, -7.8 }, //moved 5.15 to 5.1
                          { 5.7, -13.5 },
                          { 6.8, -18.4 },
                          { 7.9, -20.2 }};
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
    System.out.println("Right Limit: " + limit);
    System.out.println("Right Pitch: " + pitch);
    return limit;
  }

  //directly change the pitch :)
  public double shuffleboardPitch(double limit) {
    return limit + SmartDashboard.getNumber("Catapult Soft Limit", 0);
  }

  public double getYaw() {
    PhotonTrackedTarget target;
    double yaw;

    target = m_camera.getLatestResult().getBestTarget();

    if(SmartDashboard.getBoolean("Disable Vision", false) == true) {
      // possibly want different outcome
      yaw = 0;
    } else if(target == null) {
      yaw = 0;
    } else {
      yaw = target.getYaw() - 2;
    }

    return -yaw;
  }
}
