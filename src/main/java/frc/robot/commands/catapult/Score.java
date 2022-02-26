// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.catapult;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.RightCatapult;
import frc.robot.subsystems.PrettyLights;

public class Score extends ParallelCommandGroup {
  public Score(LeftCatapult leftCatapult, RightCatapult rightCatapult, XboxController controller, PrettyLights prettyLights) {
    addCommands(
      new ScoreLeft(leftCatapult, controller, prettyLights),
      sequence(new WaitCommand(0.15), new ScoreRight(rightCatapult, controller, prettyLights))
    );
  }
}
