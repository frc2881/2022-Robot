// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.kEnableDetailedLogging;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pneumatics extends SubsystemBase {
  private final PneumaticHub m_ph = new PneumaticHub();
  private final DoubleLogEntry m_logCurrent;

  /** Creates a new Pnuematics subsystem. */
  public Pneumatics() {
    m_ph.enableCompressorDigital();
    if(kEnableDetailedLogging) {
      DataLog log = DataLogManager.getLog();
      m_logCurrent = new DoubleLogEntry(log, "/pneumatics/compressorCurrent");
    } else {
      m_logCurrent = null;
    }
  }

  @Override
  public void periodic() {
    if(kEnableDetailedLogging) {
      m_logCurrent.append(m_ph.getCompressorCurrent());
    }
  }
}
