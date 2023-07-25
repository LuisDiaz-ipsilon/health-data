import Foundation
import Capacitor
import AVFoundation

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(HealthDataPluginPlugin)
public class HealthDataPluginPlugin: CAPPlugin, AVCaptureMetadataOutputObjectsDelegate {
    private let implementation = HealthDataPlugin()

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? "no message"
        call.resolve([
            "value": implementation.echo(value)
        ])
    }

    override public func load(){

    }

    @objc func getSteps(_ call : CAPPluginCall){
        //let value = 
        call.resolve([
            "name": "Step Counter Sensor",
            "count": 10
        ])
    }

    @objc func openAppSettings(_ call : CAPPluginCall){

    }

    var stepCountSensor: AVCaptureDevice?

    @objc override func checkPermission(_ call: CAPPluginCall) {
        let locationState: String

        switch CLLocationManager.authorizationStatus() {
        case .notDetermined:
            locationState = "prompt"
        case .restricted, .denied:
            locationState = "denied"
        case .authorizedAlways, .authorizedWhenInUse:
            locationState = "granted"
        @unknown default:
            locationState = "prompt"
        }

        call.resolve(["location": locationState])
    }

    @objc override func requestPermissions(_ call: CAPPluginCall) {
        AVCaptureDevice.requestAccess(for: .stepCountSensor) { [weak self] _ in
            self?.checkPermissions(call)
        }
    }

}
