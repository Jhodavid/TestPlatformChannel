import 'package:flutter/services.dart';

class ArcGISActivityChannel {
  static const MethodChannel _channel = MethodChannel('flutter_channel');

  static Future<void> startArcGisActivity() async {
    try {
      await _channel.invokeMethod('arcGISActivity');
    } catch (e) {
      print('Error al invocar el método startNativeActivity: $e');
    }
  }
}