// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PrettyLights extends SubsystemBase {

  public final double hotPink = 0.57;
  public final double green = 0.77;
  public final double red = 0.61;
  public final double yellow = 0.69;
  private boolean defaultColor = true;
  private final Spark lights;

  /** Creates a new PrettyLights. */
  public PrettyLights() {

    lights = new Spark(0);

    lights.set(hotPink);
  }

  //enabled Solid Pink
  //(no code) disabled heartbeat pink
  //Intake red cargo Solid red (1-2 seconds)
  //Intake blue cargo Solid blue (1-2 seconds)
  //flash green once
  //flash red twice

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if(defaultColor == true){
        if(DriverStation.getMatchTime() <= 45 && DriverStation.getMatchTime() > 43){
            lights.set(yellow);
        } else {
            lights.set(hotPink);
        }
    }

  }


  public void defaultColor(){
    defaultColor = true;
  }
  public void greenColor(){
    defaultColor = false;
    lights.set(green);
  }
  public void redColor(){
    defaultColor = false;
    lights.set(red);
  }

}
