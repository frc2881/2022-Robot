// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.catapult;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.feedback.RumbleYes;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.PrettyLights;
import frc.robot.subsystems.RightCatapult;
import static frc.robot.Constants.Catapult.kShootTimeDelay;

public class Score extends SequentialCommandGroup {
  public Score(LeftCatapult leftCatapult, RightCatapult rightCatapult,
               PrettyLights prettyLights,
               XboxController manipulatorController) {
    addCommands(parallel(new ScoreLeft(leftCatapult),
                sequence(new WaitCommand(kShootTimeDelay), new ScoreRight(rightCatapult))),
                new RumbleYes(prettyLights, null, manipulatorController));
  }
}
