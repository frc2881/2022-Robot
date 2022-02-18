package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class LeftCatapult extends SubsystemBase {

    private CANSparkMax leftCatapult;
    private ColorMatchResult matchLeft;
    private ColorSensorV3 colorSensorLeft = new ColorSensorV3(Port.kMXP);

    private final ColorMatch colorMatcher = new ColorMatch();
                                            //R       G       B
    private final Color blueCargo = new Color(.1436, .4070, .4499); 
    private final Color redCargo = new Color(.5720, .3222, .1062); 

    private final int distance = 600;

    public enum Left_Catapult_Direction {LAUNCH, RESET}

    public LeftCatapult(){

        leftCatapult = new CANSparkMax(16, MotorType.kBrushless);
            leftCatapult.restoreFactoryDefaults();
            leftCatapult.setInverted(false);
            leftCatapult.setIdleMode(IdleMode.kBrake);
            leftCatapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
            leftCatapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
            leftCatapult.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, (float)4.5);
            leftCatapult.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, (float)0);
            leftCatapult.getEncoder().setPosition(0);
            leftCatapult.setSmartCurrentLimit(80);

        colorMatcher.addColorMatch(blueCargo);
        colorMatcher.addColorMatch(redCargo);
        colorMatcher.setConfidenceThreshold(.95);





    }

    public void leftCatapult(double speed){
        Color detectedColorLeft = colorSensorLeft.getColor();
        matchLeft = colorMatcher.matchColor(detectedColorLeft);
        
        if(DriverStation.getAlliance() == Alliance.Blue && colorSensorLeft.getProximity() > distance
        && matchLeft.color == blueCargo){
        leftCatapult.set(speed);
        }
        else if(DriverStation.getAlliance() == Alliance.Red && colorSensorLeft.getProximity() > distance 
        && matchLeft.color == redCargo){
        leftCatapult.set(speed);
        }
        else{
        leftCatapult.set(speed/2);
        }
        
    }


    public boolean leftColorBlue(){
        Color detectedColorLeft = colorSensorLeft.getColor();
        matchLeft = colorMatcher.matchColor(detectedColorLeft);
        if(matchLeft != null && matchLeft.color == blueCargo && colorSensorLeft.getProximity() > distance){
            return true;
         }
         else {
            return false;
         }
     
    }

    public boolean leftColorRed(){
        Color detectedColorLeft = colorSensorLeft.getColor();
        matchLeft = colorMatcher.matchColor(detectedColorLeft);
        if(matchLeft != null && matchLeft.color == redCargo && colorSensorLeft.getProximity() > distance){
            return true;
         }
         else {
            return false;
         }
     
    }

    @Override
      public void initSendable(SendableBuilder builder) {  
        super.initSendable(builder);

        builder.addDoubleProperty("Left Color Sensor Distance", () -> colorSensorLeft.getProximity(),  null);
       // builder.addStringProperty("Left Color", () -> leftColor(),  null);
        builder.addBooleanProperty("Left Blue", () -> leftColorBlue(), null);
        builder.addBooleanProperty("Left Red", () -> leftColorRed(), null);

    }

}