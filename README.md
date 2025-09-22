# 🚗 Sistema de Concesionaria de Vehículos

## 📋 Descripción

Sistema de gestión para una concesionaria que permite administrar vehículos (Autos, Motos, Camionetas) con funcionalidades de CRUD, filtrado, cambio de estados y persistencia en múltiples formatos.

## 🏗️ Arquitectura del Sistema

### 📊 Diagramas UML

El sistema está documentado a través de diagramas UML modulares para facilitar su comprensión:

#### 🎯 [Vista General Completa](uml/uml_general.puml)

![Diagrama General](uml/uml_general.png)
_Diagrama completo del sistema con todas las relaciones_

#### 📦 Diagramas por Módulos:

| Módulo              | Diagrama                                                                               | Descripción                                     |
| ------------------- | -------------------------------------------------------------------------------------- | ----------------------------------------------- |
| **🏛️ Modelos**      | [![Modelos](uml/Modelos_Enums.png)](uml/Modelos_Enums.puml)                            | Jerarquía de vehículos, enums y relaciones base |
| **⚙️ Interfaces**   | [![Interfaces](uml/Interfaces_Gestor.png)](uml/Interfaces_Gestor.puml)                 | Contratos del sistema y gestor principal        |
| **🎮 Controllers**  | [![Controllers](uml/Controllers_JavaFx.png)](uml/Controllers_JavaFx.puml)              | Controladores JavaFX y manejo de UI             |
| **✅ Validaciones** | [![Validaciones](uml/Validaciones_Excepciones.png)](uml/Validaciones_Excepciones.puml) | Sistema de validación y excepciones             |
| **💾 Persistencia** | [![Utilities](uml/Utilities.png)](uml/Utilities.puml)                                  | Utilidades para JSON, CSV y TXT                 |

### 🎯 **Navegación Rápida por Diagramas:**

```
📁 uml/
├── 🌍 uml_general.puml          # Diagrama completo del sistema
├── 🏛️ 01_modelos_enums.puml     # Modelos de dominio y enumeraciones
├── ⚙️ 02_interfaces_gestor.puml # Interfaces y administrador principal
├── 🎮 03_controllers.puml       # Controladores JavaFX de la UI
├── ✅ 04_validaciones_excepciones.puml # Sistema de validación
└── 💾 05_utilities.puml         # Utilidades de persistencia
```

### 🔗 **Relaciones entre Módulos:**

- **Modelos** ← Implementan → **Interfaces**
- **Controllers** ← Utilizan → **Gestor** ← Contiene → **Modelos**
- **Controllers** ← Validan con → **Validaciones**
- **Gestor** ← Persiste con → **Utilities**

## 🚀 Características Principales

### ✨ **Funcionalidades:**

- ✅ **CRUD Completo** - Crear, leer, actualizar, eliminar vehículos
- 🔍 **Filtrado Avanzado** - Por tipo y estado de vehículo
- 🔄 **Gestión de Estados** - Disponible, Alquilado, En Mantenimiento
- 💾 **Persistencia Múltiple** - JSON, CSV, TXT
- 🎫 **Sistema de Tickets** - Generación de comprobantes
- ✅ **Validación Robusta** - Validación de datos en tiempo real

### 🏛️ **Tipos de Vehículos:**

| Tipo             | Atributos Específicos | Marcas Disponibles                 |
| ---------------- | --------------------- | ---------------------------------- |
| **🚗 Auto**      | Número de puertas     | Ford, Chevrolet, Toyota, BMW, etc. |
| **🏍️ Moto**      | Cilindrada            | Honda, Yamaha, Kawasaki, BMW, etc. |
| **🚚 Camioneta** | Capacidad de carga    | Renault, Nissan, Jeep, Dodge, RAM  |

## 🛠️ Tecnologías Utilizadas

- **☕ Java 17+** - Lenguaje principal
- **🎨 JavaFX** - Interfaz gráfica de usuario
- **📊 JSON** - Persistencia de datos
- **📋 CSV** - Importación/exportación
- **🏗️ PlantUML** - Documentación de arquitectura

## 📁 Estructura del Proyecto

```
ConsecionariaDeVehiculos/
├── 📦 src/
│   ├── 🏛️ Models/           # Clases de dominio (Vehiculo, Auto, Moto, Camioneta)
│   ├── 🎮 Controllers/      # Controladores JavaFX
│   ├── ⚙️ Gestor/          # AdministradorVehiculos
│   ├── 🔗 Interfaces/       # Contratos del sistema
│   ├── 📝 Enums/           # Enumeraciones (Tipos, Estados, Marcas)
│   ├── ✅ Validations/      # Validadores de datos
│   ├── ❌ Exceptions/       # Excepciones personalizadas
│   └── 💾 Utilities/        # Utilidades de persistencia
├── 🎨 resources/           # Archivos FXML y estilos
├── 📊 uml/                # Diagramas de arquitectura
└── 📖 README.md           # Esta documentación
```

## 🎯 **Puntos Destacados de la Arquitectura:**

### 🏗️ **Patrones Implementados:**

- **🏛️ Factory Pattern** - Creación de vehículos según tipo
- **🎯 Strategy Pattern** - Validaciones específicas por tipo
- **🔗 Observer Pattern** - Actualización de vistas
- **💾 Repository Pattern** - Persistencia de datos

### 🔐 **Principios SOLID:**

- **S** - Responsabilidad única en cada validador
- **O** - Extensible para nuevos tipos de vehículos
- **L** - Substitución de Liskov en jerarquía de vehículos
- **I** - Interfaces segregadas (CRUD, IVehiculoEditable)
- **D** - Inversión de dependencias con interfaces

## 🚀 Instalación y Uso

### 📋 **Requisitos:**

- Java 17 o superior
- JavaFX 17+
- IDE compatible (VS Code, IntelliJ, Eclipse)

### ▶️ **Ejecución:**

```bash
# Clonar repositorio
git clone [URL-del-repositorio]

# Compilar y ejecutar
javac -cp "path/to/javafx/lib/*" src/**/*.java
java -cp "path/to/javafx/lib/*:src" Controllers.MainViewController
```

## 📈 **Métricas del Proyecto:**

- **📦 Clases:** 25+
- **🔗 Interfaces:** 5
- **📝 Enums:** 6
- **✅ Validadores:** 4
- **🎮 Controllers:** 4
- **🧪 Tests:** En desarrollo

---

## 👨‍💻 **Desarrollado por:**

**Santino Casado** - Programación II - 2024

### 📊 **Ver Diagramas:**

- [🌍 Diagrama General](uml/uml_general.png) - Vista completa del sistema
- [📦 Por Módulos](uml/) - Diagramas específicos por área

_Para generar los diagramas: usar PlantUML con los archivos .puml_
