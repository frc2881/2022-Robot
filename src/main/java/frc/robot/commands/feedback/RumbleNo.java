// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.feedback;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.PrettyLights;

public class RumbleNo extends CommandBase {
  private PrettyLights prettylights;
  XboxController m_controller;
  double time = 0;

  /** Creates a new RumbleNo. */
  public RumbleNo(PrettyLights prettylights, XboxController controller) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_controller = controller;
    this.prettylights = prettylights;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_controller.setRumble(RumbleType.kLeftRumble, 1);
    time = System.currentTimeMillis();
    prettylights.redColor();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if((System.currentTimeMillis() - time) >= 400) {
      m_controller.setRumble(RumbleType.kLeftRumble, 1);
      prettylights.redColor();
    } else if((System.currentTimeMillis() - time) >= 200) {
      m_controller.setRumble(RumbleType.kLeftRumble, 0);
      prettylights.defaultColor();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_controller.setRumble(RumbleType.kLeftRumble, 0);
    prettylights.defaultColor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if((System.currentTimeMillis() - time) >= 600) {
      return true;
    } else {
      return false;
    }
  }
}
