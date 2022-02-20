// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Drive;

public class FollowTrajectory extends CommandBase {
  private final Drive m_drive;
  private final Trajectory m_trajectory;
  private final RamseteCommand m_ramsete;

  public FollowTrajectory(Drive drive, Trajectory trajectory) {
    m_drive = drive;
    m_trajectory = trajectory;

    addRequirements(m_drive);

    RamseteController ramsetecontroller = new RamseteController(Constants.Drive.kRamseteB, Constants.Drive.kRamseteZeta);
    SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(Constants.Drive.kS, Constants.Drive.kV, Constants.Drive.kA);
    PIDController pidleft = new PIDController(Constants.Drive.kPDriveVel, 0, Constants.Drive.kDDriveVel);
    PIDController pidright = new PIDController(Constants.Drive.kPDriveVel, 0, Constants.Drive.kDDriveVel);

    m_ramsete = new RamseteCommand(trajectory, drive::getPose, ramsetecontroller, feedforward, Constants.Drive.kKinematics,
    drive::getWheelSpeeds, pidleft, pidright, drive::driveTankVolts, drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // Reset the drive odometry to the initial pose of the trajectory.
    m_drive.resetOdometry(m_trajectory.getInitialPose());

    m_ramsete.initialize();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_ramsete.execute();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_ramsete.end(interrupted);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_ramsete.isFinished();
  }
}
