import React, {useEffect, useState} from 'react';
import {SafeAreaView, StyleSheet, Text, View} from 'react-native';
import NativeBatteryInfo from './specs/NativeBatteryInfo';

const App = () => {
  const [batteryLevel, setBatteryLevel] = useState<number | null>(null);

  // Fetch battery level from the native module.
  const fetchBatteryLevel = async () => {
    try {
      const level = await NativeBatteryInfo.getBatteryLevel();
      setBatteryLevel(level);
    } catch (error) {
      console.error('Error fetching battery level:', error);
    }
  };

  // Poll battery level every 5 seconds to simulate real-time updates.
  useEffect(() => {
    fetchBatteryLevel();
    const intervalId = setInterval(fetchBatteryLevel, 5000);
    return () => clearInterval(intervalId);
  }, []);

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.headerText}>
          Battery: {batteryLevel !== null ? `${batteryLevel}%` : 'N/A'}
        </Text>
      </View>
      <View style={styles.content}>
        <Text style={styles.contentText}>Main app content goes here.</Text>
      </View>
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
    alignItems: 'center',
    justifyContent: 'center',
  },
  contentText: {
    fontSize: 16,
  },
});

export default App;
