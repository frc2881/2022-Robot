package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drive extends SubsystemBase {

    private CANSparkMax leftFront;
    private CANSparkMax leftRear;
    private CANSparkMax rightFront;
    private CANSparkMax rightRear;

    private DifferentialDrive driveTrain;

    public Drive(){
      
        leftFront = new CANSparkMax(1, MotorType.kBrushless);
            leftFront.setInverted(false);
            leftFront.setIdleMode(IdleMode.kBrake);

        leftRear = new CANSparkMax(2, MotorType.kBrushless);
            leftRear.setInverted(false);
            leftRear.setIdleMode(IdleMode.kBrake);
        
        rightFront = new CANSparkMax(3, MotorType.kBrushless);
            rightFront.setInverted(false);
            rightFront.setIdleMode(IdleMode.kBrake);

        rightRear = new CANSparkMax(4, MotorType.kBrushless);
            rightRear.setInverted(false);
            rightRear.setIdleMode(IdleMode.kBrake);
        
        driveTrain = new DifferentialDrive(leftFront, rightFront);
        
        leftRear.follow(leftFront);
        rightRear.follow(rightFront);
    }

    public void arcadeDrive(double speed, double rotation){
        System.out.println(speed + " " + rotation);
        driveTrain.arcadeDrive(speed, rotation);
    }

    public void setMaxOutput(double maxOutput){
        driveTrain.setMaxOutput(maxOutput);
    }
}