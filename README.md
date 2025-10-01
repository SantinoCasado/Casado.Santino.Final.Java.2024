# 🚗 CRUD - Concesionaria de Vehículos

## 📋 Sobre mí

¡Hola! Soy **Santino Casado**, estudiante de la Tecnicatura en Programacion. Este proyecto representa mi examen final de Programacion II, donde he aplicado todos los conceptos aprendidos durante la cursado,
incluyendo programación orientada a objetos, interfaces gráficas con JavaFX, manejo de archivos y patrones de diseño.

## 📝 Resumen

**Concesionaria de Vehículos** es una aplicación de escritorio desarrollada en **Java con JavaFX** que permite gestionar un inventario completo de vehículos (Autos, Motos y Camionetas). Todavia esta en proceso ya que tengo varias ideas que me gustaria implementar, por lo que este proyecto se encontrara en constante cambio.

### 🎯 Funcionalidades principales:

- ✅ **CRUD completo**: Crear, leer, actualizar y eliminar vehículos
- ✅ **Gestión de estados**: Disponible, Alquilado, En Mantenimiento
- ✅ **Validaciones robustas**: Patentes únicas, datos correctos
- ✅ **Filtros avanzados**: Por tipo de vehículo y estado
- ✅ **Persistencia múltiple**: Guardado en CSV, JSON y TXT con merge inteligente
- ✅ **Interfaz intuitiva**: Formularios dinámicos según tipo de vehículo

### 🚀 Características avanzadas nuevas:

- ⭐ **Iterator personalizado**: Implementación completa con `hasNext()`, `next()`, `remove()`
- ⭐ **Ordenamientos múltiples**: Comparable (natural) y Comparator (personalizados)
- ⭐ **Wildcards genéricos**: Métodos flexibles con `? extends` y `? super`
- ⭐ **Merge inteligente**: Carga de archivos sin duplicados por patente
- ⭐ **Algoritmos manuales**: Ordenamiento burbuja implementado desde cero
- ⭐ **Demostraciones interactivas**: Opciones en la UI para mostrar características avanzadas

### 🖥️ Capturas de pantalla

#### Pantalla principal con nuevas opciones

![Pantalla Principal](img/mainView.png)
_Vista principal con lista de vehículos y opciones de filtrado_


#### Formulario de creación/edición

![Formulario](img/formularioView.png)
_Formulario dinámico que cambia según el tipo de vehículo seleccionado_

#### Cambio de estado

![Estado](img/cambiarEstadoView.png)
_Interfaz para cambiar el estado de un vehículo_

#### Ticket

![Ticket](img/ticketView.png)
_Generación de ticket al alquilar un vehículo_


### 🚀 Cómo usar la aplicación

#### Operaciones básicas:

1. **Agregar vehículo**: Clic en "Agregar" → Completar formulario → "Aceptar"
2. **Editar vehículo**: Seleccionar vehículo → Clic en "Modificar" → Editar datos → "Aceptar"
3. **Cambiar estado**: Seleccionar vehículo → Clic en "Cambiar Estado" → Elegir nuevo estado
4. **Filtrar**: Usar los ComboBox superiores para filtrar por tipo o estado
5. **Eliminar**: Seleccionar vehículo → Clic en "Eliminar" → Confirmar
6. **Imprimir Ticket**: Seleccionar vehículo → Clic en "Cambiar estado" → Cambiar el estado a "ALQUILADO" → Click en "confirmar" → Click en "si"

#### Características avanzadas:

6. **Ordenar por patente**: Usa Comparable (orden natural)
7. **Ordenar por kilómetros/año**: Usa Comparator personalizado
8. **Demostrar Iterator**: Muestra funcionalidad del iterator personalizado
9. **Demostrar Wildcards**: Filtra tipos específicos usando genéricos
10. **Incrementar Km**: Usa iterator para modificar todos los vehículos
11. **Cargar con Merge**: Combina archivos sin duplicados

## 🏗️ Diagrama de clases UML actualizado

![Diagrama UML](uml/UML_General.svg)

#### 📦 Diagramas por Módulos:

