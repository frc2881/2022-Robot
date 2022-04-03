// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.kEnableDetailedLogging;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.motorcontrol.PWMMotorController;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PrettyLights extends SubsystemBase {
  private final double hotPink = 0.57;
  private final double green = 0.77;
  private final double red = 0.61;
  private final double yellow = 0.69;
  private final double rainbow = -0.97;
  private final double confetti = -0.87;
  private final double twinkles = -0.55;
  private final double colorwave = -0.45;
  private final double sinelon = -0.77;
  private final PowerDistribution m_powerHub;
  private final Spark m_lights0;
  private final Spark m_lights1;
  private boolean m_useDefault = true;
  private final DoubleLogEntry m_logOutput;
  private final DoubleLogEntry m_logCurrent;
  private double m_defaultColor = hotPink;
  PWMMotorController m_lights;

  /** Creates a new PrettyLights. */
  public PrettyLights(PowerDistribution powerHub) {
    m_powerHub = powerHub;

    m_lights0 = new Spark(0);
    m_lights1 = new Spark(1);

    m_lights0.set(m_defaultColor);
    m_lights1.set(m_defaultColor);

    if(kEnableDetailedLogging) {
      DataLog log = DataLogManager.getLog();
      m_logOutput = new DoubleLogEntry(log, "/prettyLights/output");
      m_logCurrent = new DoubleLogEntry(log, "/prettyLights/current");
    } else {
      m_logOutput = null;
      m_logCurrent = null;
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if(m_useDefault == true) {
      if((DriverStation.getMatchTime() <= 35) &&
         (DriverStation.getMatchTime() > 25)) {
        m_lights0.set(yellow);
      } else {
        m_lights0.set(m_defaultColor);
      }
    }

    if(kEnableDetailedLogging) {
      m_logOutput.append(m_lights0.get());
      m_logOutput.append(m_lights1.get());
      m_logCurrent.append(m_powerHub.getCurrent(11));
    }
  }

  public void defaultColor() {
    m_useDefault = true;
  }

  public void greenColor() {
    m_useDefault = false;
    m_lights0.set(green);
  }

  public void redColor() {
    m_useDefault = false;
    m_lights0.set(red);
  }

  public void partyColor() {
    m_useDefault = false;
    m_lights0.set(rainbow);
  }

  public boolean isPartyColor(){
    if(m_lights.get() == rainbow){
      return true;
    } else {
      return false;
    }
  }
  public void lightShow(){
    m_defaultColor = confetti;
  }

  public void reset() {
    m_defaultColor = hotPink;
    m_lights0.set(m_defaultColor);
  }
}
