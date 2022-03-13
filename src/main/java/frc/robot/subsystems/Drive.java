// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.kEnableDetailedLogging;
import static frc.robot.Constants.Drive.kCurrentLimit;
import static frc.robot.Constants.Drive.kLeftFrontMotor;
import static frc.robot.Constants.Drive.kLeftRearMotor;
import static frc.robot.Constants.Drive.kMotorSafetyTime;
import static frc.robot.Constants.Drive.kRampRate;
import static frc.robot.Constants.Drive.kRightFrontMotor;
import static frc.robot.Constants.Drive.kRightRearMotor;
import static frc.robot.Constants.Drive.kRotationsToMeters;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.NavX;

public class Drive extends SubsystemBase {
  private final NavX m_navx;
  private final CANSparkMax m_leftFront;
  private final CANSparkMax m_leftRear;
  private final CANSparkMax m_rightFront;
  private final CANSparkMax m_rightRear;
  private final DifferentialDrive m_driveTrain;
  private final RelativeEncoder m_leftEncoder;
  private final RelativeEncoder m_rightEncoder;
  private final DifferentialDriveOdometry m_odometry;
  private final Field2d m_field = new Field2d();
  private final DoubleLogEntry m_logLeftPosition;
  private final DoubleLogEntry m_logLeftVelocity;
  private final DoubleLogEntry m_logLeftFrontOutput;
  private final DoubleLogEntry m_logLeftFrontBusVoltage;
  private final DoubleLogEntry m_logLeftFrontCurrent;
  private final DoubleLogEntry m_logLeftRearOutput;
  private final DoubleLogEntry m_logLeftRearBusVoltage;
  private final DoubleLogEntry m_logLeftRearCurrent;
  private final DoubleLogEntry m_logRightPosition;
  private final DoubleLogEntry m_logRightVelocity;
  private final DoubleLogEntry m_logRightFrontOutput;
  private final DoubleLogEntry m_logRightFrontBusVoltage;
  private final DoubleLogEntry m_logRightFrontCurrent;
  private final DoubleLogEntry m_logRightRearOutput;
  private final DoubleLogEntry m_logRightRearBusVoltage;
  private final DoubleLogEntry m_logRightRearCurrent;

  public Drive(NavX navx) {
    m_navx = navx;

    m_leftFront = new CANSparkMax(kLeftFrontMotor, MotorType.kBrushless);
    m_leftFront.restoreFactoryDefaults();
    m_leftFront.setInverted(true);
    m_leftFront.setIdleMode(IdleMode.kBrake);
    m_leftFront.setSmartCurrentLimit(kCurrentLimit);
    m_leftFront.setOpenLoopRampRate(kRampRate);

    m_leftRear = new CANSparkMax(kLeftRearMotor, MotorType.kBrushless);
    m_leftRear.restoreFactoryDefaults();
    m_leftRear.setInverted(true);
    m_leftRear.setIdleMode(IdleMode.kBrake);
    m_leftRear.setSmartCurrentLimit(kCurrentLimit);
    m_leftRear.setOpenLoopRampRate(kRampRate);
    m_leftRear.follow(m_leftFront);

    m_rightFront = new CANSparkMax(kRightFrontMotor, MotorType.kBrushless);
    m_rightFront.restoreFactoryDefaults();
    m_rightFront.setInverted(false);
    m_rightFront.setIdleMode(IdleMode.kBrake);
    m_rightFront.setSmartCurrentLimit(kCurrentLimit);
    m_rightFront.setOpenLoopRampRate(kRampRate);

    m_rightRear = new CANSparkMax(kRightRearMotor, MotorType.kBrushless);
    m_rightRear.restoreFactoryDefaults();
    m_rightRear.setInverted(false);
    m_rightRear.setIdleMode(IdleMode.kBrake);
    m_rightRear.setSmartCurrentLimit(kCurrentLimit);
    m_rightRear.setOpenLoopRampRate(kRampRate);
    m_rightRear.follow(m_rightFront);

    m_driveTrain = new DifferentialDrive(m_leftFront, m_rightFront);
    m_driveTrain.setExpiration(kMotorSafetyTime);

    m_leftEncoder = m_leftRear.getEncoder();
    m_rightEncoder = m_rightRear.getEncoder();

    m_leftEncoder.setPositionConversionFactor(kRotationsToMeters);
    m_rightEncoder.setPositionConversionFactor(kRotationsToMeters);

    m_leftEncoder.setVelocityConversionFactor(kRotationsToMeters / 60);
    m_rightEncoder.setVelocityConversionFactor(kRotationsToMeters / 60);

    resetEncoders();

    m_odometry = new DifferentialDriveOdometry(navx.getRotation2d());
    SmartDashboard.putData("Field", m_field);

    if(kEnableDetailedLogging) {
      DataLog log = DataLogManager.getLog();
      m_logLeftPosition = new DoubleLogEntry(log, "drive/leftPosition");
      m_logLeftVelocity = new DoubleLogEntry(log, "drive/leftVelocity");
      m_logLeftFrontOutput = new DoubleLogEntry(log, "/drive/leftFrontOutput");
      m_logLeftFrontBusVoltage =
        new DoubleLogEntry(log, "/drive/leftFrontBusVoltage");
      m_logLeftFrontCurrent =
        new DoubleLogEntry(log, "/drive/leftFrontCurrent");
      m_logLeftRearOutput = new DoubleLogEntry(log, "/drive/leftRearOutput");
      m_logLeftRearBusVoltage =
        new DoubleLogEntry(log, "/drive/leftRearBusVoltage");
      m_logLeftRearCurrent = new DoubleLogEntry(log, "/drive/leftRearCurrent");
      m_logRightPosition = new DoubleLogEntry(log, "drive/rightPosition");
      m_logRightVelocity = new DoubleLogEntry(log, "drive/rightVelocity");
      m_logRightFrontOutput =
        new DoubleLogEntry(log, "/drive/rightFrontOutput");
      m_logRightFrontBusVoltage =
        new DoubleLogEntry(log, "/drive/rightFrontBusVoltage");
      m_logRightFrontCurrent =
        new DoubleLogEntry(log, "/drive/rightFrontCurrent");
      m_logRightRearOutput = new DoubleLogEntry(log, "/drive/rightRearOutput");
      m_logRightRearBusVoltage =
        new DoubleLogEntry(log, "/drive/rightRearBusVoltage");
      m_logRightRearCurrent =
        new DoubleLogEntry(log, "/drive/rightRearCurrent");
    } else {
      m_logLeftPosition = null;
      m_logLeftVelocity = null;
      m_logLeftFrontOutput = null;
      m_logLeftFrontBusVoltage = null;
      m_logLeftFrontCurrent = null;
      m_logLeftRearOutput = null;
      m_logLeftRearBusVoltage = null;
      m_logLeftRearCurrent = null;
      m_logRightPosition = null;
      m_logRightVelocity = null;
      m_logRightFrontOutput = null;
      m_logRightFrontBusVoltage = null;
      m_logRightFrontCurrent = null;
      m_logRightRearOutput = null;
      m_logRightRearBusVoltage = null;
      m_logRightRearCurrent = null;
    }
  }