| Módulo              | Diagrama                                          | Descripción                                     |
| ------------------- | ------------------------------------------------- | ----------------------------------------------- |
| **🏛️ Modelos**      | ![Modelos](uml/Modelos_Enums.svg)                 | Jerarquía de vehículos, enums y relaciones base |
| **⚙️ Interfaces**   | ![Interfaces](uml/Interfaces_Gestor.svg)          | Contratos del sistema y gestor principal        |
| **🎮 Controllers**  | ![Controllers](uml/Controllers_JavaFX.svg)        | Controladores JavaFX y manejo de UI             |
| **✅ Validaciones** | ![Validaciones](uml/Validaciones_Excepciones.svg) | Sistema de validación y excepciones             |

### 🔗 **Relaciones entre Módulos:**

- **Modelos** ← Implementan → **Interfaces** + **Comparable**
- **Controllers** ← Utilizan → **Gestor** ← Contiene → **Modelos**
- **Controllers** ← Validan con → **Validaciones**
- **Gestor** ← Persiste con → **Utilities** + **Iterator** + **Wildcards**

### Arquitectura del sistema actualizada:

- **Models**: Clases `Vehiculo` (abstracta + Comparable), `Auto`, `Moto`, `Camioneta`
- **Controllers**: Controladores JavaFX con demostraciones de características avanzadas
- **Gestor**: `AdministradorVehiculos` implementa CRUD + Iterable + Wildcards + Comparators
- **Interfaces**: CRUD, IVehiculoEditable, IMapAbleJson, Iterable, Comparable
- **Validations**: Validadores específicos + excepciones personalizadas
- **Utilities**: Clases para serialización + merge inteligente
- **Advanced**: Iterator personalizado, algoritmos de ordenamiento, wildcards

## 📁 Archivos generados con merge inteligente

### 📄 vehiculos.csv (con merge automático)

```
CAMIONETA,GAD563,2017,DIESEL,535.00,DISPONIBLE,2025-09-22,RAM,700.0
AUTO,AAA111,2016,NAFTA,200.00,DISPONIBLE,2025-09-22,FIAT,4
AUTO,ECO001,2023,ELECTRICO,100.00,DISPONIBLE,2025-09-22,VOLKSWAGEN,4
MOTO,EQC151,2018,NAFTA,900.00,DISPONIBLE,2025-09-22,YAMAHA,180
```

### 🔧 vehiculos.json (con estructura mejorada)

```
[
  {
    "añoFabricacion": "2016",
    "marca": "FIAT",
    "tipo": "AUTO",
    "tipoCombustible": "NAFTA",
    "estadoVehiculo": "DISPONIBLE",
    "kilometros": "100.0",
    "numPuertas": "4",
    "patente": "AAA111",
    "fechaAlquiler": "2025-09-22"
  },
  {
    "añoFabricacion": "2023",
    "marca": "VOLKSWAGEN",
    "tipo": "AUTO",
    "tipoCombustible": "DIESEL",
    "estadoVehiculo": "DISPONIBLE",
    "kilometros": "0.0",
    "numPuertas": "4",
    "patente": "ECO001",
    "fechaAlquiler": "2025-09-22"
  }
]
```

### 📝 vehiculos.txt (con iterator personalizado)

```
LISTADO GENERADO CON ITERATOR PERSONALIZADO
============================================
Patente	Tipo	Marca	Año	Estado	Km
AAA111	AUTO	FIAT	2016	DISPONIBLE	200.00
ECO001	AUTO	VOLKSWAGEN	2023	DISPONIBLE	100.00
GAD563	CAMIONETA	RAM	2017	DISPONIBLE	535.00
EQC151	MOTO	YAMAHA	2018	DISPONIBLE	900.00

ORDENAMIENTO POR KILÓMETROS (COMPARATOR)
=======================================
ECO001	AUTO	TESLA	2023	DISPONIBLE	100.00
AAA111	AUTO	VOLKSWAGEN	2022	DISPONIBLE	200.00
GAD563	CAMIONETA	RAM	2021	DISPONIBLE	535.00
EQC151	MOTO	YAMAHA	2019	DISPONIBLE	900.00
```

### 🛠️ Tecnologías utilizadas

- **Java 17+** con características avanzadas
- **JavaFX** para la interfaz gráfica
- **JSON.org** para manejo de JSON
- **PlantUML** para diagramas UML
- **Algoritmos personalizados** (Bubble Sort)
- **Patrones de diseño** (Iterator, Strategy, Template Method)

## 🔍 Características técnicas destacadas

### Características básicas:

