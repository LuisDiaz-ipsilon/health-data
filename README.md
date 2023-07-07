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

</docgen-api>
