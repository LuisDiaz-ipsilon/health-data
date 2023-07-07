import { WebPlugin } from '@capacitor/core';

import type { HealthDataPluginPlugin } from './definitions';

export class HealthDataPluginWeb extends WebPlugin implements HealthDataPluginPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async getSteps(): Promise<{ name: string, count: number }> {
    return {
    name : "Name Sensor",
    count : 300
    }
  }

}

