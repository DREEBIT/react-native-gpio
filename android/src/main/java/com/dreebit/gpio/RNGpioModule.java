
package com.dreebit.gpio;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class RNGpioModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNGpioModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;

    GpioManager.getInstance().setReactContext(reactContext);
  }

  @Override
  public String getName() {
    return "RNGpio";
  }

  @ReactMethod
  public void subscribeGpio(String gpioName){

    if (gpioName == null || gpioName.isEmpty()){
      return;
    }

    GpioManager.getInstance().subscribeGpio(gpioName);


  }


  @ReactMethod
  public void unsubscribeGpio(String gpioName) {

    if (gpioName == null || gpioName.isEmpty()){
      return;
    }

    GpioManager.getInstance().unsubscribe(gpioName);

  }



}