  public void reset() {
    arcadeDrive(0.0, 0.0);
  }

  public void arcadeDrive(double speed, double rotation) {
    m_driveTrain.arcadeDrive(speed, rotation);
  }

  @Override
  public void periodic() {
    m_odometry.update(m_navx.getRotation2d(), m_leftEncoder.getPosition(),
                      m_rightEncoder.getPosition());
    m_field.setRobotPose(m_odometry.getPoseMeters());

    if(kEnableDetailedLogging) {
      m_logLeftPosition.append(m_leftEncoder.getPosition());
      m_logLeftVelocity.append(m_leftEncoder.getVelocity());
      m_logLeftFrontOutput.append(m_leftFront.getAppliedOutput());
      m_logLeftFrontBusVoltage.append(m_leftFront.getBusVoltage());
      m_logLeftFrontCurrent.append(m_leftFront.getOutputCurrent());
      m_logLeftRearOutput.append(m_leftRear.getAppliedOutput());
      m_logLeftRearBusVoltage.append(m_leftRear.getBusVoltage());
      m_logLeftRearCurrent.append(m_leftRear.getOutputCurrent());
      m_logRightPosition.append(m_rightEncoder.getPosition());
      m_logRightVelocity.append(m_rightEncoder.getVelocity());
      m_logRightFrontOutput.append(m_rightFront.getAppliedOutput());
      m_logRightFrontBusVoltage.append(m_rightFront.getBusVoltage());
      m_logRightFrontCurrent.append(m_rightFront.getOutputCurrent());
      m_logRightRearOutput.append(m_rightRear.getAppliedOutput());
      m_logRightRearBusVoltage.append(m_rightRear.getBusVoltage());
      m_logRightRearCurrent.append(m_rightRear.getOutputCurrent());
    }
  }

  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  public void resetEncoders() {
    m_leftEncoder.setPosition(0);
    m_rightEncoder.setPosition(0);
  }

  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    m_odometry.resetPosition(pose, m_navx.getRotation2d());
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(m_leftEncoder.getVelocity(),
                                            m_rightEncoder.getVelocity());
  }

  public void driveTankVolts(double leftVolts, double rightVolts) {
    m_leftFront.setVoltage(leftVolts);
    m_rightFront.setVoltage(rightVolts);
    m_driveTrain.feed();
  }
}
