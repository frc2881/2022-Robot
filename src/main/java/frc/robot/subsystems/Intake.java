// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.kEnableDetailedLogging;
import static frc.robot.Constants.Intake.kCurrentLimit;
import static frc.robot.Constants.Intake.kMaxSpeed;
import static frc.robot.Constants.Intake.kMotor;
import static frc.robot.Constants.Intake.kSolenoid;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.datalog.BooleanLogEntry;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  private final CANSparkMax m_intake;
  private final Solenoid m_solenoid;
  private final DoubleLogEntry m_logOutput;
  private final DoubleLogEntry m_logBusVoltage;
  private final DoubleLogEntry m_logCurrent;
  private final BooleanLogEntry m_logSolenoid;
  private int counter = 0;

  public Intake() {
    m_intake = new CANSparkMax(kMotor, MotorType.kBrushless);
    m_intake.restoreFactoryDefaults();
    m_intake.setInverted(false);
    m_intake.setIdleMode(IdleMode.kBrake);
    m_intake.setSmartCurrentLimit(kCurrentLimit);

    m_solenoid = new Solenoid(PneumaticsModuleType.REVPH, kSolenoid);

    if(kEnableDetailedLogging) {
      DataLog log = DataLogManager.getLog();
      m_logOutput = new DoubleLogEntry(log, "/intake/output");
      m_logBusVoltage = new DoubleLogEntry(log, "/intake/busVoltage");
      m_logCurrent = new DoubleLogEntry(log, "/intake/current");
      m_logSolenoid = new BooleanLogEntry(log, "/intake/solenoid");
    } else {
      m_logOutput = null;
      m_logBusVoltage = null;
      m_logCurrent = null;
      m_logSolenoid = null;
    }
  }

  @Override
  public void periodic() {
    if(kEnableDetailedLogging) {
      m_logOutput.append(m_intake.getAppliedOutput());
      m_logBusVoltage.append(m_intake.getBusVoltage());
      m_logCurrent.append(m_intake.getOutputCurrent());
    }
  }

  public void reset() {
    run(0.0);
    counter = 1; 
    retract();
  }

  public void run(double speed) {
    if(speed > kMaxSpeed) {
      speed = kMaxSpeed;
    }
    m_intake.set(-speed);
  }

  public void runReverse(double speed) {
    m_intake.set(speed);
  }

  public void extend() {
    System.out.println(counter);
    if(counter == 0){
      m_solenoid.set(true);
      if(kEnableDetailedLogging) {
        m_logSolenoid.append(m_solenoid.get());
      }
    }
    counter++;
  }

  public void retract() {
    System.out.println(counter);
    if(counter > 0){
    counter--;
    }
    if(counter == 0){ 
      m_solenoid.set(false);
      if(kEnableDetailedLogging) {
        m_logSolenoid.append(m_solenoid.get());
      }
    }
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);

    builder.addDoubleProperty("Current", () -> m_intake.getOutputCurrent(),
                              null);
  }
}
