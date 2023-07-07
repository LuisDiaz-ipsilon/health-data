export interface HealthDataPluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  getSteps(): Promise<{ name: string, count: number }>;
  checkPermission(options?: CheckPermissionOptions): Promise<CheckPermissionResult>;
  openAppSettings(): Promise<void>;
}

export interface CheckPermissionOptions {
  /**
   * If this is set to `true`, the user will be prompted for the permission.
   * The prompt will only show if the permission was not yet granted and also not denied completely yet.
   *
   * @default false
   * @since 0.0.1
   */
  force?: boolean;
}

export interface CheckPermissionResult {
  /**
   * When set to `true`, the permission is granted.
   */
  granted?: boolean;

  /**
   * When set to `true`, the permission is denied and cannot be prompted for.
   * The `openAppSettings` method should be used to let the user grant the permission.
   *
   * @since 0.0.1
   */
  denied?: boolean;

  /**
   * When this is set to `true`, the user was just prompted the permission.
   * Ergo: a dialog, asking the user to grant the permission, was shown.
   *
   * @since 0.0.1
   */
  asked?: boolean;

  /**
   * When this is set to `true`, the user has never been prompted the permission.
   *
   * @since 0.0.1
   */
  neverAsked?: boolean;

  /**
   * iOS only
   * When this is set to `true`, the permission cannot be requested for some reason.
   *
   * @since 0.0.1
   */
  restricted?: boolean;

  /**
   * iOS only
   * When this is set to `true`, the permission status cannot be retrieved.
   *
   * @since 0.0.1
   */
  unknown?: boolean;
}
