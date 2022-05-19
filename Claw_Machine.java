package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

//Verson 1 of Claw_Machine
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// We'll probably decide to do encoders
// We also might us a raspberry pie I think
// Also try to do another version of this code and we'll test it to see which one we should use

class Claw_Mac {
    public DcMotor LMY = null;
    public DcMotor RMY = null;
    public DcMotor Sliderx = null;
    public DcMotor Vertical = null;
    public Servo LServo = null;
    public Servo RServo = null;

    public static double Claw_Home = 0.45;
    public static double Claw_Max = 0.45;
    public static double Claw_Min = 0.0;

    HardwareMap map = null;

    public void init(HardwareMap maps){
        map = maps;
        LMY = maps.get(DcMotor.class, "lmy1");
        RMY = maps.dcMotor.get("rmy1");
        Sliderx = maps.dcMotor.get("slider");
        Vertical = maps.dcMotor.get("vertical");
        LServo = maps.servo.get("lservo");
        RServo = maps.servo.get("rservo");

        LMY.setDirection(DcMotorSimple.Direction.FORWARD);
        RMY.setDirection(DcMotorSimple.Direction.FORWARD);
        Sliderx.setDirection(DcMotorSimple.Direction.FORWARD);
        Vertical.setDirection(DcMotorSimple.Direction.FORWARD);

        LMY.setPower(0.0);
        RMY.setPower(0.0);
        Sliderx.setPower(0.0);
        Vertical.setPower(0.0);
        LServo.setPosition(Claw_Home);
        RServo.setPosition(Claw_Home);

        LMY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RMY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Sliderx.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Vertical.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }
}

@TeleOp(name = "Claw_Machine: Tele", group = "Claw_Machine")

public class Claw_Machine extends LinearOpMode{
    Claw_Mac robot = new Claw_Mac();
    double y;
    double x;
    double ver_pos = 0.0;
    double claw_pos = robot.Claw_Home;
    final double vmos = 1;
    final double claw_speed = 0.15;


    @Override
    public void runOpMode(){
        robot.init(hardwareMap);
        telemetry.addData("Say", "Hello");
        telemetry.update();


        waitForStart();

        while(opModeIsActive()){
            y = -gamepad1.left_stick_y;
            x = gamepad1.left_stick_x;
            claw_pos = Range.clip(claw_pos,0.0,0.45);

            if (gamepad1.a && claw_pos == 0.0){
                claw_pos += 0.45;
            }
            else if (gamepad1.a && claw_pos != 0.45)
                claw_pos -= claw_speed;
            else if (ver_pos != 0)
                ver_pos = 0;
            else if (gamepad1.right_bumper)
                ver_pos += vmos;
            else if (gamepad1.left_bumper)
                ver_pos += -vmos;
            else{
                robot.LMY.setPower(y/3);
                robot.RMY.setPower(-y/3);
                robot.Sliderx.setPower(x/3);
            }

            robot.Vertical.setPower(ver_pos);
            robot.LServo.setPosition(claw_pos);
            robot.RServo.setPosition(claw_pos);

            telemetry.addData("y", "%.2f",y);
            telemetry.addData("x","%.2f",x);
            telemetry.addData("Vertical_position","%.2f",ver_pos);
            telemetry.addData("Claw_position","%.2f",claw_pos);
            telemetry.update();

            sleep(50);
        }
    }
}
