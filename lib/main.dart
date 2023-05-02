import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:test_platform_channel/activity_channel.dart';
import 'package:test_platform_channel/arc_gis_activity_channel.dart';
import 'package:test_platform_channel/biometric_auth_channel.dart';
import 'package:test_platform_channel/dialog_channerl.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {

  const MyHomePage({super.key});

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

  final MethodChannel _channel = const MethodChannel('flutter_channel');
  String authState = "None";
  Color authColor = Colors.grey;

  @override
  void initState() {
    super.initState();

    _channel.setMethodCallHandler((call) async {
      if (call.method == "biometricAuthStream") {
        final String data = call.arguments as String;
        setState(() {
          authState = data;
          switch(data) {
            case "CANCEL_AUTHENTICATED": authColor = Colors.yellow; break;
            case "NOT_AUTHENTICATED": authColor = Colors.red; break;
            case "AUTHENTICATED": authColor = Colors.green; break;
          }
        });
      }
    });
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Probando Platform Channel'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                TextButton(
                  child: const Icon( Icons.android_rounded ),
                  onPressed: () {
                    DialogChannel.showDialog('Hola', 'Mensaje en nativo');
                  },
                ),
                TextButton(
                  child: const Icon( Icons.phonelink_ring_rounded ),
                  onPressed: () {
                    ActivityChannel.startNativeActivity();
                  },
                ),
                TextButton(
                  child: const Icon( Icons.fingerprint_rounded ),
                  onPressed: () async {
                    final result = await BiometricAuthChannel.biometricAuth();
                    // ignore: use_build_context_synchronously
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(
                        content: Text(result ?? "Error")
                      )
                    );
                  },
                ),
                TextButton(
                  child: const Icon( Icons.map_rounded ),
                  onPressed: () {
                    ArcGISActivityChannel.startArcGisActivity();
                  },
                ),
              ],
            ),
            Container(
              height: 100,
              width: 220,
              color: authColor,
              child: Center(
                child: Text(
                  authState,
                  style: const TextStyle(
                    fontSize: 16
                  ),
                )
              ),
            )
          ],
        ),
      ),
    );
  }
}
