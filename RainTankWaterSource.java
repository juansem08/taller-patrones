package irrigation.source;

public class RainTankWaterSource implements WaterSource {

    private double currentVolume; // litros

    public RainTankWaterSource(double initialVolume) {
        this.currentVolume = initialVolume;
    }

    @Override
    public void supplyWater(double liters) {
        if (liters > currentVolume) {
            System.out.println("[TanqueLluvia] Agua insuficiente. Disponible: " + currentVolume + " litros.");
            return;
        }
        currentVolume -= liters;
        System.out.println("[TanqueLluvia] Suministrando " + liters + " litros. Restante en tanque: " + currentVolume);
    }

    @Override
    public String getName() {
        return "Tanque de lluvia";
    }
}

