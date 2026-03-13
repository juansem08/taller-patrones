package irrigation.adapter;

public class LegacyPumpSystem {

    // Método legado con firma diferente y unidades en m^3
    public void startPump(double cubicMeters, int seconds) {
        System.out.println("[BombaAntigua] Bombeando " + cubicMeters + " m^3 de agua durante " + seconds + " segundos.");
    }
}

