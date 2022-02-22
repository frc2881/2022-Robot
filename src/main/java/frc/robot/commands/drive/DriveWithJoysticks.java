// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.drive;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;

public class DriveWithJoysticks extends CommandBase {
  private final Drive m_drive;
  private final DoubleSupplier m_speed;
  private final DoubleSupplier m_rotation;

  public DriveWithJoysticks(Drive drive, DoubleSupplier speed, DoubleSupplier rotation) {
    m_drive = drive;
    m_speed = speed;
    m_rotation = rotation;

    addRequirements(m_drive);
  }

  @Override
  public void execute() {
    m_drive.arcadeDrive(m_speed.getAsDouble(), m_rotation.getAsDouble());
  }

  @Override
  public void end(boolean interrupted) {
    m_drive.arcadeDrive(0.0, 0.0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
