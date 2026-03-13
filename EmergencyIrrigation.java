package irrigation.bridge;

import irrigation.source.WaterSource;

public class EmergencyIrrigation extends IrrigationMode {

    public EmergencyIrrigation(WaterSource waterSource) {
        super(waterSource);
    }

    @Override
    public void irrigate(double areaSquareMeters) {
        if (areaSquareMeters <= 0) {
            System.out.println("Error: el área debe ser mayor que 0.");
            return;
        }
        // En emergencia se envía más agua: 20 litros por m^2
        double liters = areaSquareMeters * 20;
        System.out.println("\n[RiegoEmergencia] Usando " + waterSource.getName());
        System.out.println("MODO EMERGENCIA para área: " + areaSquareMeters + " m^2");
        System.out.println("Total de litros solicitados: " + liters);
        waterSource.supplyWater(liters);
    }
}

