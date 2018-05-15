/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  FlatList,
  View,
  DeviceEventEmitter
} from 'react-native';
import RNGpio from "react-native-gpio";

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
  android: 'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

const pins = {
  BCM2: null,
  BCM3: null,
  BCM4: null,
  BCM5: null,
  BCM6: null,
  BCM7: null,
  BCM8: null,
  BCM9: null,
  BCM10: null,
  BCM11: null,
  BCM12: null,
  BCM13: null,
  BCM14: null,
  BCM15: null,
  BCM16: null,
  BCM17: null,
  BCM18: null,
  BCM19: null,
  BCM20: null,
  BCM21: null,
  BCM22: null,
  BCM23: null,
  BCM24: null,
  BCM25: null,
  BCM26: null,
}

type GpioEvent = {
  [string]: boolean
}

type Props = {};
export default class App extends Component<Props> {

  state = {
    pins
  }

  componentDidMount(){

    // register for gpio pins
    Object.keys(pins).forEach((pin)=>{
      RNGpio.subscribeGpio(pin);
    })

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

  componentWillUnmount(){

    Object.keys(pins).forEach((pin)=>{
      RNGpio.unsubscribeGpio(pin);
    })

  }

  render() {

    const pinsArray = Object.keys(this.state.pins) || [];
    console.log(this.state);
    return (
      <View style={styles.container}>
        <FlatList
          data={pinsArray.map( key => ({
            key,
            value: this.state.pins[key]
          }))}
          renderItem={({item}) => <Text>
            {item.key}: {item.value === null ? '-' : !!item.value ? 'On' : 'Off'}
          </Text>}
          />

      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  list: {

  },

});
