// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.


package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class RumbleYes extends CommandBase {
  XboxController m_controller;
  double time = 0;

  /** Creates a new RumbleYes. */
  public RumbleYes(XboxController controller) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_controller = controller;
    
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_controller.setRumble(RumbleType.kLeftRumble, 1);
    time = System.currentTimeMillis();
  }
  
  
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_controller.setRumble(RumbleType.kLeftRumble, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
  if (System.currentTimeMillis() - time >= 20){
    return true;

  } else {
    return false;
  }


  }
}
