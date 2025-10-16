# Aplicación Android – Intents & Navegación entre Activities

[![GitHub repo](https://img.shields.io/badge/github-android--intents-blue.svg)](https://github.com/PhilippeJean833/Aplicacion-Android.git)

## 🟢 Resumen del proyecto

Esta aplicación Android sirve como demostración de uso de **intents implícitos y explícitos**, además de mejoras en la interacción y navegación entre *activities*. El proyecto ya se encuentra alojado en GitHub para control de versiones y colaboración.  
- Versión mínima de Android: **API XX / Android X.X** *(reemplaza con tu minSdkVersion / targetSdkVersion reales)*  
- Versión de Android Gradle Plugin (AGP): **X.X.X** *(reemplaza con la versión que usas, por ejemplo “7.4.2”)*  

## ✅ Intents implementados

### Intents implícitos (5 ejemplos)

Los siguientes intents implícitos permiten que la app delegue la acción al sistema o a otras apps:

| Funcionalidad | Acción | Pasos de prueba |
|---|---|---|
| Ver ubicación en Google Maps | `ACTION_VIEW` + URI `geo:` | 1. Tocar botón «Ubicación» <br>2. Se lanza Maps con la ubicación dada |
| Abrir galería de imágenes | `ACTION_PICK` con tipo `image/*` | 1. Pulsar “Seleccionar imagen” <br>2. Se abre la galería del dispositivo |
| Abrir configuración del teléfono | `Settings.ACTION_SETTINGS` | 1. Pulsar “Abrir ajustes” <br>2. Se muestran los ajustes del sistema |
| Ver contactos | `Intent.ACTION_PICK` con `ContactsContract.Contacts.CONTENT_URI` | 1. Pulsar “Seleccionar contacto” <br>2. Se abre la lista de contactos del teléfono |
| Iniciar llamada con número predeterminado | `Intent.ACTION_DIAL` con URI `tel:` | 1. Pulsar “Llamar” <br>2. Se abre la app de llamadas con el número ya cargado |

### Intents explícitos (3 ejemplos)

Con intents explícitos diriges la acción a una *activity* concreta dentro de tu app:

| Acción | Destino / propósito | Pasos de prueba |
|---|---|---|
| Abrir configuración interna de la app | `SettingsActivity` (o similar) | 1. En el menú pulsar “Configuración interna” <br>2. Se abre la pantalla interna para ajustes |
| Cambiar modo oscuro / claro | `Intent` explícito + extras | 1. Desde la configuración interna elegir modo <br>2. Se aplica el tema correspondiente en toda la app |
| Aplicar transiciones personalizadas entre activities | Intent explícito con animaciones (overridePendingTransition) | 1. Navegar entre pantallas <br>2. Ver efectos de transición (fade, slide, etc.) |

## 🖼 Capturas de pantalla / GIF



*(Sustituye las imágenes del ejemplo con capturas reales de tu app)*

## 📦 APK Debug / Instrucciones de compilación

Busca el archivo en:  
