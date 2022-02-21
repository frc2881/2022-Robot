// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.Drive.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.util.sendable.SendableBuilder;
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

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Left Position",
                              () -> m_leftEncoder.getPosition(),  null);
    builder.addDoubleProperty("Right Position",
                              () -> m_rightEncoder.getPosition(),  null);
    builder.addDoubleProperty("Left Velocity",
                              () -> m_leftEncoder.getVelocity(),  null);
    builder.addDoubleProperty("Right Velocity",
                              () -> m_rightEncoder.getVelocity(),  null);
  }
}
