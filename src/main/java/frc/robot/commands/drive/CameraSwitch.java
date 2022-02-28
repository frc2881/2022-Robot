// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.drive;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class CameraSwitch extends CommandBase {
  private final NetworkTableEntry m_cameraFront;

  /** Creates a new SwitchCamera. */
  public CameraSwitch() {
    NetworkTableInstance ntinst = NetworkTableInstance.getDefault();
    NetworkTable table = ntinst.getTable("RPi");
    m_cameraFront = table.getEntry("cameraFront");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_cameraFront.setBoolean(false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_cameraFront.setBoolean(true);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
