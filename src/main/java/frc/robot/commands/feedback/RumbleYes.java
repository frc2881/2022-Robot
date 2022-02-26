// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.feedback;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.PrettyLights;

public class RumbleYes extends CommandBase {
  private PrettyLights prettylights;
  XboxController m_driverController;
  XboxController m_manipulatorController;
  double time = 0;

  /** Creates a new RumbleYes. */
  public RumbleYes(PrettyLights prettylights, XboxController driverController, XboxController manipulatorController) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_driverController = driverController;
    m_manipulatorController = manipulatorController;
    this.prettylights = prettylights;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(m_driverController != null){
      m_driverController.setRumble(RumbleType.kLeftRumble, 1);
      m_driverController.setRumble(RumbleType.kRightRumble, 1);
    }

    if(m_manipulatorController != null){
      m_manipulatorController.setRumble(RumbleType.kLeftRumble, 1);
      m_manipulatorController.setRumble(RumbleType.kRightRumble, 1);
    }
    time = System.currentTimeMillis();
    prettylights.greenColor();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if(m_driverController != null){
    m_driverController.setRumble(RumbleType.kLeftRumble, 0);
    m_driverController.setRumble(RumbleType.kRightRumble, 0);
    }
    if(m_manipulatorController != null){
    m_manipulatorController.setRumble(RumbleType.kLeftRumble, 0);
    m_manipulatorController.setRumble(RumbleType.kRightRumble, 0);
    }
    prettylights.defaultColor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if((System.currentTimeMillis() - time) >= 200) {
      return true;
    } else {
      return false;
    }
  }
}
