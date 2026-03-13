package irrigation.bridge;

import irrigation.source.WaterSource;

public abstract class IrrigationMode {

    protected WaterSource waterSource;

    public IrrigationMode(WaterSource waterSource) {
        this.waterSource = waterSource;
    }

    public abstract void irrigate(double areaSquareMeters);
}

