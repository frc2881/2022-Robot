package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Intake extends SubsystemBase {

    private CANSparkMax intake_feeder;
    private Solenoid intakeSolenoid;

    public enum Feeder_Direction {INTAKE, EJECT}
    public enum Intake_Arm_Direction {EXTEND, RETRACT}



    public Intake(){
      
        intake_feeder = new CANSparkMax(15, MotorType.kBrushless);
            intake_feeder.restoreFactoryDefaults();
            intake_feeder.setInverted(false);
            intake_feeder.setIdleMode(IdleMode.kBrake);
            intake_feeder.setSmartCurrentLimit(16);
            
        intakeSolenoid = new Solenoid(PneumaticsModuleType.REVPH, 0);

    }


    public void intake_feed(double speed, Feeder_Direction state){
        if(state == Feeder_Direction.INTAKE)
        intake_feeder.set(-speed);
        else{
            intake_feeder.set(speed);
        }
    }
 

    public void extend(){
        intakeSolenoid.set(true);
    }

    public void retract(){
        intakeSolenoid.set(false);
    }

    @Override
      public void initSendable(SendableBuilder builder) {  
        super.initSendable(builder);

        builder.addDoubleProperty("Current", () -> intake_feeder.getOutputCurrent(),  null);

    }

}