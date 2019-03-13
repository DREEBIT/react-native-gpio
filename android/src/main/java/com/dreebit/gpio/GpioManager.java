package com.dreebit.gpio;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.dreebit.gpio.Throttle;

import static android.content.ContentValues.TAG;

public class GpioManager {

    private static volatile GpioManager sInstance = new GpioManager();
    private ReactApplicationContext reactContext;

    // GPIO Pin Name
    private ArrayList<Gpio> mGpios = new ArrayList<>();
    // Throttle map
    private HashMap<String, Throttle> mGpioThrottles = new HashMap<String, Throttle>();

    //private constructor.
    private GpioManager(){}

    public static GpioManager getInstance() {
        return sInstance;
    }


    public void subscribeGpio(String gpioName){

        boolean existing = false;
        for (Gpio gpio: mGpios) {
            String name = gpio.getName();
            if (name != null && gpioName.equals(name)){
                existing = true;
            }
        }

        if (existing){
            this.unsubscribe(gpioName);
        }
        // Attempt to access the GPIO
        try {
            PeripheralManager manager = PeripheralManager.getInstance();
            Gpio gpio = manager.openGpio(gpioName);
            this.configureInput(gpio);
            gpio.registerGpioCallback(mGpioCallback);
            mGpios.add(gpio);
        } catch (IOException e) {
            Log.w(TAG, "Unable to access GPIO", e);
        }
    }

    private void configureInput(Gpio gpio) throws IOException {
        // Initialize the pin as an input
        gpio.setDirection(Gpio.DIRECTION_IN);
        // Low voltage is considered active
        gpio.setActiveType(Gpio.ACTIVE_LOW);

        // Register for all state changes
        gpio.setEdgeTriggerType(Gpio.EDGE_BOTH);
        gpio.registerGpioCallback(mGpioCallback);
    }

    public void unsubscribe(String gpioName) {

        Gpio pickedGpio = null;
        // your code
        for (Gpio gpio: mGpios) {
            String name = gpio.getName();
            if (name != null && gpioName.equals(name)){
                pickedGpio = gpio;
                break;
            }
        }

        if (pickedGpio != null){
            try {
                pickedGpio.unregisterGpioCallback(mGpioCallback);
                pickedGpio.close();
                mGpios.remove(pickedGpio);
            } catch (IOException e) {
                Log.w(TAG, "Unable to close GPIO", e);
            }
        }
    }

    private GpioCallback mGpioCallback = new GpioCallback() {

        @Override
        public boolean onGpioEdge(Gpio gpio) {
            // Read the active low pin state
            try {
                WritableMap params = Arguments.createMap();
                String gpioName = gpio.getName();
                boolean gpioValue = gpio.getValue();
                String throttleKey = gpioName + (gpioValue ? "_1" : "_0");
                if (!mGpioThrottles.containsKey(throttleKey)) {
                    mGpioThrottles.put(throttleKey, new Throttle(500));
                }
                params.putBoolean(gpioName, gpioValue);
                GpioManager.this.sendEvent(mGpioThrottles.get(throttleKey),
                        GpioManager.this.reactContext, "RNGPIO", params);

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Continue listening for more interrupts
            return true;
        }

        @Override
        public void onGpioError(Gpio gpio, int error) {
            Log.w(TAG, gpio + ": Error event " + error);
        }
    };


    public ReactApplicationContext getReactContext() {
        return reactContext;
    }

    public void setReactContext(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    private void sendEvent(@NonNull Throttle throttle,
                           final ReactContext reactContext,
                           final String eventName,
                           final @Nullable WritableMap params) {
        throttle.attempt(new Runnable() {
            @Override
            public void run() {
                reactContext
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit(eventName, params);
            }
        });
    }
}
