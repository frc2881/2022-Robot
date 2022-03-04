// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.Climber.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
  private final CANSparkMax m_arm;
  private final RelativeEncoder m_encoder;
  private final Solenoid m_solenoid;

  public Climber() {
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
  }

  public void reset() {
    moveArm(0.0);
    _armUp();
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
    m_encoder.setPosition(0.1);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Climber Postion", () -> m_encoder.getPosition(),
                              null);
    builder.addDoubleProperty("Current", () -> m_arm.getOutputCurrent(), null);
  }
}
