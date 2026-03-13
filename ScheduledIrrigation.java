package irrigation.bridge;

import irrigation.source.WaterSource;

public class ScheduledIrrigation extends IrrigationMode {

    private final double litersPerSquareMeter;

    public ScheduledIrrigation(WaterSource waterSource, double litersPerSquareMeter) {
        super(waterSource);
        this.litersPerSquareMeter = litersPerSquareMeter;
    }

    @Override
    public void irrigate(double areaSquareMeters) {
        if (areaSquareMeters <= 0) {
            System.out.println("Error: el área debe ser mayor que 0.");
            return;
        }
        double liters = areaSquareMeters * litersPerSquareMeter;
        System.out.println("\n[RiegoProgramado] Usando " + waterSource.getName());
        System.out.println("Área: " + areaSquareMeters + " m^2");
        System.out.println("Litros por m^2: " + litersPerSquareMeter);
        System.out.println("Total de litros solicitados: " + liters);
        waterSource.supplyWater(liters);
    }
}

