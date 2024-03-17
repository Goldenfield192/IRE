package cam72cam.immersiverailroading.registry;

import cam72cam.immersiverailroading.Config;
import cam72cam.immersiverailroading.entity.LocomotiveElectric;
import cam72cam.immersiverailroading.gui.overlay.GuiBuilder;
import cam72cam.immersiverailroading.library.Gauge;
import cam72cam.immersiverailroading.library.ValveGearConfig;
import cam72cam.immersiverailroading.model.StockModel;
import cam72cam.immersiverailroading.util.DataBlock;
import cam72cam.immersiverailroading.util.FluidQuantity;
import cam72cam.mod.resource.Identifier;
import cam72cam.immersiverailroading.model.ElectricLocomotiveModel;

import java.io.IOException;
import java.util.Map;

public class LocomotiveElectricDefinition extends LocomotiveDefinition {
    public EntityRollingStockDefinition.SoundDefinition idle;
    public EntityRollingStockDefinition.SoundDefinition running;
    public EntityRollingStockDefinition.SoundDefinition horn;
    private double fuelCapacity_l;
    private int fuelEfficiency;
    private boolean hornSus;
    private int notches;
    private float enginePitchRange;
    public boolean hasDynamicTractionControl;

    public LocomotiveElectricDefinition(String defID, DataBlock data) throws Exception {
        super(LocomotiveElectric.class, defID, data);
    }

    protected Identifier defaultDataLocation() {
        return new Identifier("immersiverailroading", "rolling_stock/default/diesel.caml");
    }

    public void loadData(DataBlock data) throws Exception {
        super.loadData(data);
        DataBlock properties = data.getBlock("properties");
        if (!this.isCabCar()) {
            this.fuelCapacity_l = (double)properties.getValue("fuel_capacity_l").asInteger() * this.internal_inv_scale * (double) Config.ConfigBalance.DieselLocomotiveTankMultiplier;
            this.fuelEfficiency = properties.getValue("fuel_efficiency_%").asInteger();
            this.hasDynamicTractionControl = properties.getValue("dynamic_traction_control").asBoolean();
        } else {
            this.fuelCapacity_l = 0.0;
        }

        this.notches = properties.getValue("throttle_notches").asInteger();
        this.hornSus = properties.getValue("horn_sustained").asBoolean();
        DataBlock sounds = data.getBlock("sounds");
        this.idle = SoundDefinition.getOrDefault(sounds, "idle");
        this.running = SoundDefinition.getOrDefault(sounds, "running");
        this.enginePitchRange = sounds.getValue("engine_pitch_range").asFloat();
        this.horn = SoundDefinition.getOrDefault(sounds, "horn");
        this.bell = SoundDefinition.getOrDefault(sounds, "bell");
    }

    protected StockModel<?, ?> createModel() throws Exception {
        return new ElectricLocomotiveModel(this);
    }

    protected GuiBuilder getDefaultOverlay(DataBlock data) throws IOException {
        return this.readCabCarFlag(data) ? GuiBuilder.parse(new Identifier("immersiverailroading", "gui/default/cab_car.caml")) : GuiBuilder.parse(new Identifier("immersiverailroading", "gui/default/diesel.caml"));
    }

    public StockModel<?, ?> getModel() {
        return (ElectricLocomotiveModel)super.getModel();
    }

    public boolean getHornSus() {
        return this.hornSus;
    }

    public FluidQuantity getFuelCapacity(Gauge gauge) {
        FluidQuantity cap = FluidQuantity.FromLiters((int)Math.ceil(this.fuelCapacity_l * gauge.scale()) * Config.ConfigBalance.DieselLocomotiveTankMultiplier).min(FluidQuantity.FromBuckets(1));
        return Config.ConfigBalance.RoundStockTankToNearestBucket ? cap.roundBuckets() : cap;
    }

    public int getFuelEfficiency() {
        return this.fuelEfficiency;
    }

    public ValveGearConfig getValveGear() {
        return super.getValveGear() == null ? new ValveGearConfig(ValveGearConfig.ValveGearType.CONNECTING, (Map)null) : super.getValveGear();
    }

    public int getThrottleNotches() {
        return this.notches;
    }

    public float getEnginePitchRange() {
        return this.enginePitchRange;
    }
}
