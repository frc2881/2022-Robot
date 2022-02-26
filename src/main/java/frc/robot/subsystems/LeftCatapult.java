// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.Catapult.kBlueCargo;
import static frc.robot.Constants.Catapult.kCurrentLimit;
import static frc.robot.Constants.Catapult.kDistance;
import static frc.robot.Constants.Catapult.kForwardLimitLeft;
import static frc.robot.Constants.Catapult.kLeftMotor;
import static frc.robot.Constants.Catapult.kRedCargo;
import static frc.robot.Constants.Catapult.kResetPosition;
import static frc.robot.Constants.Catapult.kReverseLimit;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LeftCatapult extends SubsystemBase {
  private final CANSparkMax m_catapult;
  private final RelativeEncoder m_encoder;
  private final ColorSensorV3 m_colorSensor;
  private final ColorMatch m_colorMatcher;
  private boolean m_cargoIsRed;
  private boolean m_cargoIsBlue;
  private boolean m_isCorrectCargo;
  private boolean m_isIncorrectCargo;
  

  public LeftCatapult() {
    m_catapult = new CANSparkMax(kLeftMotor, MotorType.kBrushless);
    m_catapult.restoreFactoryDefaults();
    m_catapult.setInverted(false);
    m_catapult.setIdleMode(IdleMode.kBrake);
    m_catapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
    m_catapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
    m_catapult.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward,
                            (float)kForwardLimitLeft);
    m_catapult.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse,
                            (float)kReverseLimit);
    m_catapult.setSmartCurrentLimit(kCurrentLimit);

    m_encoder = m_catapult.getEncoder();
    m_encoder.setPosition(0);

    m_colorSensor = new ColorSensorV3(Port.kMXP);

    m_colorMatcher = new ColorMatch();
    m_colorMatcher.addColorMatch(kRedCargo);
    m_colorMatcher.addColorMatch(kBlueCargo);
    m_colorMatcher.setConfidenceThreshold(0.95);
  }

  public void reset() {
    run(0.0);
  }

  public void disableEncoderSoftLimit() {
    m_catapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, false);
    m_catapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, false);
  }

  public void enableEncoderSoftLimit() {
    m_catapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
    m_catapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
  }

  public void resetEncoder() {
    m_encoder.setPosition(kResetPosition);
  }

  public boolean reachedUpperSoftLimit() {
    return Math.abs(kForwardLimitLeft - m_encoder.getPosition()) < 0.1;
  }

  public boolean reachedLowerSoftLimit() {
    return Math.abs(kReverseLimit - m_encoder.getPosition()) < 0.1;
  }

  public void run(double speed) {
    m_catapult.set(speed);
  }

  public boolean isRed() {
    return m_cargoIsRed;
  }

  public boolean isBlue() {
    return m_cargoIsBlue;
  }

  public boolean isCorrectCargo(){
    if(m_cargoIsRed == true || m_cargoIsBlue == true){
      if((m_cargoIsRed == true && DriverStation.getAlliance() == Alliance.Red) || (m_cargoIsBlue == true && DriverStation.getAlliance() == Alliance.Blue)){
        if(m_isCorrectCargo == false){
          return m_isCorrectCargo = true;
        } else{
          return m_isCorrectCargo;
        }
      } else{
        return m_isCorrectCargo = false;
      }
    } else{
      return m_isCorrectCargo = false;
    }
  }

  public boolean isIncorrectCargo(){
    if(m_cargoIsRed == true || m_cargoIsBlue == true){
      if((m_cargoIsRed == true && DriverStation.getAlliance() == Alliance.Blue) || (m_cargoIsBlue == true && DriverStation.getAlliance() == Alliance.Red)){
        if(m_isIncorrectCargo == false){
          return m_isIncorrectCargo = true;
        } else{
          return m_isIncorrectCargo;
        }
      } else{
        return m_isIncorrectCargo = false;
      }
    } else{
      return m_isIncorrectCargo = false;
    }
  }
  

  @Override
  public void periodic() {
    Color detectedColor = m_colorSensor.getColor();
    ColorMatchResult match = m_colorMatcher.matchColor(detectedColor);

    if((match != null) && (match.color == kRedCargo) &&
       (m_colorSensor.getProximity() > kDistance)) {
      m_cargoIsRed = true;
    } else {
      m_cargoIsRed = false;
    }

    if((match != null) && (match.color == kBlueCargo) &&
       (m_colorSensor.getProximity() > kDistance)) {
      m_cargoIsBlue = true;
    } else {
      m_cargoIsBlue = false;
    }
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);

    builder.addDoubleProperty("Left Distance",
                              () -> m_colorSensor.getProximity(), null);
    builder.addBooleanProperty("Left Blue", () -> isBlue(), null);
    builder.addBooleanProperty("Left Red", () -> isRed(), null);
    builder.addDoubleProperty("Left Catapult Position",
                              () -> m_encoder.getPosition(), null);
  }
}
