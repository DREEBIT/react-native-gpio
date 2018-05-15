
# react-native-gpio

## Getting started

`$ npm install react-native-gpio --save`

### Mostly automatic installation

`$ react-native link react-native-gpio`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.dreebit.gpio.RNGpioPackage;` to the imports at the top of the file
  - Add `new RNGpioPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-gpio'
  	project(':react-native-gpio').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-gpio/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-gpio')
  	```

## Android Things

Please use this link to view Android Things (e.g. Raspberry Pi) I/O documentation: [raspberrypi-io](https://developer.android.com/things/hardware/raspberrypi-io).
Use the name for the GPIO pin for subscription. 

## Usage
```javascript
import RNGpio from 'react-native-gpio';

...

componentDidMount(){

    // register for gpio pins
    RNGpio.subscribeGpio("BCM14");

    // register for events
    DeviceEventEmitter.addListener('RNGPIO', (event: GpioEvent) => {
      
      /**
       * Sample event: {
       *  "BCM2": true
       * }
       */
      this.setState({
        pins: {
          ...this.state.pins,
          ...event
        }
      })
    });
}

...

```
