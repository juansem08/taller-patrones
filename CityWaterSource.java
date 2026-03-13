package irrigation.source;

public class CityWaterSource implements WaterSource {

    @Override
    public void supplyWater(double liters) {
        System.out.println("[AguaCiudad] Suministrando " + liters + " litros desde la red de la ciudad.");
    }

    @Override
    public String getName() {
        return "Agua de la ciudad";
    }
}

