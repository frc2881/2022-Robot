// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Climber;
import frc.robot.utils.NavX;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SecondClimberSequence extends SequentialCommandGroup {
  public SecondClimberSequence(Climber climber, NavX navx) {
    addCommands(
        new ArmToLength(climber, -.6, 21.8),
        new ArmBack(climber),
        new ArmToLength(climber, -.6, 17.5),
        new ArmUp(climber),
        new ArmToLength(climber, -.6, 5.0),
        new ArmToLength(climber, -.4, .25),
        new WaitCommand(.5),
        new ArmToLength(climber, 1.0, 5.0),
        new ArmBack(climber),
        //new ArmWaitToExtend(navx),
        new ArmToLength(climber, 1.0, 25.5),
        new ArmUp(climber)
      /*new WaitCommand(10),
      new ArmToLength(climber, .3, 5.0),
      new ArmBack(climber),
      //new ArmWaitToExtend(navx),
      new ArmToLength(climber, .3, 25.5),
      new ArmUp(climber),
      new ArmBack(climber),
      new ArmToLength(climber, -0.3, 13.0),
      new ArmUp(climber),
      new ArmToLength(climber, -.3, .5)*/
    );
  }
}
