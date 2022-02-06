package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Intake_Catapult extends SubsystemBase {

    private CANSparkMax intake;
    private CANSparkMax leftCatapult;
    private CANSparkMax rightCatapult;
    private Solenoid intakeSolenoid;

    public enum Direction {INTAKE, EJECT}

    public Intake_Catapult(){
      
        intake = new CANSparkMax(15, MotorType.kBrushless);
            intake.setInverted(false);
            intake.setIdleMode(IdleMode.kBrake);
            intake.setSmartCurrentLimit(16);

        //two motor controllers, one follows the other
        /*
        leftCatapult = new CANSparkMax(16, MotorType.kBrushless);
            leftCatapult.setInverted(false);
            leftCatapult.setIdleMode(IdleMode.kBrake);
        
        rightCatapult = new CANSparkMax(17, MotorType.kBrushless);
            rightCatapult.setInverted(false);
            rightCatapult.setIdleMode(IdleMode.kBrake);
            */

        intakeSolenoid = new Solenoid(PneumaticsModuleType.REVPH, 0);

    }

    public void intake(double speed, Direction state){
        if(state == Direction.INTAKE)
        intake.set(-speed);
        else{
            intake.set(speed);
        }
    }

    public void leftCatapult(double speed){
        leftCatapult.set(speed);
    }

    public void rightCatapult(double speed){
        rightCatapult.set(-speed);
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
        builder.addDoubleProperty("Current", () -> intake.getOutputCurrent(),  null);
    }

}