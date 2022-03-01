// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.feedback;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class WaitCommandNT extends CommandBase {
  /** Creates a new WaitCommand. */
  String m_ntEntry;
  Double m_ntNum;
  long time; 
  public WaitCommandNT(String ntEntry) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_ntEntry = ntEntry;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize(){
    m_ntNum = SmartDashboard.getNumber(m_ntEntry, 0) * 1000;
    time = System.currentTimeMillis();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if((System.currentTimeMillis() - time) >= m_ntNum) {
      return true;
    } else {
      return false;
    }
  }
}
