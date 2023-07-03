export interface HealthDataPluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
