import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  getBatteryLevel(): Promise<number>;
  subscribeToBatteryChange(callback: (level: number) => void): void;
  unsubscribeFromBatteryChange(): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('NativeBattery');
