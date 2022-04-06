// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.catapult;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.feedback.RumbleYes;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.PrettyLights;
import frc.robot.subsystems.RightCatapult;
import frc.robot.subsystems.VisionTracking;

import static frc.robot.Constants.Catapult.kShootTimeDelay;
import static frc.robot.Constants.Catapult.kShootTimeDelayNear;
import static frc.robot.Constants.Catapult.kShootTimeDelayFar;

public class ScoreNoColor extends SequentialCommandGroup {
  public ScoreNoColor(LeftCatapult leftCatapult, RightCatapult rightCatapult,
                      PrettyLights prettyLights,
                      XboxController manipulatorController, Intake intake, VisionTracking vision) {
                        
   if(intake != null){
        addCommands(new InstantCommand(() -> intake.extend(), intake));
    }
    
    addCommands(
    parallel(new ScoreLeftNoColor(leftCatapult),  
    sequence(//new WaitCommand(kShootTimeDelay),
    new ConditionalCommand(new WaitCommand(kShootTimeDelayNear), new WaitCommand(kShootTimeDelayFar), () -> smallLim(vision)),
    new ScoreRightNoColor(rightCatapult))),
    new RumbleYes(prettyLights, null, manipulatorController));
    
    if(intake != null){
      addCommands(new InstantCommand(() -> intake.retract(), intake));
    }
  }
  public boolean smallLim(VisionTracking vision){
    if(vision.LeftCatapultPitchToLim() < 5.4){
      return true;
    }
    else{
      return false;
    }
  }
}
