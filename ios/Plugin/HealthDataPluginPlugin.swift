import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(HealthDataPluginPlugin)
public class HealthDataPluginPlugin: CAPPlugin {
    private let implementation = HealthDataPlugin()

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.resolve([
            "value": implementation.echo(value)
        ])
    }

    override public func load(){
        
    }

    @objc func getSteps(_ call : CAPPluginCall){
        //let value = 
        call.resolve([
            "name": 'Step Counter Sensor ',
            "count": 10
        ])
    }

}
