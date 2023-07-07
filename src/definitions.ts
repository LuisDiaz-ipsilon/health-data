export interface HealthDataPluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  getSteps(): Promise<{ name: string, count: number }>;
}
