import React from 'react';
import {
  SafeAreaView,
  StyleSheet,
  Text,
  TextInput,
  Button,
  View,
} from 'react-native';

import NativeLocalStorage from './specs/NativeLocalStorage';
import NativeBattery from './specs/NativeBattery';

const EMPTY = '<empty>';

function App(): React.JSX.Element {
  const [value, setValue] = React.useState<string | null>(null);
  const [editingValue, setEditingValue] = React.useState<string | null>(null);
  const [batteryLevel, setBatteryLevel] = React.useState<number | null>(null);

  React.useEffect(() => {
    // Local storage: retrieve the stored value
    const storedValue = NativeLocalStorage?.getItem('myKey');
    setValue(storedValue ?? '');

    // Battery: get initial battery level
    NativeBattery.getBatteryLevel()
      .then(level => {
        setBatteryLevel(level);
      })
      .catch(error => {
        console.error('Failed to get battery level', error);
      });

    // Subscribe to battery level changes
    const batteryCallback = (level: number) => {
      setBatteryLevel(level);
    };
    NativeBattery.subscribeToBatteryChange(batteryCallback);

    // Cleanup: unsubscribe when the component unmounts
    return () => {
      NativeBattery.unsubscribeFromBatteryChange();
    };
  }, []);

  function saveValue() {
    NativeLocalStorage?.setItem(editingValue ?? EMPTY, 'myKey');
    setValue(editingValue);
  }

  function clearAll() {
    NativeLocalStorage?.clear();
    setValue('');
  }

  function deleteValue() {
    NativeLocalStorage?.removeItem('myKey');
    setValue('');
  }

  return (
    <SafeAreaView style={{flex: 1}}>
      {/* Persistent header displaying the battery percentage */}
      <View style={styles.header}>
        <Text style={styles.headerText}>
          Battery: {batteryLevel !== null ? `${batteryLevel}%` : 'Loading...'}
        </Text>
      </View>

      <Text style={styles.text}>
        Current stored value is: {value ?? 'No Value'}
      </Text>
      <TextInput
        placeholder="Enter the text you want to store"
        style={styles.textInput}
        onChangeText={setEditingValue}
      />
      <Button title="Save" onPress={saveValue} />
      <Button title="Delete" onPress={deleteValue} />
      <Button title="Clear" onPress={clearAll} />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  header: {
    padding: 10,
    backgroundColor: '#ddd',
    alignItems: 'center',
  },
  headerText: {
    fontSize: 18,
  },
  text: {
    margin: 10,
    fontSize: 20,
  },
  textInput: {
    margin: 10,
    height: 40,
    borderColor: 'black',
    borderWidth: 1,
    paddingLeft: 5,
    paddingRight: 5,
    borderRadius: 5,
  },
});

export default App;
