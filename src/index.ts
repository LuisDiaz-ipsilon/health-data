import { registerPlugin } from '@capacitor/core';

import type { HealthDataPluginPlugin } from './definitions';

const HealthDataPlugin = registerPlugin<HealthDataPluginPlugin>('HealthDataPlugin', {
  web: () => import('./web').then(m => new m.HealthDataPluginWeb()),
});

export * from './definitions';
export { HealthDataPlugin };
