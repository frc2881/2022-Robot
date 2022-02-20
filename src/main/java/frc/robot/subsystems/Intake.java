// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  private CANSparkMax intake;
  private Solenoid solenoid;

  public enum Direction { INTAKE, EJECT }
  public enum State { EXTEND, RETRACT }

  public Intake() {
    intake = new CANSparkMax(15, MotorType.kBrushless);
        intake.restoreFactoryDefaults();
        intake.setInverted(false);
        intake.setIdleMode(IdleMode.kBrake);
        intake.setSmartCurrentLimit(16);

    solenoid = new Solenoid(PneumaticsModuleType.REVPH, 0);
  }

  public void run(double speed, Direction state) {
    if(state == Direction.INTAKE) {
      intake.set(-speed);
    } else {
      intake.set(speed);
    }
  }

  public void extend() {
    solenoid.set(true);
  }

  public void retract() {
    solenoid.set(false);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);

    builder.addDoubleProperty("Current", () -> intake.getOutputCurrent(),  null);
  }
}
