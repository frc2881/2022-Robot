// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;
import static frc.robot.Constants.kEnableDetailedLogging;
import static frc.robot.Constants.Catapult.kBlueCargo;
import static frc.robot.Constants.Catapult.kCurrentLimit;
import static frc.robot.Constants.Catapult.kDistance;
import static frc.robot.Constants.Catapult.kEjectVoltage;
import static frc.robot.Constants.Catapult.kForwardLimitLeft;
import static frc.robot.Constants.Catapult.kLeftMotor;
import static frc.robot.Constants.Catapult.kRedCargo;
import static frc.robot.Constants.Catapult.kResetPosition;
import static frc.robot.Constants.Catapult.kResetVoltage;
import static frc.robot.Constants.Catapult.kReverseLimit;
import static frc.robot.Constants.Catapult.kShootVoltage;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LeftCatapult extends SubsystemBase {
  private final CANSparkMax m_catapult;
  private final RelativeEncoder m_encoder;
  private ColorSensorV3 m_colorSensor;
  private final ColorMatch m_colorMatcher;
  private boolean m_cargoIsRed;
  private boolean m_cargoIsBlue;
  private final Debouncer m_correctDebouncer;
  private final Debouncer m_incorrectDebouncer;
  private final DoubleLogEntry m_logPosition;
  private final DoubleLogEntry m_logOutput;
  private final DoubleLogEntry m_logBusVoltage;
  private final DoubleLogEntry m_logCurrent;
  private final VisionTracking m_vision;
  public double limit;

  public LeftCatapult(VisionTracking vision) {
    m_vision = vision;
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

    m_correctDebouncer = new Debouncer(0.1, Debouncer.DebounceType.kBoth);
    m_incorrectDebouncer = new Debouncer(0.1, Debouncer.DebounceType.kBoth);

    if(kEnableDetailedLogging) {
      DataLog log = DataLogManager.getLog();
      m_logPosition = new DoubleLogEntry(log, "/leftCatapult/position");
      m_logOutput = new DoubleLogEntry(log, "/leftCatapult/output");
      m_logBusVoltage = new DoubleLogEntry(log, "/leftCatapult/busVoltage");
      m_logCurrent = new DoubleLogEntry(log, "/leftCatapult/current");
    } else {
      m_logPosition = null;
      m_logOutput = null;
      m_logBusVoltage = null;
      m_logCurrent = null;
    }
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
    double position = m_encoder.getPosition();
    boolean difference = (limit - position) < 0.25;
    SmartDashboard.putNumber("L Encoder Position", position);
    SmartDashboard.putBoolean("L Upper Soft Limit Reached", difference);
    return difference;
  }

  public boolean reachedLowerSoftLimit() {
    return Math.abs(kReverseLimit - m_encoder.getPosition()) < 0.1;
  }

  public void run(double voltage) {
    m_catapult.setVoltage(voltage);
  }

  public void score() {
    limit = m_vision.LeftCatapultPitchToLim();
    limit += SmartDashboard.getNumber("Catapult Soft Limit", 0);
    m_catapult.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, (float) limit);
    SmartDashboard.putNumber("left limit at score", limit);
    run(kShootVoltage);
  }

  public void eject() {
    limit = kForwardLimitLeft;
    m_catapult.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, (float) limit);
    run(kEjectVoltage);
  }

  public void stop() {
    run(0);
  }

  public void down() {
    run(kResetVoltage);
  }

  public boolean isRed() {
    return m_cargoIsRed;
  }

  public boolean isBlue() {
    return m_cargoIsBlue;
  }

  public boolean isCorrectCargo() {
    boolean currentCargo;
    if((m_cargoIsRed == true) || (m_cargoIsBlue == true)) {
      if(((m_cargoIsRed == true) &&
          (DriverStation.getAlliance() == Alliance.Red)) ||
         ((m_cargoIsBlue == true) &&
          (DriverStation.getAlliance() == Alliance.Blue))) {
        currentCargo = true;
      } else {
        currentCargo = false;
      }
    } else{
      currentCargo = false;
    }
    return m_correctDebouncer.calculate(currentCargo);
  }

  public boolean isIncorrectCargo() {
    boolean currentCargo;
    if((m_cargoIsRed == true) || (m_cargoIsBlue == true)) {
      if(((m_cargoIsRed == true) &&
          (DriverStation.getAlliance() == Alliance.Blue)) ||
         ((m_cargoIsBlue == true) &&
          (DriverStation.getAlliance() == Alliance.Red))) {
        currentCargo = true;
      } else {
        currentCargo = false;
      }
    } else{
      currentCargo = false;
    }
    return m_incorrectDebouncer.calculate(currentCargo);
  }

  @Override
  public void periodic() {
    Color detectedColor;
    int distance;

    if(m_colorSensor.isConnected()) {
      detectedColor = m_colorSensor.getColor();
      distance = m_colorSensor.getProximity();
    } else {
      detectedColor = new Color(0, 0, 0);
      distance = 0;
    }
    ColorMatchResult match = m_colorMatcher.matchColor(detectedColor);

    if((match != null) && (match.color == kRedCargo) &&
       (distance > kDistance)) {
      m_cargoIsRed = true;
    } else {
      m_cargoIsRed = false;
    }

    if((match != null) && (match.color == kBlueCargo) &&
       (distance > kDistance)) {
      m_cargoIsBlue = true;
    } else {
      m_cargoIsBlue = false;
    }

    if(m_colorSensor.isConnected() && m_colorSensor.hasReset()) {
      m_colorSensor = new ColorSensorV3(Port.kMXP);
    }

    if(kEnableDetailedLogging) {
      m_logPosition.append(m_encoder.getPosition());
      m_logOutput.append(m_catapult.getAppliedOutput());
      m_logBusVoltage.append(m_catapult.getBusVoltage());
      m_logCurrent.append(m_catapult.getOutputCurrent());
    }
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    if(m_colorSensor.isConnected()) {
      builder.addDoubleProperty("Left Catapult Sensor Distance", () -> m_colorSensor.getProximity(), null);
    }
    //builder.addDoubleProperty("Left Catapult Limit from Vision", () -> m_vision.LeftCatapultPitchToLim(), null);
    builder.addBooleanProperty("Left Blue", () -> isBlue(), null);
    builder.addBooleanProperty("Left Red", () -> isRed(), null);
  }
}
