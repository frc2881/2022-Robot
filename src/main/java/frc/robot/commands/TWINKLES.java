// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.PrettyLights;

public class TWINKLES extends CommandBase {
  private PrettyLights m_prettyLights;
  /** Creates a new TWINKLES. */
  public TWINKLES(PrettyLights prettyLights) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_prettyLights = prettyLights;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    DriverStation.Alliance alliance = DriverStation.getAlliance();
    if(alliance == DriverStation.Alliance.Blue || alliance == DriverStation.Alliance.Red) {
      m_prettyLights.color(PrettyLights.hotPink, 0);
      
    } 
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
