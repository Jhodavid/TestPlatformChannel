import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class DialogChannel {
  static const MethodChannel _channel = MethodChannel('flutter_channel');

  static Future<void> showDialog(String title, String message) async {
    try {
      await _channel.invokeMethod('showDialog', {
        'title': title,
        'message': message,
      });
    } catch (e) {
      if (kDebugMode) {
        print('Error al invocar el método showDialog: $e');
      }
    }
  }
}