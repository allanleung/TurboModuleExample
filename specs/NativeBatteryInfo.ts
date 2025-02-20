import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  getBatteryLevel(): Promise<number>;
  getBatteryHealth(): Promise<number>;
  getBatteryCapacity(): Promise<number>;
  getBatteryChargeVoltage(): Promise<number>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('NativeBatteryInfo');
