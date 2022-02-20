// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.LeftCatapult;
import frc.robot.subsystems.RightCatapult;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class Eject extends SequentialCommandGroup {
  /** Creates a new Eject. */
  public Eject(LeftCatapult leftCatapult, RightCatapult rightCatapult) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      
      new ConditionalCommand(new EjectRight(rightCatapult).withTimeout(1).andThen(new ResetRight(rightCatapult).withTimeout(2)), 
      new ConditionalCommand(new EjectLeft(leftCatapult).withTimeout(1).andThen(new ResetLeft(leftCatapult).withTimeout(2)), 
                            new WaitCommand(0.00001), 
                            () -> EjectLeftCargo(leftCatapult)), 
      () -> EjectRightCargo(rightCatapult))


    );
  }
  public boolean EjectRightCargo(RightCatapult rightCatapult){
    if(((DriverStation.getAlliance() == Alliance.Red) && (rightCatapult.isBlue() == true))
    || ((DriverStation.getAlliance() == Alliance.Blue) && (rightCatapult.isRed() == true))){
      return true;
    } else {
      return false;
    }
  }

  public boolean EjectLeftCargo(LeftCatapult leftCatapult){
    if (((DriverStation.getAlliance() == Alliance.Red) && (leftCatapult.isBlue() == true)) 
    || ((DriverStation.getAlliance() == Alliance.Blue) && (leftCatapult.isRed() == true))) {
      return true;
    } else {
      return false;
    }
  }  
}
