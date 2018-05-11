
# react-native-gpio

## Getting started

`$ npm install react-native-gpio --save`

### Mostly automatic installation

`$ react-native link react-native-gpio`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.dreebit.RNGpioPackage;` to the imports at the top of the file
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


## Usage
```javascript
import RNGpio from 'react-native-gpio';

// TODO: What to do with the module?
RNGpio;
```
  