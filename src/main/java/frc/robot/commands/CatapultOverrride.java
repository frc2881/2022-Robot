// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.RightCatapult;

public class CatapultOverrride extends CommandBase {
  private LeftCatapult m_leftCatapult;
  private RightCatapult m_rightCatapult;
  
  public CatapultOverrride(LeftCatapult leftCatapult, RightCatapult rightCatapult) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_leftCatapult = leftCatapult;
    m_rightCatapult = rightCatapult;

    addRequirements(m_leftCatapult);
    addRequirements(m_rightCatapult);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_leftCatapult.disableEncoderSoftLimit();
    m_rightCatapult.disableEncoderSoftLimit();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_leftCatapult._run(-.1);
    m_rightCatapult._run(-.1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_leftCatapult.enableEncoderSoftLimit();
    m_leftCatapult.resetEncoder();
    m_rightCatapult.enableEncoderSoftLimit();
    m_rightCatapult.resetEncoder();

    m_leftCatapult._run(0.0);
    m_rightCatapult._run(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
