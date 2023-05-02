import 'package:flutter/services.dart';

class ActivityChannel {
  static const MethodChannel _channel = MethodChannel('flutter_channel');

  static Future<void> startNativeActivity() async {
    try {
      await _channel.invokeMethod('startNativeActivity');
    } catch (e) {
      print('Error al invocar el método startNativeActivity: $e');
    }
  }
}