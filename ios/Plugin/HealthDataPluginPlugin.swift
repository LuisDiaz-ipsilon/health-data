import Foundation
import Capacitor
import AVFoundation

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(HealthDataPluginPlugin)
public class HealthDataPluginPlugin: CAPPlugin {
    private let implementation = HealthDataPlugin()

    static let stepCount: HKQuantityTypeIdentifier

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? "no message"
        call.resolve([
            "value": implementation.echo(value)
        ])
    }

    override public func load(){

    }

    @objc func getSteps(_ call : CAPPluginCall){
        if !isHealthKitPermissionGranted(){
            requestPermissions(call, CallingMethod)
        } else {
            stepCount = HKQuantityType.quantityType(forIdentifier: HKQuantityTypeIdentifier.stepCount)!
        }

        call.resolve([
            "name": "Step Counter Sensor",
            "count": stepCount.getJSObjtect()
        ])
        
    }

    @objc func openAppSettings(_ call : CAPPluginCall){

    }

    var stepCountSensor: AVCaptureDevice?

    @objc override func checkPermission(_ call: CAPPluginCall) {
        let permissionState: String

        switch CLLocationManager.authorizationStatus() {
        case .notDetermined:
            permissionState = "prompt"
        case .restricted, .denied:
            permissionState = "denied"
        case .authorizedAlways, .authorizedWhenInUse:
            permissionState = "granted"
        @unknown default:
            permissionState = "prompt"
        }

        call.resolve(["health": permissionState])
    }

    @objc override func requestPermissions(_ call: CAPPluginCall) {
        AVCaptureDevice.requestAccess(for: .Healthkit) { [weak self] _ in
            self?.checkPermissions(call)
        }
    }

    private func isHealthKitPermissionGranted() -> Bool{
        switch CLLocationManager.authorizationStatus(for: .Healthkit) {
        case .notDetermined, .restricted, .denied:
            return false
        case .authorized:
            return true
        @unknown default:
            return false
        }
    }

}