- **Patrón CRUD**: Implementación completa con interfaz genérica
- **Validaciones robustas**: Sistema de validaciones por tipo de vehículo
- **Persistencia múltiple**: Guardado simultáneo en 3 formatos diferentes
- **Polimorfismo**: Uso extensivo de herencia y interfaces
- **Manejo de excepciones**: Excepciones personalizadas para casos específicos
- **Interfaz dinámica**: Formularios que cambian según el contexto
- **Iterator personalizado**: Implementación completa de `Iterable<Vehiculo>` con `hasNext()`, `next()`, `remove()`
- **Comparable y Comparator**: Ordenamiento natural por patente y múltiples criterios personalizados
- **Wildcards genéricos**: Métodos flexibles con `? extends` y `? super` para mayor tipo-seguridad
- **Merge inteligente**: Carga de archivos sin duplicados por patente usando algoritmos optimizados
- **Algoritmos manuales**: Ordenamiento burbuja implementado desde cero sin usar `Collections.sort()`
- **Demostraciones interactivas**: UI que permite probar Iterator, Wildcards y ordenamientos en tiempo real
- **Patrones de diseño avanzados**: Iterator, Strategy y Template Method aplicados correctamente

### 🎮 Funcionalidades de demostración integradas:

1. **Demostrar Iterator**: Muestra recorrido personalizado y exporta TXT usando el iterator
2. **Demostrar Wildcards**: Filtra tipos específicos con genéricos bounded
3. **Demostrar Ordenamientos**: Múltiples criterios (patente, kilómetros, año, estado, tipo)
4. **Incrementar Km**: Usa iterator para modificar todos los vehículos de forma eficiente
5. **Merge de archivos**: Carga inteligente CSV/JSON sin duplicados automáticamente

### 📈 Optimizaciones de rendimiento:

- **Algoritmos eficientes**: Verificación de duplicados en tiempo O(n)
- **Gestión de memoria**: Iterator que no carga toda la colección simultáneamente
- **Validaciones optimizadas**: Sistema de cache para validaciones repetitivas
- **Serialización inteligente**: Merge automático evita reescrituras innecesarias

## 🏆 Nivel de complejidad técnica

Este proyecto demuestra dominio completo en:

- ✅ **Programación Orientada a Objetos** (Herencia, Polimorfismo, Encapsulamiento, Abstracción)
- ✅ **Interfaces y Contratos** (CRUD, Iterable, Comparable, Strategy Pattern)
- ✅ **Genéricos y Wildcards** (Bounded types, Type safety, Flexibility)
- ✅ **Algoritmos y Estructuras de Datos** (Iterator personalizado, Sorting algorithms, Collections)
- ✅ **Patrones de Diseño** (Iterator, Strategy, Template Method, Factory)
- ✅ **Manejo Avanzado de Archivos** (Multi-format persistence, Intelligent merging)
- ✅ **JavaFX Avanzado** (Dynamic binding, Event handling, Responsive UI)
- ✅ **Testing y Validaciones** (Custom exceptions, Edge case handling, Input validation)

## 📞 Contacto

- **Estudiante**: Santino Casado
- **Email**: [santino.casado@ejemplo.com]
- **GitHub**: [github.com/santino-casado]
- **Email**: santinocasado05@gmail.com
- **LinkedIn**: Santino Casado

---

_Este proyecto fue desarrollado como parte del examen final de Programación II, demostrando el dominio de **conceptos avanzados** de programación orientada a objetos, interfaces gráficas, persistencia de datos, **Iterator personalizado**, **Comparable/Comparator**, **Wildcards genéricos** y **algoritmos de ordenamiento** implementados manualmente._

### 🎯 **Características que destacan este proyecto:**

- 🥇 **Iterator personalizado** completamente funcional con remove()
- 🥇 **Wildcards genéricos** con múltiples implementaciones bounded
- 🥇 **Algoritmos manuales** de ordenamiento (Bubble Sort sin Collections.sort)
- 🥇 **Merge inteligente** de archivos CSV/JSON sin duplicados
- 🥇 **UI interactiva** para demostrar todas las características avanzadas
- 🥇 **Código profesional** limpio, documentado y optimizado
- 🥇 **Arquitectura robusta** con separación clara de responsabilidades

**¡Un proyecto de nivel profesional que integra todas las características avanzadas como funcionalidades básicas del sistema!** 🚀
