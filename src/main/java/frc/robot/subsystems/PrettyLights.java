// Copyright (c) 2022 FRC Team 2881 - The Lady Cans
//
// Open Source Software; you can modify and/or share it under the terms of BSD
// license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.kEnableDetailedLogging;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
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
  private final double strobe = -0.05; 
  private final PowerDistribution m_powerHub;
  private final Spark m_feedback;
  private final Spark m_vision;
  private final AddressableLED m_led; 
  private final AddressableLEDBuffer m_ledBuffer;
  int m_rainbowFirstPixelHue;
  int m_firstColor;
  int firstSat = 128;
  int m_rainFirstPixelHue;
  int m_loadingDoDa; 
  int m_offset;
  private boolean m_useDefault = true;
  private final DoubleLogEntry m_logOutput;
  private final DoubleLogEntry m_logCurrent;
  private double m_defaultColor = hotPink;
  PWMMotorController m_lights;

  /** Creates a new PrettyLights. */
  public PrettyLights(PowerDistribution powerHub) {
    m_powerHub = powerHub;

    m_feedback = new Spark(0);
    m_vision = new Spark(1); 

    m_feedback.set(m_defaultColor);
    m_vision.set(m_defaultColor);

    if(kEnableDetailedLogging) {
      DataLog log = DataLogManager.getLog();
      m_logOutput = new DoubleLogEntry(log, "/prettyLights/output");
      m_logCurrent = new DoubleLogEntry(log, "/prettyLights/current");
    } else {
      m_logOutput = null;
      m_logCurrent = null;
    }

    m_led = new AddressableLED(5);
    m_ledBuffer = new AddressableLEDBuffer(16);
    m_led.setLength(( m_ledBuffer).getLength());
    m_led.setData(m_ledBuffer); 
    m_led.start();
  }

  private void rainbow(){
    for(var i=0; i < m_ledBuffer.getLength(); i++){
      final var hue = (m_rainbowFirstPixelHue + (i * 180 / m_ledBuffer.getLength())) % 180;
      m_ledBuffer.setHSV(i, hue, 225, 32);
    }
    m_rainbowFirstPixelHue += 3;
    m_rainbowFirstPixelHue %= 180;
  }
  private void rain(){
    for(var i = 0; i < m_ledBuffer.getLength(); i++){
      final var hue = (m_rainFirstPixelHue + (i * 199 / m_ledBuffer.getLength())) % 199;
      m_ledBuffer.setHSV(i, hue, 255, 32);
        if(i >= 1){
        m_ledBuffer.setHSV(i - 1, hue, 255, 32);
      }
    }
    m_rainFirstPixelHue +=2;
    m_rainFirstPixelHue %= 199;
  }
  private void gradient() {
    for (var i = 0; i < m_ledBuffer.getLength(); i++) { 
    final var hue = (m_firstColor + (i * 80 / m_ledBuffer.getLength())) % 80;
      m_ledBuffer.setHSV(i, hue, 255, 32);
      }
      m_firstColor += 1;
      m_firstColor %= 80;
    }

    private void breathing() {
      for (int i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setHSV(i, 180, 255, firstSat);
      }
      firstSat -= 2;
      if(firstSat <= 0){
        while(firstSat <= 128){
          //System.out.println("I am doing this");
          firstSat += 2;
        }
      }
      }
    
    private void strobe() {
      for (int i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setHSV(i, 0, 240, firstSat);
      }
      firstSat -= 10;
      if(firstSat <= 0){
        while(firstSat <= 128){
          firstSat += 10;
        }
      }
      }

    private void loading(){
      m_offset++;
      if(m_offset % 8 !=0){
        return;
      }
      for(var i = 0; i < m_ledBuffer.getLength(); i++){
        m_ledBuffer.setHSV(i, 0, 0, 0);
      }
        m_ledBuffer.setHSV(((m_offset / 8) + 16 - 1) % 16, 60, 225, 32);   //green
        m_ledBuffer.setHSV(((m_offset / 8) + 16 - 2) % 16, 120, 225, 32);  //blue
        m_ledBuffer.setHSV(((m_offset / 8) + 16 - 3) % 16, 150, 225, 32);  //purple
        m_ledBuffer.setHSV((m_offset / 8) % 16, 30, 225, 32);             //yellow
        m_ledBuffer.setHSV(((m_offset / 8) + 16 + 1) % 16, 19, 225, 32); //orange
        m_ledBuffer.setHSV(((m_offset / 8) + 16 + 2) % 16, 5, 225, 32); //red
    }
    private void pgbop(){
      m_offset++;
      if(m_offset % 8 !=0){
        return;
      }
      for(var i = 0; i < m_ledBuffer.getLength(); i++){
        m_ledBuffer.setHSV(i, 0, 0, 0);
      }
        m_ledBuffer.setHSV(((m_offset / 8) + 16 - 1) % 16, 19, 225, 32);   //o
        m_ledBuffer.setHSV(((m_offset / 8) + 16 - 2) % 16, 140, 225, 32);  //p
        m_ledBuffer.setHSV((m_offset / 8) % 16, 120, 225, 32);             //b
        m_ledBuffer.setHSV(((m_offset / 8) + 16 + 1) % 16, 60, 225, 32); //g
        m_ledBuffer.setHSV(((m_offset / 8) + 16 + 2) % 16, 165, 225, 32); //p
    }
    private void blueallince(){
      m_offset++;
      if(m_offset % 8 !=0){
        return;
      }
      for(var i = 0; i < m_ledBuffer.getLength(); i++){
        m_ledBuffer.setHSV(i, 0, 0, 0);
      }
      m_ledBuffer.setHSV((m_offset / 8) % 16, 120, 225, 32);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 2) % 16, 120, 225, 32);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 4) % 16, 120, 225, 32);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 6) % 16, 120, 225, 32);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 8) % 16, 120, 225, 32);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 10) % 16, 120, 225, 32);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 12) % 16, 120, 225, 32);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 14) % 16, 120, 225, 32);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 16) % 16, 120, 225, 32);
    }
    private void redallince(){
      m_offset++;
      if(m_offset % 8 !=0){
        return;
      }
      for(var i = 0; i < m_ledBuffer.getLength(); i++){
        m_ledBuffer.setHSV(i, 0, 0, 0);
      }
      m_ledBuffer.setHSV((m_offset / 8) % 16, 120, 225, 32);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 2) % 16, 0, 255, 64);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 4) % 16, 0, 255, 64);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 6) % 16, 0, 255, 64);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 8) % 16, 0, 255, 64);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 10) % 16, 0, 255, 64);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 12) % 16, 0, 255, 64);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 14) % 16, 0, 255, 64);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 16) % 16, 0, 255, 64);
    }
    private void pink(){
      m_offset++;
      if(m_offset % 8 !=0){
        return;
      }
      for(var i = 0; i < m_ledBuffer.getLength(); i++){
        m_ledBuffer.setHSV(i, 0, 0, 0);
      }
      m_ledBuffer.setHSV((m_offset / 8) % 16, 120, 225, 32);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 2) % 16, 0, 250, 64);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 4) % 16, 0, 250, 64);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 6) % 16, 0, 250, 64);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 8) % 16, 0, 250, 64);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 10) % 16, 0, 250, 64);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 12) % 16, 0, 250, 64);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 14) % 16, 0, 250, 64);
      m_ledBuffer.setHSV(((m_offset / 8) + 16 + 16) % 16, 0, 250, 64);
    }
    

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if(m_useDefault == true) {
      if((DriverStation.getMatchTime() <= 35) &&
         (DriverStation.getMatchTime() > 25)) {
        m_feedback.set(yellow);
      } else {
        m_feedback.set(m_defaultColor);
      }
    }

    if(kEnableDetailedLogging) {
      m_logOutput.append(m_feedback.get());
      m_logOutput.append(m_vision.get());
      m_logCurrent.append(m_powerHub.getCurrent(11));
    }

    loading();
    m_led.setData(m_ledBuffer);
  }

  public void defaultColor() {
    m_useDefault = true;
  }

  public void greenColor() {
    m_useDefault = false;
    m_feedback.set(green);
  }

  public void redColor() {
    m_useDefault = false;
    m_feedback.set(red);
  }

  public void partyColor() {
    m_useDefault = false;
    m_feedback.set(rainbow);
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
    m_feedback.set(m_defaultColor);
  }
}
