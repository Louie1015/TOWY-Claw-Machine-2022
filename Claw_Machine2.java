package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

//Verson 1 of Claw_Machine
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//How do we make a function that does everything that a claw machine does expect for moving around

class Claw_Mac2 {
    public DcMotor LMY = null;
    public DcMotor RMY = null;
    public DcMotor Sliderx = null;
    public DcMotor Vertical = null;
    public Servo LServo = null;
    public Servo RServo = null;
    public DigitalChannel front;
    public DigitalChannel back;

    public static double Claw_HomeL = 0.30;
    public static double Claw_HomeR = 0.30;

    HardwareMap map = null;

    public void init(HardwareMap maps){
        map = maps;
        LMY = maps.get(DcMotor.class, "lmy1");
        RMY = maps.dcMotor.get("rmy1");
        Sliderx = maps.dcMotor.get("slider");
        Vertical = maps.dcMotor.get("vertical");
        LServo = maps.servo.get("lservo");
        RServo = maps.servo.get("rservo");
        front = maps.digitalChannel.get("front");
        back = maps.digitalChannel.get("back");

        LMY.setDirection(DcMotorSimple.Direction.FORWARD);
        RMY.setDirection(DcMotorSimple.Direction.REVERSE);
        Sliderx.setDirection(DcMotorSimple.Direction.FORWARD);
        Vertical.setDirection(DcMotorSimple.Direction.REVERSE);


        LMY.setPower(0.0);
        RMY.setPower(0.0);
        Sliderx.setPower(0.0);
        Vertical.setPower(0.0);
        LServo.setPosition(Claw_HomeL);
        RServo.setPosition(Claw_HomeR);

        LMY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RMY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Sliderx.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Vertical.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        front.setMode(DigitalChannel.Mode.INPUT);
        back.setMode(DigitalChannel.Mode.INPUT);


    }
}

@TeleOp(name = "Claw_Machine2: Tele", group = "Claw_Machine")

public class Claw_Machine2 extends LinearOpMode{
    Claw_Mac2 robot = new Claw_Mac2();
    double y;
    double x;
    double ver_pos = 0.0;
    double claw_posl = robot.Claw_HomeL;
    double claw_posr = robot.Claw_HomeR;
    final double vmos = 1;
    final double claw_speed = 0.45;

    public void final_claw(){
        robot.Vertical.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.Vertical.setTargetPosition(3400);
        robot.Vertical.setPower(0.5);
        robot.Vertical.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while(robot.Vertical.isBusy()){
            telemetry.addData("Vertical is running",0);
            telemetry.update();
        }
        robot.Vertical.setPower(0);

        claw_posl = 0.0;
        claw_posr = 0.70;

        robot.LServo.setPosition(0.0);
        robot.RServo.setPosition(0.70);
        sleep(1500);

        robot.Vertical.setTargetPosition(0);
        robot.Vertical.setPower(-0.5);

        while(robot.Vertical.isBusy()){
            telemetry.addData("Vertical is running",0);
            telemetry.update();
        }

        robot.Sliderx.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.Sliderx.setTargetPosition(0);
        robot.Sliderx.setPower(-0.5);
        robot.Sliderx.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        if (robot.front.getState() == true){
            robot.LMY.setPower(1.0);
            robot.RMY.setPower(1.0);
        }
        else{
            robot.RMY.setPower(0.0);
            robot.LMY.setPower(0.0);
        }

        while(robot.Sliderx.isBusy() || robot.RMY.isBusy() || robot.LMY.isBusy()){
            telemetry.addData("Movement is happening",0);
            telemetry.update();
        }
        robot.Sliderx.setPower(0);
        robot.RMY.setPower(0);
        robot.LMY.setPower(0);

        claw_posl = 0.30;
        claw_posr = 0.30;

        robot.RServo.setPosition(0.30);
        robot.LServo.setPosition(0.30);
        sleep(1500);
    }


    @Override
    public void runOpMode(){
        robot.init(hardwareMap);
        telemetry.addData("Say", "Hello");
        telemetry.update();


        waitForStart();

        while(opModeIsActive()){
            y = -gamepad1.left_stick_y;
            x = gamepad1.left_stick_x;
            claw_posr = Range.clip(claw_posr,0.30,0.70);
            claw_posl = Range.clip(claw_posl,0,0.30);

            if (gamepad1.a)
                final_claw();
            else{
                robot.LMY.setPower(y/3);
                robot.RMY.setPower(y/3);
                robot.Sliderx.setPower(x/2);
            }

            robot.Vertical.setPower(ver_pos);
            robot.LServo.setPosition(claw_posl);
            robot.RServo.setPosition(claw_posr);

            telemetry.addData("y", "%.2f",y);
            telemetry.addData("x","%.2f",x);
            telemetry.addData("Claw_position_R","%.2f",claw_posr);
            telemetry.addData("Claw_position_L","%.2f",claw_posl);
            telemetry.update();

            sleep(50);
        }
    }
}
