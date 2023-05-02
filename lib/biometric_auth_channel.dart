import 'dart:async';

import 'package:flutter/services.dart';

class BiometricAuthChannel {

  static const MethodChannel _channel = MethodChannel('flutter_channel');

  static Future<String?> biometricAuth() async {
    try {
      return await _channel.invokeMethod('biometricAuth');
    } catch (e) {
      print('Error al invocar el método biometricAuth: $e');
    }
    return null;
  }
}