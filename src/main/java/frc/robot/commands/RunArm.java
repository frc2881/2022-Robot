// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;
import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class RunArm extends CommandBase {
  private final DoubleSupplier m_speed;

  private Climber m_climber;

  
  /** Creates a new RunArm. */
  public RunArm(DoubleSupplier speed, Climber climber) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_speed = speed;

    m_climber = climber;

    addRequirements(m_climber);


  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_climber.moveArm(m_speed.getAsDouble());

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_climber.moveArm(0.0);

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
