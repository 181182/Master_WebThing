package io.webthings.webthing.example;

import org.json.JSONArray;
import org.json.JSONObject;
import io.webthings.webthing.Action;
import io.webthings.webthing.Event;
import io.webthings.webthing.Property;
import io.webthings.webthing.Thing;
import io.webthings.webthing.Value;
import io.webthings.webthing.WebThingServer;
import io.webthings.webthing.errors.PropertyError;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PD1 {

    //PD1 Values
    private static Value<Double> CO2level;
    private static Value<Double> gravityLevel;
    private static Value<Double> pHLevel;
    private static Value<Double> temperatureLevel;
    private static Value<Double> salinityLevel;
    private static Value<Double> turbidityLevel;

    //PD2 Values
    private static Value<Double> acousticTomopraghyLevel;
    private static Value<Double> geoPositioningLevel;
    private static Value<Double> acousticEnvironmentLevel;
    private static Value<Double> oceanographicPointLevel;
    private static Value<Double> resonance;
    private static Value<Double> pipeline;

    public static Thing makeThing() {

        //Defining the first PD
        Thing thing = new Thing("PD1",
                "PD1",
                new JSONArray(Arrays.asList("MultiLevelSensor")),
                "Sensor for underwater technology");

        //Event
        JSONObject gasLeakageProperty = new JSONObject();
        gasLeakageProperty.put("description",
                "Event for GasLeakage");
        gasLeakageProperty.put("type", "boolean");
        thing.addAvailableEvent("GasLeakage", gasLeakageProperty);

        //Properties
        JSONObject CO2Properyy = new JSONObject();
        CO2Properyy.put("@type", "LevelProperty");
        CO2Properyy.put("title", "CO2");
        CO2Properyy.put("type", "number");
        CO2Properyy.put("description", "The current CO2 in %");
        CO2Properyy.put("minimum", 0);
        CO2Properyy.put("maximum", 100);
        CO2Properyy.put("unit", "percent");
        CO2Properyy.put("readOnly", true);
        CO2level = new Value<>(0.0);
        thing.addProperty(new Property(thing,
                "CO2",
                CO2level,
                CO2Properyy));

        JSONObject gravityProperty = new JSONObject();
        gravityProperty.put("@type", "LevelProperty");
        gravityProperty.put("title", "Gravity");
        gravityProperty.put("type", "number");
        gravityProperty.put("description", "The current Gravity in m/s^2");
        gravityProperty.put("readOnly", true);
        gravityLevel = new Value<>(0.0);
        thing.addProperty(new Property(thing,
                "Gravity",
                gravityLevel,
                gravityProperty));

        JSONObject pHProperty = new JSONObject();
        pHProperty.put("@type", "LevelProperty");
        pHProperty.put("title", "pH");
        pHProperty.put("type", "number");
        pHProperty.put("description", "The current pH level");
        pHProperty.put("readOnly", true);
        pHLevel = new Value<>(0.0);
        thing.addProperty(new Property(thing, "pH", pHLevel, pHProperty));

        JSONObject temperatureProperty = new JSONObject();
        temperatureProperty.put("@type", "TemperatureProperty");
        temperatureProperty.put("title", "Temperature");
        temperatureProperty.put("type", "number");
        temperatureProperty.put("description", "The current temperature in celsius");
        temperatureProperty.put("unit", "degree celsius");
        temperatureProperty.put("readOnly", true);
        temperatureLevel = new Value<>(0.0);
        thing.addProperty(new Property(thing, "Temperature", temperatureLevel, temperatureProperty));

        JSONObject salinityProperty = new JSONObject();
        salinityProperty.put("@type", "LevelProperty");
        salinityProperty.put("title", "Salinity");
        salinityProperty.put("type", "number");
        salinityProperty.put("description", "The current Salinity in parts per thousand");
        salinityProperty.put("readOnly", true);
        salinityLevel = new Value<>(0.0);
        thing.addProperty(new Property(thing, "Salinity", salinityLevel, salinityProperty));

        JSONObject turbidityProperty = new JSONObject();
        turbidityProperty.put("@type", "LevelProperty");
        turbidityProperty.put("title", "Turbidity");
        turbidityProperty.put("type", "number");
        turbidityProperty.put("description", "The current Turbidity in FTU");
        turbidityProperty.put("readOnly", true);
        turbidityLevel = new Value<>(0.0);


        // Start a thread that polls the sensor reading every 3 seconds
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000);
                    // Update the underlying value, which in turn notifies
                    // all listeners
                    double newLevel = readCO2();
                    double newGravity = readGravity();
                    double newpH = readpH();
                    double newTemperature = readTemperature();
                    double newSalinity = readSalinity();
                    double newTurbitity = readTurbidity();

                    //Event handler
                    if (CO2level.get() < 20) {
                        thing.addEvent(new GasLeakageEvent(thing, true));
                    }

                    //Terminal messages
                    System.out.printf("setting new CO2 level: %f\n",
                            newLevel);
                    System.out.printf("setting new Gravity level: %f\n",
                            newGravity);
                    System.out.printf("setting new pH level: %f\n",
                            newpH);
                    System.out.printf("setting new Temperature: %f\n",
                            newTemperature);
                    System.out.printf("setting new Salinity: %f\n",
                            newSalinity);
                    System.out.printf("setting new Turbitity: %f\n",
                            newTurbitity);

                    //Update Value
                    CO2level.notifyOfExternalUpdate(newLevel);
                    gravityLevel.notifyOfExternalUpdate(newGravity);
                    pHLevel.notifyOfExternalUpdate(newpH);
                    temperatureLevel.notifyOfExternalUpdate(newTemperature);
                    salinityLevel.notifyOfExternalUpdate(newSalinity);
                    turbidityLevel.notifyOfExternalUpdate(newTurbitity);

                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        }).start();

        return thing;
    }

    private static double readCO2() {
        return Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
    }

    private static double readGravity() {
        return Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
    }

    private static double readpH() {
        return Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
    }

    private static double readTemperature() {
        return Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
    }

    private static double readSalinity() {
        return Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
    }

    private static double readTurbidity() {
        return Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
    }

    public static Thing makeSecondThing() {

        Thing thing = new Thing("PD2",
                "PD2",
                new JSONArray(Arrays.asList("MultiLevelSensor")),
                "Sensor for underwater technology");

        JSONObject acousticTomographyProperty = new JSONObject();
        acousticTomographyProperty.put("@type", "FrequencyProperty");
        acousticTomographyProperty.put("title", "Acoustic Tomography");
        acousticTomographyProperty.put("type", "number");
        acousticTomographyProperty.put("description", "The current Acoustic Tomography in Hertz");
        acousticTomographyProperty.put("unit", "hertz");
        acousticTomographyProperty.put("readOnly", true);
        acousticTomopraghyLevel = new Value<>(0.0);
        thing.addProperty(new Property(thing,
                "Acoustic Tomography",
                acousticTomopraghyLevel,
                acousticTomographyProperty));

        JSONObject geopositioningProperty = new JSONObject();
        geopositioningProperty.put("@type", "LevelProperty");
        geopositioningProperty.put("title", "Geo-Positioning");
        geopositioningProperty.put("type", "number");
        geopositioningProperty.put("description", "The Geo-Positioning in coordinates");
        geopositioningProperty.put("readOnly", true);
        geoPositioningLevel = new Value<>(0.0);
        thing.addProperty(new Property(thing, "Geo-Positioning", geoPositioningLevel, geopositioningProperty));

        JSONObject acousticEnvironmentProperty = new JSONObject();
        acousticEnvironmentProperty.put("@type", "FrequencyProperty");
        acousticEnvironmentProperty.put("title", "Acoustic Environment");
        acousticEnvironmentProperty.put("type", "number");
        acousticEnvironmentProperty.put("description", "The Acoustic Environment in Hertz");
        acousticEnvironmentProperty.put("unit", "hertz");
        acousticEnvironmentProperty.put("readOnly", true);
        acousticEnvironmentLevel = new Value<>(0.0);
        thing.addProperty(new Property(thing, "Acoustic Environment", acousticEnvironmentLevel, acousticEnvironmentProperty));

        JSONObject oceanographicPointProperty = new JSONObject();
        oceanographicPointProperty.put("@type", "LevelProperty");
        oceanographicPointProperty.put("title", "Oceanographic Point");
        oceanographicPointProperty.put("type", "number");
        oceanographicPointProperty.put("description", "The Oceanographic Point in coordinates");
        oceanographicPointProperty.put("readOnly", true);
        oceanographicPointLevel = new Value<>(0.0);
        thing.addProperty(new Property(thing, "Oceanographic Point", oceanographicPointLevel, oceanographicPointProperty));


        // Start a thread that polls the sensor reading every 3 seconds
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000);
                    // Update the underlying value, which in turn notifies
                    // all listeners
                    double newAcousticTomography = readAcousticTomoprahy();
                    double newGeopositioning = readGeopositioning();
                    double newAcousticEnvironment = readAcousticEnvironment();
                    double newoceanographicPoint = readoceanographicPoint();

                    System.out.printf("setting new Acoustic Tomography frequency: %f\n",
                            newAcousticTomography);
                    System.out.printf("setting new Geo-Positioning: %f\n",
                            newGeopositioning);
                    System.out.printf("setting new Acoustic Environment frequency: %f\n",
                            newAcousticEnvironment);
                    System.out.printf("setting new Acoustic Environment frequency: %f\n",
                            newoceanographicPoint);

                    acousticTomopraghyLevel.notifyOfExternalUpdate(newAcousticTomography);
                    geoPositioningLevel.notifyOfExternalUpdate(newGeopositioning);
                    acousticEnvironmentLevel.notifyOfExternalUpdate(newAcousticEnvironment);
                    oceanographicPointLevel.notifyOfExternalUpdate(newoceanographicPoint);

                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        }).start();

        return thing;
    }

    private static double readAcousticTomoprahy() {
        return Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
    }
    private static double readGeopositioning() {
        return Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
    }
    private static double readAcousticEnvironment() {
        return Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
    }
    private static double readoceanographicPoint() {
        return Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
    }


    public static Thing makeThirdThing() {

        Thing thing = new Thing("PD3",
                "PD3",
                new JSONArray(Arrays.asList("MultiLevelSensor")),
                "Sensor for underwater technology");

        JSONObject acousticResonanceProperty = new JSONObject();
        acousticResonanceProperty.put("@type", "FrequencyProperty");
        acousticResonanceProperty.put("title", "Acoustic Resonance");
        acousticResonanceProperty.put("type", "number");
        acousticResonanceProperty.put("description", "The current Acoustic Resonance in Hertz");
        acousticResonanceProperty.put("unit", "hertz");
        acousticResonanceProperty.put("readOnly", true);
        resonance = new Value<>(0.0);
        thing.addProperty(new Property(thing,
                "Acoustic Resonance",
                resonance,
                acousticResonanceProperty));

        // Start a thread that polls the sensor reading every 3 seconds
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000);
                    // Update the underlying value, which in turn notifies
                    // all listeners
                    double newFrequency = readResonance();
                    System.out.printf("setting new Acoustic Resonance frequency: %f\n",
                            newFrequency);
                    resonance.notifyOfExternalUpdate(newFrequency);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        }).start();

        return thing;
    }

    private static double readResonance() {
        return Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
    }

    public static Thing makeFourthThing() {

        Thing thing = new Thing("PD4",
                "PD4",
                new JSONArray(Arrays.asList("MultiLevelSensor")),
                "PD4 Sensor for underwater technology");

        JSONObject pipelineVibrationsProperty = new JSONObject();
        pipelineVibrationsProperty.put("@type", "FrequencyProperty");
        pipelineVibrationsProperty.put("title", "Pipeline Vibrations (DAS)");
        pipelineVibrationsProperty.put("type", "number");
        pipelineVibrationsProperty.put("description", "The current Pipeline Vibrations in Hertz");
        pipelineVibrationsProperty.put("unit", "hertz");
        pipelineVibrationsProperty.put("readOnly", true);
        pipeline = new Value<>(0.0);
        thing.addProperty(new Property(thing,
                "Pipeline Vibrations (DAS)",
                pipeline,
                pipelineVibrationsProperty));

        // Start a thread that polls the sensor reading every 3 seconds
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000);
                    // Update the underlying value, which in turn notifies
                    // all listeners
                    double newFrequency = readPipelineVibrations();
                    System.out.printf("setting new Pipeline Vibrations (DAS) frequency: %f\n",
                            newFrequency);
                    pipeline.notifyOfExternalUpdate(newFrequency);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        }).start();

        return thing;
    }

    private static double readPipelineVibrations() {
        return Math.abs(70.0d * Math.random() * (-0.5 + Math.random()));
    }

    public static void main(String[] args) {
        Thing firstThing = makeThing();
        Thing secondThing = makeSecondThing();
        Thing thirdThing = makeThirdThing();
        Thing fourthThing = makeFourthThing();

        try {
            List<Thing> things = new ArrayList<>();
            things.add(firstThing);
            things.add(secondThing);
            things.add(thirdThing);
            things.add(fourthThing);
            // If adding more than one thing, use MultipleThings() with a name.
            // In the single thing case, the thing's name will be broadcast.
            WebThingServer server = new WebThingServer(new WebThingServer.MultipleThings(things, "PD1AndPD2Device"),
                    8888);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    server.stop();
                }
            });

            server.start(false);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    public static class GasLeakageEvent extends Event {
        public GasLeakageEvent(Thing thing, boolean data) {
            super(thing, "GasLeakage", data);
        }
    }


}
