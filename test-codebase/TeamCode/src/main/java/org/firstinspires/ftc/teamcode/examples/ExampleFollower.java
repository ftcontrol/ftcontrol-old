package org.firstinspires.ftc.teamcode.examples;

import com.bylazar.ftcontrol.panels.configurables.annotations.Configurable;

@Configurable
public class ExampleFollower {
    public static boolean useTranslational = true;
    public static boolean useCentripetal = true;
    public static boolean useHeading = true;
    public static boolean useDrive = true;

    public static ErrorProducer producer = new ErrorProducer();

    void init(){
        producer.init();
    }
}
