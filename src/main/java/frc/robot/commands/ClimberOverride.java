// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class ClimberOverride extends CommandBase {
  private Climber m_climber;
  DoubleSupplier m_speed;

  /** Creates a new SetClimberZero. */
  public ClimberOverride(Climber climber, DoubleSupplier speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_climber = climber;
    m_speed = speed;

    addRequirements(climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_climber.disableEncoderSoftLimit();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_climber._moveArm(m_speed.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_climber.enableEncoderSoftLimit();
    m_climber.resetEncoder();

    m_climber._moveArm(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
