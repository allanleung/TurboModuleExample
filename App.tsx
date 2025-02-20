import React, {useEffect, useState} from 'react';
import {SafeAreaView, StyleSheet, Text, View, ScrollView} from 'react-native';
import NativeBatteryInfo from './specs/NativeBatteryInfo';

const App = () => {
  const [batteryLevel, setBatteryLevel] = useState<number | null>(null);
  const [batteryHealth, setBatteryHealth] = useState<number | null>(null);
  const [batteryCapacity, setBatteryCapacity] = useState<number | null>(null);
  const [batteryVoltage, setBatteryVoltage] = useState<number | null>(null);

  const fetchBatteryData = async () => {
    try {
      const level = await NativeBatteryInfo.getBatteryLevel();
      const health = await NativeBatteryInfo.getBatteryHealth();
      const capacity = await NativeBatteryInfo.getBatteryCapacity();
      const voltage = await NativeBatteryInfo.getBatteryChargeVoltage();

      setBatteryLevel(level);
      setBatteryHealth(health);
      setBatteryCapacity(capacity);
      setBatteryVoltage(voltage);
    } catch (error) {
      console.error('Error fetching battery data:', error);
    }
  };

  useEffect(() => {
    fetchBatteryData();
    const intervalId = setInterval(fetchBatteryData, 5000);
    return () => clearInterval(intervalId);
  }, []);

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.headerText}>
          Battery Level: {batteryLevel !== null ? `${batteryLevel}%` : 'N/A'}
        </Text>
      </View>
      <ScrollView style={styles.content}>
        <Text style={styles.contentText}>
          Battery Health: {batteryHealth !== null ? batteryHealth : 'N/A'}
        </Text>
        <Text style={styles.contentText}>
          Battery Capacity:{' '}
          {batteryCapacity !== null ? `${batteryCapacity} mAh` : 'N/A'}
        </Text>
        <Text style={styles.contentText}>
          Battery Voltage:{' '}
          {batteryVoltage !== null ? `${batteryVoltage} mV` : 'N/A'}
        </Text>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  header: {
    padding: 16,
    backgroundColor: '#f1f1f1',
    alignItems: 'center',
    justifyContent: 'center',
  },
  headerText: {
    fontSize: 18,
    fontWeight: '600',
  },
  content: {
    flex: 1,
    padding: 16,
  },
  contentText: {
    fontSize: 16,
    marginBottom: 8,
  },
});

export default App;
