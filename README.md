# health-data

Capacitor Plugin to Retrieve User's Device Step Count on Android

## Install

```bash
npm install health-data
npx cap sync
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`getSteps()`](#getsteps)
* [`checkPermission(...)`](#checkpermission)
* [`openAppSettings()`](#openappsettings)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### getSteps()

```typescript
getSteps() => Promise<{ name: string; count: number; }>
```

**Returns:** <code>Promise&lt;{ name: string; count: number; }&gt;</code>

--------------------


### checkPermission(...)

```typescript
checkPermission(options?: CheckPermissionOptions | undefined) => Promise<CheckPermissionResult>
```

| Param         | Type                                                                      |
| ------------- | ------------------------------------------------------------------------- |
| **`options`** | <code><a href="#checkpermissionoptions">CheckPermissionOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#checkpermissionresult">CheckPermissionResult</a>&gt;</code>

--------------------


### openAppSettings()

```typescript
openAppSettings() => Promise<void>
```

--------------------


### Interfaces


#### CheckPermissionResult

| Prop             | Type                 | Description                                                                                                                                                | Since |
| ---------------- | -------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------- | ----- |
| **`granted`**    | <code>boolean</code> | When set to `true`, the permission is granted.                                                                                                             |       |
| **`denied`**     | <code>boolean</code> | When set to `true`, the permission is denied and cannot be prompted for. The `openAppSettings` method should be used to let the user grant the permission. | 1.0.0 |
| **`asked`**      | <code>boolean</code> | When this is set to `true`, the user was just prompted the permission. Ergo: a dialog, asking the user to grant the permission, was shown.                 | 1.0.0 |
| **`neverAsked`** | <code>boolean</code> | When this is set to `true`, the user has never been prompted the permission.                                                                               | 1.0.0 |
| **`restricted`** | <code>boolean</code> | iOS only When this is set to `true`, the permission cannot be requested for some reason.                                                                   | 1.0.0 |
| **`unknown`**    | <code>boolean</code> | iOS only When this is set to `true`, the permission status cannot be retrieved.                                                                            | 1.0.0 |


#### CheckPermissionOptions

| Prop        | Type                 | Description                                                                                                                                                                                                                                                              | Default            | Since |
| ----------- | -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ------------------ | ----- |
| **`force`** | <code>boolean</code> | If this is set to `true`, the user will be prompted for the permission. The prompt will only show if the permission was not yet granted and also not denied completely yet. For more information see: https://github.com/capacitor-community/barcode-scanner#permissions | <code>false</code> | 1.0.0 |

</docgen-api>
