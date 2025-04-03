package lol.lazar.lazarkit.flows;

import lol.lazar.lazarkit.flows.conditional.Must;
import lol.lazar.lazarkit.flows.groups.Parallel;
import lol.lazar.lazarkit.flows.groups.Sequential;

public class TestJava {

    public int batteryLevel = 50;

    private final Sequential autonomous = new Sequential(
            new Instant(() -> {
                System.out.println("DASH: FLOWS: Start moving");
                return null;
            }),
            new Wait(1000),
            new Must(() -> batteryLevel > 20,
                    new Instant(() -> {
                        System.out.println("DASH: FLOWS: Battery is good, proceeding");
                        return null;
                    })
            ),
            new Must(() -> true,
                    new Instant(() -> {
                        System.out.println("DASH: FLOWS: Sensor detected obstacle!");
                        return null;
                    })
            ),
            new Parallel(
                    new Instant(() -> {
                        System.out.println("DASH: FLOWS: Parallel flow start");
                        return null;
                    }),
                    new Instant(() -> {
                        System.out.println("DASH: FLOWS: Parallel flow start");
                        return null;
                    })
            ),
            new Instant(() -> {
                System.out.println("DASH: FLOWS: Done");
                return null;
            })
    );

    public void printAutonomousFlowDescription() {
        System.out.println("Autonomous Flow Description:");
        autonomous.describe();
    }
}