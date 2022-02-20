// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

/** Add your docs here. */
public class ArmToLength extends CommandBase {
  private Climber m_climber;
  private Double m_speed;
  private Double m_position;

  public ArmToLength(Climber climber, Double speed, Double position) {
    m_climber = climber;
    m_speed = speed;
    m_position = position;
    addRequirements(m_climber);
  }

  @Override
  public void initialize() {
    m_climber.moveArm(m_speed);
  }

  @Override
  public void end(boolean interrupted) {
    m_climber.moveArm(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if((m_speed < 0) && (m_climber.getClimbEncoderPostion() <= m_position)) {
      return true;
    } else if((m_speed > 0) && (m_climber.getClimbEncoderPostion() >= m_position)) {
      return true;
    } else {
      return false;
    }
  }
}
