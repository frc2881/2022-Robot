// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// runs intake when kB is held on XboxController, ends when kB is released

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.Direction;

public class RunIntake extends CommandBase {
  private Intake intake;

  public RunIntake(Intake intake) {
    this.intake = intake;
  }

  // called when the command is initially scheduled
  @Override
  public void initialize() {
    intake.run(1.0, Direction.INTAKE);
  }

  // called every time the scheduler runs while the command is scheduled
  @Override
  public void execute() {}

  // called once the command ends or is interrupted
  @Override
  public void end(boolean interrupted) {
    intake.run(0.0, Direction.INTAKE);
  }

  // returns false when the command should end
  @Override
  public boolean isFinished() {
    return false;
  }
}
