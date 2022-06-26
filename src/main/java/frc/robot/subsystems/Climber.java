// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.kEnableDetailedLogging;
import static frc.robot.Constants.Climber.kForwardLimit;
import static frc.robot.Constants.Climber.kMatchSafety;
import static frc.robot.Constants.Climber.kMotor;
import static frc.robot.Constants.Climber.kRampRate;
import static frc.robot.Constants.Climber.kReverseLimit;
import static frc.robot.Constants.Climber.kRotationsToInches;
import static frc.robot.Constants.Climber.kSolenoid;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.util.datalog.BooleanLogEntry;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.NavX;

public class Climber extends SubsystemBase {
  private final CANSparkMax m_arm;
  private final RelativeEncoder m_encoder;
  private final Solenoid m_solenoid;
  private final DoubleLogEntry m_logPosition;
  private final DoubleLogEntry m_logOutput;
  private final DoubleLogEntry m_logBusVoltage;
  private final DoubleLogEntry m_logCurrent;
  private final BooleanLogEntry m_logSolenoid;
  private final NavX m_navx;
  private float largestVal = 0;
  private boolean safe;
  private boolean back = true;

  public Climber(NavX navx) {
    m_arm = new CANSparkMax(kMotor, MotorType.kBrushless);
    m_arm.restoreFactoryDefaults();
    m_arm.setInverted(false);
    m_arm.setIdleMode(IdleMode.kBrake);
    m_arm.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
    m_arm.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward,
                       (float)kForwardLimit);
    m_arm.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
    m_arm.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse,
                       (float)kReverseLimit);
    m_arm.setOpenLoopRampRate(kRampRate);

    m_encoder = m_arm.getEncoder();
    m_encoder.setPositionConversionFactor(kRotationsToInches);

    m_solenoid = new Solenoid(PneumaticsModuleType.REVPH, kSolenoid);

    m_navx = navx;

    if(kEnableDetailedLogging) {
      DataLog log = DataLogManager.getLog();
      m_logPosition = new DoubleLogEntry(log, "/climber/position");
      m_logOutput = new DoubleLogEntry(log, "/climber/output");
      m_logBusVoltage = new DoubleLogEntry(log, "/climber/busVoltage");
      m_logCurrent = new DoubleLogEntry(log, "/climber/current");
      m_logSolenoid = new BooleanLogEntry(log, "/climber/solenoid");
    } else {
      m_logPosition = null;
      m_logOutput = null;
      m_logBusVoltage = null;
      m_logCurrent = null;
      m_logSolenoid = null;
    }
  }

  @Override
  public void periodic() {
    if(kEnableDetailedLogging) {
      m_logPosition.append(m_encoder.getPosition());
      m_logOutput.append(m_arm.getAppliedOutput());
      m_logBusVoltage.append(m_arm.getBusVoltage());
      m_logCurrent.append(m_arm.getOutputCurrent());
    }

    float roll = m_navx.getRoll();

    if(back == true) {
      if(roll > largestVal) {
        largestVal = roll;
      }
      else if(roll < largestVal - 3) {
        if(largestVal < 19) {
          safe = true;
        } else{
          safe = false;
        }
        back = false;
      }
    } else {
      if(roll < largestVal) {
        largestVal = roll;
      }
      else if(roll > largestVal + 3) {
        if(largestVal > -24) {
          safe = true;
        } else {
          safe = false;
        }
        back = true;
      }
    }
  }

  public void setBackTrue() {
    back = true;
  }

  public void reset() {
    moveArm(0.0);
    _armUp();
    back = true;
    safe = true;
  }

  public void _moveArm(double speed) {
    m_arm.set(speed);
  }

  /**
   * Extends or retracts the arm
   *
   * @param speed positive value extends
   */
  public void moveArm(double speed) {
    if(DriverStation.getMatchTime() < kMatchSafety) {
      _moveArm(speed);
    }
  }

  public void _armBack() {
    m_solenoid.set(true);
    if(kEnableDetailedLogging) {
      m_logSolenoid.append(m_solenoid.get());
    }
  }

  /**
   * Brings the arm to back position
   */
  public void armBack() {
    if(DriverStation.getMatchTime() < kMatchSafety) {
      _armBack();
    }
  }

  public void _armUp() {
    m_solenoid.set(false);
    if(kEnableDetailedLogging) {
      m_logSolenoid.append(m_solenoid.get());
    }
  }

  /**
   * Brings the arm to upright position
   */
  public void armUp() {
    if(DriverStation.getMatchTime() < kMatchSafety) {
      _armUp();
    }
  }

  public void _armToggle() {
    m_solenoid.toggle();
    if(kEnableDetailedLogging) {
      m_logSolenoid.append(m_solenoid.get());
    }
  }

  public void armToggle() {
    if(DriverStation.getMatchTime() < kMatchSafety) {
      _armToggle();
    }
  }

  public Double getClimbEncoderPosition() {
    return m_encoder.getPosition();
  }

  public void disableEncoderSoftLimit() {
    m_arm.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, false);
    m_arm.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, false);
  }

  public void enableEncoderSoftLimit() {
    m_arm.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
    m_arm.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
  }

  public void resetEncoder() {
    m_encoder.setPosition(-0.1);
  }

  public boolean isSafe(){
    return safe;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Climber Postion", () -> m_encoder.getPosition(),
                              null);
    builder.addDoubleProperty("Current", () -> m_arm.getOutputCurrent(), null);
  }
}
