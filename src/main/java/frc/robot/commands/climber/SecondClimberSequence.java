// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.feedback.RumbleYes;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.PrettyLights;
import frc.robot.utils.NavX;

public class SecondClimberSequence extends SequentialCommandGroup {
  public SecondClimberSequence(Climber climber, PrettyLights prettylights, NavX navx, XboxController manipulatorController) {
    addCommands(
        new InstantCommand(() -> climber.setBackTrue(), climber),
        new ArmToLength(climber, -0.3, 24.5), 
        //new ArmBack(climber),
        new ArmToLength(climber, -0.5, 16.0),
        new ArmToLength(climber, -1.0, 6.5), 
        //new ArmUp(climber),
        new ArmToLength(climber, -1.0, 2.5), 
        new ArmToLength(climber, -0.4, 0.0),
<<<<<<< HEAD
        new WaitCommand(0.2),
=======
        new WaitCommand(0.5),
>>>>>>> origin/main
        new ArmToLength(climber, .9, 5.0),
        new ArmBack(climber),
        //new ArmWaitToExtend(navx),
        new ArmToLength(climber, .9, 25.5),
        new ArmUp(climber),
        new WaitCommand(0.15),
        new RumbleYes(prettylights, null, manipulatorController),
        new InstantCommand(() -> prettylights.partyColor(), prettylights) 
    );
  }
}
