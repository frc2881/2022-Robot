// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.GearRatio;
import frc.robot.utils.NavX;

public class Drive extends SubsystemBase {
  private final NavX m_navx;
  private final RelativeEncoder leftenc;
  private final RelativeEncoder rightenc;
  private final DifferentialDriveOdometry odometry;

  private final CANSparkMax leftFront;
  private final CANSparkMax leftRear;
  private final CANSparkMax rightFront;
  private final CANSparkMax rightRear;

  private final DifferentialDrive driveTrain;

  public Drive(NavX navx) {
    m_navx = navx;

    leftFront = new CANSparkMax(11, MotorType.kBrushless);
        leftFront.restoreFactoryDefaults();
        leftFront.setInverted(false);
        leftFront.setIdleMode(IdleMode.kBrake);
        leftFront.setSmartCurrentLimit(Constants.Drive.kCurrentLimit);
        leftFront.setOpenLoopRampRate(0.08);

    leftRear = new CANSparkMax(12, MotorType.kBrushless);
        leftRear.restoreFactoryDefaults();
        leftRear.setInverted(false);
        leftRear.setIdleMode(IdleMode.kBrake);
        leftRear.setSmartCurrentLimit(Constants.Drive.kCurrentLimit);
        leftRear.setOpenLoopRampRate(0.08);

    rightFront = new CANSparkMax(13, MotorType.kBrushless);
        rightFront.restoreFactoryDefaults();
        rightFront.setInverted(true);
        rightFront.setIdleMode(IdleMode.kBrake);
        rightFront.setSmartCurrentLimit(Constants.Drive.kCurrentLimit);
        rightFront.setOpenLoopRampRate(0.08);

    rightRear = new CANSparkMax(14, MotorType.kBrushless);
        rightRear.restoreFactoryDefaults();
        rightRear.setInverted(true);
        rightRear.setIdleMode(IdleMode.kBrake);
        rightRear.setSmartCurrentLimit(Constants.Drive.kCurrentLimit);
        rightRear.setOpenLoopRampRate(0.08);

    driveTrain = new DifferentialDrive(leftFront, rightFront);

    leftenc = leftRear.getEncoder();
    rightenc = rightRear.getEncoder();

    leftenc.setPositionConversionFactor(GearRatio.measureDist(1, 10, 50, 4));
    rightenc.setPositionConversionFactor(GearRatio.measureDist(1, 10, 50, 4));

    leftenc.setVelocityConversionFactor(GearRatio.measureDist(1, 10, 50, 4) / 60);
    rightenc.setVelocityConversionFactor(GearRatio.measureDist(1, 10, 50, 4) / 60);

    leftRear.follow(leftFront);
    rightRear.follow(rightFront);

    resetEncoders();

    odometry = new DifferentialDriveOdometry(navx.getRotation2d());
  }

  public void arcadeDrive(double speed, double rotation) {
    driveTrain.arcadeDrive(speed, rotation);
  }

  public void tankDrive(double leftSpeed, double rightSpeed) {
    driveTrain.tankDrive(leftSpeed, rightSpeed);
  }

  public void setMaxOutput(double maxOutput) {
    driveTrain.setMaxOutput(maxOutput);
  }

  @Override
  public void periodic() {
    odometry.update(m_navx.getRotation2d(), -leftenc.getPosition(), -rightenc.getPosition());
  }

  public Pose2d getPose() {
    return odometry.getPoseMeters();
  }

  public void resetEncoders()
  {
    leftenc.setPosition(0);
    rightenc.setPosition(0);
  }

  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    odometry.resetPosition(pose, m_navx.getRotation2d());
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(-leftenc.getVelocity(),
                                            -rightenc.getVelocity());
  }

  public void driveTankVolts(double leftVolts, double rightVolts) {
    leftFront.setVoltage(-leftVolts);
    rightFront.setVoltage(-rightVolts);
    driveTrain.feed();
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Left Position", () -> -leftenc.getPosition(),  null);
    builder.addDoubleProperty("Right Position", () -> -rightenc.getPosition(),  null);
    builder.addDoubleProperty("Left Velocity", () -> -leftenc.getVelocity(),  null);
    builder.addDoubleProperty("Right Velocity", () -> -rightenc.getVelocity(),  null);
  }
}
