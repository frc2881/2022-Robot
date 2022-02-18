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
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class RightCatapult extends SubsystemBase {

    private CANSparkMax rightCatapult;
    private ColorMatchResult matchRight;
    private ColorSensorV3 colorSensorRight = new ColorSensorV3(Port.kOnboard);

    private final ColorMatch colorMatcher = new ColorMatch();
                                            //R       G       B
    private final Color blueCargo = new Color(.1436, .4070, .4499); 
    private final Color redCargo = new Color(.5720, .3222, .1062); 

    private final int distance = 600;

    public enum Right_Catapult_Direction {LAUNCH, RESET}



    public RightCatapult(){
        
        rightCatapult = new CANSparkMax(17, MotorType.kBrushless);
            rightCatapult.restoreFactoryDefaults();
            rightCatapult.setInverted(true);
            rightCatapult.setIdleMode(IdleMode.kBrake);
            rightCatapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
            rightCatapult.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
            rightCatapult.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, (float)4.5);
            rightCatapult.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, (float)0);
            rightCatapult.getEncoder().setPosition(0);
            rightCatapult.setSmartCurrentLimit(80);

        colorMatcher.addColorMatch(blueCargo);
        colorMatcher.addColorMatch(redCargo);
        colorMatcher.setConfidenceThreshold(.95);
    }


    public void rightCatapult(double speed){
        Color detectedColorRight = colorSensorRight.getColor();
        matchRight = colorMatcher.matchColor(detectedColorRight);
        if(DriverStation.getAlliance() == Alliance.Blue && colorSensorRight.getProximity() > distance
        && matchRight.color == blueCargo)
        {
        rightCatapult.set(speed);
        }
        else if(DriverStation.getAlliance() == Alliance.Red && colorSensorRight.getProximity() > distance 
        && matchRight.color == redCargo){
        rightCatapult.set(speed);
        }
        else{
        rightCatapult.set(speed/2);   
        }

    }

    public boolean rightColorBlue(){
        Color detectedColorRight = colorSensorRight.getColor();
        matchRight = colorMatcher.matchColor(detectedColorRight);
        if(matchRight != null && matchRight.color == blueCargo && colorSensorRight.getProximity() > distance){
            return true;
         }
         else {
            return false;
         }
     
    }

    public boolean rightColorRed(){
        Color detectedColorRight = colorSensorRight.getColor();
        matchRight = colorMatcher.matchColor(detectedColorRight);
        if(matchRight != null && matchRight.color == redCargo && colorSensorRight.getProximity() > distance){
            return true;
         }
         else {
            return false;
         }
     
    }

    @Override
      public void initSendable(SendableBuilder builder) {  
        super.initSendable(builder);

        builder.addDoubleProperty("Right Color Sensor Distance", () -> colorSensorRight.getProximity(),  null);   
        //builder.addStringProperty("Right Color", () -> rightColor(),  null);
        builder.addBooleanProperty("Right Blue", () -> rightColorBlue(), null);
        builder.addBooleanProperty("Right Red", () -> rightColorRed(), null);

    }

}