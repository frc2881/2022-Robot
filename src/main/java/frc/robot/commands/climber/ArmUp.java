// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Climber;

public class ArmUp extends InstantCommand {
  private Climber m_climber;

  /** Creates a new ArmUp. */
  public ArmUp(Climber climber) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_climber = climber;
    addRequirements(m_climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_climber.armUp();
    

  }
}
