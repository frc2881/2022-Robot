// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.Intake.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  private CANSparkMax m_intake;
  private Solenoid m_solenoid;

  public Intake() {
    m_intake = new CANSparkMax(kMotor, MotorType.kBrushless);
    m_intake.restoreFactoryDefaults();
    m_intake.setInverted(false);
    m_intake.setIdleMode(IdleMode.kBrake);
    m_intake.setSmartCurrentLimit(kCurrentLimit);

    m_solenoid = new Solenoid(PneumaticsModuleType.REVPH, kSolenoid);
  }

  public void reset() {
    run(0.0);
    retract();
  }

  public void run(double speed) {
    if(speed > kMaxSpeed) {
      speed = kMaxSpeed;
    }
    m_intake.set(-speed);
  }

  public void extend() {
    m_solenoid.set(true);
  }

  public void retract() {
    m_solenoid.set(false);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);

    builder.addDoubleProperty("Current", () -> m_intake.getOutputCurrent(),
                              null);
  }
}
