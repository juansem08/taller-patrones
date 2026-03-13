package irrigation.adapter;

import irrigation.source.WaterSource;

public class LegacyPumpAdapter implements WaterSource {

    private final LegacyPumpSystem legacyPump;

    public LegacyPumpAdapter(LegacyPumpSystem legacyPump) {
        this.legacyPump = legacyPump;
    }

    @Override
    public void supplyWater(double liters) {
        // 1 m^3 = 1000 litros
        double cubicMeters = liters / 1000.0;
        int seconds = (int) Math.max(10, liters / 10); // regla simple: más litros -> más segundos
        legacyPump.startPump(cubicMeters, seconds);
    }

    @Override
    public String getName() {
        return "Bomba antigua (adaptada)";
    }
}

