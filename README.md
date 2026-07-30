<div align="center">

# 🏨 Hotel Gales — Sistema de Gestión Hotelera

![Java](https://img.shields.io/badge/Java-24-ED8B00?logo=java&logoColor=white)
![NetBeans](https://img.shields.io/badge/NetBeans-24-1B6AC6?logo=apachenetbeanside&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)
![FlatLaf](https://img.shields.io/badge/FlatLaf-3.4-0052CC)
![Swing](https://img.shields.io/badge/UI-Swing-007396)
![Ecosistema](https://img.shields.io/badge/Ecosistema-Web-gold?style=flat&logo=flask)

> **Repositorio:** [github.com/anfeospa999-oss/hotel-management-system-desktop](https://github.com/anfeospa999-oss/hotel-management-system-desktop)

---

| 🖥️ Versión de Escritorio | 🌐 Versión Web |
|:---|:---|
| **Tecnologías** | **Tecnologías** |
| • Java 24 | • Flask |
| • PostgreSQL | • PostgreSQL |
| • Swing | • Bootstrap |
| • Apache NetBeans | • HTML / CSS / JavaScript |
| **Repositorio** | **Repositorio** |
| Este repositorio | [github.com/anfeospa999-oss/hotel-management-system-web](https://github.com/anfeospa999-oss/hotel-management-system-web) |

```
                    🏨 HOTEL GALES
                          │
             ┌────────────┴────────────┐
             │                         │
       Desktop Java              Portal Web
       (Escritorio)                (Web)
       Java · Swing ·          Flask · Bootstrap ·
       PostgreSQL              PostgreSQL
```

*Dos plataformas. Un mismo sistema. Una sola identidad.*

</div>

---

## 📑 Índice

- [Descripción General](#-descripción-general)
- [¿Por qué este proyecto?](#-por-qué-este-proyecto)
- [Características principales](#-características-principales)
- [Demo](#-demo)
- [Información del Proyecto](#-información-del-proyecto)
- [Funcionalidades](#-funcionalidades)
- [Capturas del Sistema](#-capturas-del-sistema)
- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Roadmap](#-roadmap)
- [Configuración Rápida](#-configuración-rápida)
- [Variables de Entorno](#-variables-de-entorno)
- [Equipo de Desarrollo](#-equipo-de-desarrollo)
- [Mi Participación](#-mi-participación)
- [Estado del Proyecto](#-estado-del-proyecto)
- [Próximas Mejoras](#-próximas-mejoras)
- [Licencia](#-licencia)

---

## 📖 Descripción General

**Hotel Gales** es un sistema de gestión hotelera de escritorio construido con **Java 24** y **Swing**, utilizando **NetBeans 24** como IDE y **PostgreSQL 16** como motor de base de datos. La interfaz gráfica utiliza **FlatLaf 3.4** (FlatDarkLaf) para un aspecto moderno y profesional con una paleta de colores dorado (`#C9A84C`) y azul marino (`#0F2137`).

El sistema implementa una arquitectura **MVC (Modelo-Vista-Controlador)** con autenticación por roles (administrador, recepcionista, cliente), permitiendo gestionar clientes, empleados, habitaciones, reservas y facturación desde una interfaz unificada tipo **MDI (Multiple Document Interface)** con escritorio virtual y barra lateral navegable.

Forma parte del ecosistema **Hotel Gales**, que incluye una [versión web](https://github.com/anfeospa999-oss/hotel-management-system-web) desarrollada con Flask y Bootstrap, ambas compartiendo la misma base de datos PostgreSQL y la identidad visual del hotel.

---

## 💡 ¿Por qué este proyecto?

Este sistema fue desarrollado como proyecto académico durante la formación en **Análisis y Desarrollo de Software** del **SENA**, con el objetivo de aplicar los conocimientos adquiridos en un caso de uso real: la automatización de los procesos operativos de un hotel.

**Objetivos del proyecto:**

- **Aprendizaje práctico**: Implementar una aplicación de escritorio completa utilizando Java, Swing y PostgreSQL, integrando conceptos de POO, MVC, JDBC y cifrado de contraseñas.
- **Arquitectura limpia**: Separar responsabilidades en tres capas (modelo, vista, controlador) para facilitar el mantenimiento, la escalabilidad y la reutilización del código.
- **Trabajo colaborativo**: Utilizar Git y GitHub como herramientas de control de versiones y colaboración en equipo, incluyendo manejo de ramas, pull requests y resolución de conflictos.
- **Calidad de software**: Aplicar buenas prácticas como validación de datos, manejo de excepciones, carga asíncrona con `SwingWorker` y notificaciones visuales para mejorar la experiencia de usuario.
- **Ecosistema multiplataforma**: Demostrar la capacidad de construir un mismo sistema en dos tecnologías diferentes (escritorio con Java/Swing y web con Flask/Bootstrap), manteniendo la misma lógica de negocio y base de datos.

La experiencia adquirida abarca desde el modelado de bases de datos relacionales con PostgreSQL hasta el diseño de interfaces gráficas modernas con FlatLaf, pasando por la implementación de patrones de diseño, autenticación por roles y generación automática de facturas.

---

## ✨ Características principales

| | |
|:---|:---|
| ✔ Arquitectura MVC | ✔ Autenticación por roles |
| ✔ Dashboard de estadísticas | ✔ CRUD completo |
| ✔ Facturación automática | ✔ PostgreSQL |
| ✔ FlatLaf moderno | ✔ Portal Web complementario |

---

<p align="center">
  <img src="screenshots/02-dashboard.png" alt="Dashboard Principal del sistema Hotel Gales" width="750">
  <br>
  <em>Panel de control principal con estadísticas en tiempo real</em>
</p>

---

## 🎥 Demo

<p align="center">
  <img src="screenshots/demo.gif" alt="Demostración animada del sistema Hotel Gales" width="750">
</p>

> Próximamente se añadirá una demostración animada del sistema en funcionamiento.  
> *Estructura preparada para reemplazar con un GIF real.*

---

## 📊 Información del Proyecto

| Ítem | Detalle |
|:-----|:--------|
| ☕ **Lenguaje** | Java JDK 24 |
| 🖥️ **IDE** | Apache NetBeans 24 |
| 🗄️ **Base de Datos** | PostgreSQL 16 |
| 📦 **Clases totales** | 36 (10 controladores + 8 modelos + 16 vistas + 2 utilidades) |
| 🧩 **Módulos del sistema** | 9 (Usuarios, Clientes, Empleados, Habitaciones, Tipos Hab., Reservas, Facturas, Estadísticas, Login) |
| 🪟 **Formularios/ventanas** | 14 (1 login, 1 registro, 1 principal, 1 stats, 1 frame genérico, 9 diálogos CRUD) |
| 👥 **Roles de usuario** | 3 (Administrador, Recepcionista, Cliente) |
| 🗓️ **Historial Git** | 44+ commits |

---

## ⚙️ Funcionalidades

### 🔐 Autenticación y Roles
- **Login** con credenciales cifradas (SHA-256) y opción "Recordarme"
- **Registro** de nuevos usuarios con creación automática de perfil cliente
- **Tres roles** con permisos diferenciados:
  - **Administrador**: acceso completo a todos los módulos
  - **Recepcionista**: gestión de reservas, clientes, habitaciones y facturación
  - **Cliente**: consulta de sus propias reservas y facturas

### 📊 Panel de Estadísticas (Dashboard)
- Hero con bienvenida, badge de rol e ingresos del día/semana/mes
- **Tarjetas resumen**: total habitaciones, disponibles, ocupadas, mantenimiento, clientes, empleados, ingresos totales, reservas activas
- **Alertas del día**: check-ins hoy, check-outs hoy, mantenimiento, pagos pendientes
- **Gráficos de barras**: ingresos mensuales (12 meses), reservas por tipo de habitación
- **Tablas**: próximas reservas (7 días), últimas facturas
- Botón **↻ Refrescar** para recargar datos en segundo plano (SwingWorker)

### 👥 Gestión de Usuarios (Admin)
- CRUD completo de usuarios del sistema
- Asignación de rol (administrador, recepcionista, cliente)
- Contraseñas cifradas con SHA-256

### 👤 Gestión de Clientes
- CRUD completo con tabla filtrable
- Vista combinada con nombre de usuario (LEFT JOIN)
- Auto-creación de cuenta de usuario al registrar cliente

### 👨‍💼 Gestión de Empleados
- CRUD completo con formato de salario en pesos colombianos
- Validación de campos con bordes rojos
- Auto-creación de cuenta de usuario

### 🚪 Gestión de Habitaciones
- CRUD completo con tabla filtrable
- Estados: DISPONIBLE, OCUPADA, MANTENIMIENTO
- Asignación de tipo de habitación y precio por noche
- Cambio automático de estado al crear reservas

### 🏷️ Tipos de Habitación
- CRUD de categorías (Sencilla, Doble, Suite, Matrimonial, etc.)

### 📅 Gestión de Reservas
- CRUD completo con selectores de cliente y habitación (cargados desde BD)
- Selección de número de personas (1–8) y medios de pago (EFECTIVO, TRANSFERENCIA)
- Fechas con selector de calendario (JSpinner DateEditor)
- Auto-generación de factura y cambio automático de estado de habitación a OCUPADA

### 🧾 Facturación
- Tabla con columna de acciones "Ver Factura" (botón dorado)
- Diálogo de detalle con información de factura, huésped y estadía
- Botón "Marcar como Pagada" para facturas pendientes
- Factura auto-generada desde la reserva (cálculo: noches × precio habitación)
- Estados: PENDIENTE, PAGADA, CANCELADA, ANULADA, PROCESADA

### 🎨 Interfaz de Usuario
- FlatLaf (FlatDarkLaf) con paleta dorado/azul marino
- Sidebar colapsable con secciones por rol
- Topbar con badge de usuario/rol y botón de salir
- Tablas con cabeceras oscuras y búsqueda filtro
- Notificaciones tipo toast (success, error, warning, confirm)
- Botones consistentes con estilos dorados

### 🌐 Portal Web (Extra)
- Portal web complementario del ecosistema Hotel Gales, accesible desde navegador
- Diseño premium con la identidad visual del hotel
- Versión web del mismo sistema, enfocada en acceso de personal

---

## 📸 Capturas del Sistema

| Pantalla | Vista |
|:---------|:------|
| **Inicio de Sesión** | ![Login](screenshots/01-login.png) |
| **Dashboard / Estadísticas** | ![Dashboard](screenshots/02-dashboard.png) |
| **Usuarios (CRUD)** | ![Usuarios](screenshots/04-usuarios.png) |
| **Clientes (CRUD)** | ![Clientes](screenshots/05-clientes.png) |
| **Empleados (CRUD)** | ![Empleados](screenshots/06-empleados.png) |
| **Habitaciones (CRUD)** | ![Habitaciones](screenshots/07-habitaciones.png) |
| **Reservas (CRUD)** | ![Reservas](screenshots/08-reservas.png) |
| **Facturación** | ![Facturación](screenshots/10-facturacion.png) |

---

## 🛠 Tecnologías

<div align="center">

| Categoría | Tecnología |
|:----------|:-----------|
| **Backend** | Java 24 |
| **Interfaz gráfica** | Swing + FlatLaf (FlatDarkLaf) |
| **Base de datos** | PostgreSQL 16 |
| **Conector BD** | PostgreSQL JDBC 42.7.11 |
| **IDE** | Apache NetBeans 24 |
| **Arquitectura** | MVC |
| **Capa de datos** | JDBC |
| **Cifrado** | SHA-256 |

</div>

---

## 🏗 Arquitectura

El proyecto sigue el patrón **MVC (Modelo-Vista-Controlador)** con tres capas bien definidas y un flujo de datos unidireccional:

```
                     ┌──────────────────┐
                     │   USUARIO        │
                     │ (Interfaz gráfica)│
                     └────────┬─────────┘
                              │ Acción del usuario
                              ▼
┌─────────────────────────────────────────────────────┐
│                     VISTA                            │
│           (Swing — JFrame, JDialog, JPanel)          │
│         Muestra datos · Captura eventos             │
└────────────────────────┬────────────────────────────┘
                         │ Llamada al controlador
                         ▼
┌─────────────────────────────────────────────────────┐
│                   CONTROLADOR                        │
│              (Lógica de negocio)                     │
│     Valida · Orquesta · Coordina Vista y Modelo     │
└────────────────────────┬────────────────────────────┘
                         │ Consulta / modifica datos
                         ▼
┌─────────────────────────────────────────────────────┐
│                     MODELO                            │
│              (JDBC — SQL directo)                    │
│      Listar() · insertar() · modificar() · eliminar()│
└────────────────────────┬────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────┐
│                   PostgreSQL 16                       │
│              (Servidor remoto)                       │
└─────────────────────────────────────────────────────┘
```

### Capas del sistema

- **Modelo** (`modelo/`): Clases Java que encapsulan los datos y ejecutan operaciones SQL directas contra PostgreSQL mediante JDBC. Cada tabla de la base de datos tiene su clase modelo correspondiente con métodos estándar: `Listar()`, `insertar()`, `modificar()`, `eliminar()` y `buscar()`.

- **Controlador** (`Controlador/`): Orquesta la lógica de negocio actuando como puente entre la vista y el modelo. Valida los datos ingresados, ejecuta las operaciones del modelo y actualiza la interfaz gráfica con los resultados.

- **Vista** (`vista/`): Interfaces gráficas construidas con Swing (JFrame, JDialog, JInternalFrame, JPanel). Incluye formularios CRUD, tablas con filtros de búsqueda, notificaciones toast y un escritorio virtual MDI (Multiple Document Interface).

### Inicialización

La base de datos se auto-crea al iniciar la aplicación mediante `ConexionBD.inicializarEsquema()`, que ejecuta sentencias `CREATE TABLE IF NOT EXISTS` para garantizar que el esquema exista sin sobrescribir datos existentes. Las credenciales de conexión se configuran mediante variables de entorno con valores por defecto para desarrollo.

---

## 📁 Estructura del Proyecto

```
ProyectoHotel/
│
├── 📦 build.xml                   # Script de construcción (Ant)
├── 📦 manifest.mf                 # Manifiesto del archivo JAR
├── 📦 db_changes.sql              # Migraciones y cambios de esquema
│
├── 📸 screenshots/                # Capturas del sistema
│
├── 🌐 web/                        # Portal web complementario
│   ├── index.html
│   ├── css/
│   └── js/
│
├── 📚 lib/                        # Dependencias externas
│   ├── flatlaf-3.4.jar            # Look and Feel moderno
│   └── postgresql-42.7.11.jar     # Driver JDBC
│
├── 📦 dist/                       # Distribución compilada
│   └── ProyectoHotel.jar
│
├── 📂 src/                        # Código fuente
│   │
│   ├── 🧠 Controlador/            # Lógica de negocio
│   │   ├── ControladorLogin.java
│   │   ├── ControladorRegistroUsuario.java
│   │   ├── ControladorUsuario.java
│   │   ├── ControladorCliente.java
│   │   ├── ControladorEmpleado.java
│   │   ├── ControladorHabitaciones.java
│   │   ├── ControladorTipoHabitacion.java
│   │   ├── ControladorReserva.java
│   │   ├── ControladorFacturas.java
│   │   └── ControladorEstadisticas.java
│   │
│   ├── 🗄️ modelo/                 # Capa de datos / SQL
│   │   ├── ConexionBD.java        # Conexión y esquema PostgreSQL
│   │   ├── Login.java
│   │   ├── Cliente.java
│   │   ├── empleado.java
│   │   ├── Habitaciones.java
│   │   ├── TipoHabitacion.java
│   │   ├── Reserva.java
│   │   └── facturas.java
│   │
│   ├── 🖥️ vista/                  # Interfaz gráfica (Swing)
│   │   ├── MDILogin.java              → Login principal
│   │   ├── MDIRegistroUsuario.java    → Registro de usuarios
│   │   ├── VentanaPrincipal.java      → Dashboard MDI + sidebar
│   │   ├── PanelEstadisticas.java     → Estadísticas (SwingWorker)
│   │   ├── ModuleListInternalFrame    → Marco genérico para CRUD
│   │   ├── DialogCliente.java         → CRUD Cliente
│   │   ├── DialogEmpleado.java        → CRUD Empleado
│   │   ├── DialogHabitacion.java      → CRUD Habitación
│   │   ├── DialogReserva.java         → CRUD Reserva
│   │   ├── DialogFactura.java         → Detalle de Factura
│   │   ├── DialogUsuario.java         → CRUD Usuario
│   │   ├── DialogTipoHabitacion.java  → CRUD Tipo Habitación
│   │   ├── CardPanel.java             → Componente tarjeta para stats
│   │   ├── RoundedButton.java         → Botón personalizado
│   │   ├── TableHeaderRenderer.java   → Renderizador de cabeceras
│   │   └── ToastNotifier.java         → (en util/)
│   │
│   ├── 🔧 util/                    # Utilidades
│   │   ├── Encriptador.java        # SHA-256
│   │   └── ToastNotifier.java      # Notificaciones toast
│   │
│   ├── 🖼️ Imagenes/                # Recursos gráficos
│   │   ├── ImagenFondoLogin.png
│   │   ├── ImagenFondoLogin2.png
│   │   ├── Login3.png
│   │   └── imagen login 4.png
│   │
│   └── 🔩 org/netbeans/lib/awtextra/  # Layout absoluto NetBeans
│
└── ⚙️ nbproject/                   # Configuración del proyecto NetBeans
    └── project.properties
```

---

## 📈 Roadmap

| Estado | Versión | Hitos alcanzados / planificados |
|:------:|:--------|:--------------------------------|
| ✅ | **v1.0** | Login, autenticación por roles (admin, recepcionista, cliente), registro de usuarios |
| ✅ | **v1.1** | CRUD completo: clientes, empleados, habitaciones, tipos de habitación, reservas |
| ✅ | **v1.2** | Dashboard de estadísticas con gráficos de barras, tarjetas resumen y alertas del día |
| ✅ | **v1.3** | Facturación automática, cálculo de costos, detalle de factura, portal web complementario |
| 🚧 | **v1.4** | Exportación de reportes a PDF y Excel |
| 🔮 | **v2.0** | Calendario visual de reservas, notificaciones en tiempo real |
| 🔮 | **v2.1** | API REST para integración con aplicaciones móviles |
| 🔮 | **v2.2** | Módulo de limpieza y mantenimiento con asignación automática de tareas |

---

## ⚡ Configuración Rápida

### Requisitos

| Recurso | Versión | Descarga |
|:--------|:--------|:---------|
| Java JDK | 24 o superior | [jdk.java.net](https://jdk.java.net/) |
| Apache NetBeans | 24 | [netbeans.apache.org](https://netbeans.apache.org/) |
| PostgreSQL | 16 o compatible | — |

### Instalación

```bash
git clone https://github.com/anfeospa999-oss/hotel-management-system-desktop.git
```

Abrir el proyecto en NetBeans: `File → Open Project → Seleccionar ProyectoHotel`.

### Configuración

Las credenciales de la base de datos se configuran mediante variables de entorno o propiedades del sistema:

| Variable | Propiedad Java | Descripción |
|:---------|:---------------|:------------|
| `HOTEL_DB_URL` | `hotel.db.url` | URL JDBC (`jdbc:postgresql://IP:PUERTO/BD`) |
| `HOTEL_DB_USER` | `hotel.db.user` | Usuario de PostgreSQL |
| `HOTEL_DB_PASSWORD` | `hotel.db.password` | Contraseña de PostgreSQL |

Por defecto, el sistema apunta al servidor remoto configurado en `ConexionBD.java`.

### Ejecución

1. Verificar que las librerías en `lib/` estén agregadas al proyecto:
   - `flatlaf-3.4.jar` — FlatLaf Look and Feel
   - `postgresql-42.7.11.jar` — Driver JDBC de PostgreSQL
2. Click derecho sobre el proyecto → **Run**
3. La clase principal es `vista.MDILogin`

### Credenciales por defecto

| Usuario | Contraseña | Rol |
|:--------|:------------|:----|
| `admin` | `admin123` | Administrador |

---

## 🌐 Variables de Entorno

| Variable | Valor por defecto | Descripción |
|:---------|:------------------|:------------|
| `HOTEL_DB_URL` | `jdbc:postgresql://164.68.98.66:5439/evaluacion` | URL de conexión a PostgreSQL |
| `HOTEL_DB_USER` | `postgres` | Usuario de base de datos |
| `HOTEL_DB_PASSWORD` | `Sena2026*` | Contraseña de base de datos |

> **Nota:** Se recomienda configurar estas variables mediante propiedades del sistema (`-Dhotel.db.url=...`) o variables de entorno del sistema operativo en lugar de modificar el código fuente.

---

## 👨‍💻 Equipo de Desarrollo

Proyecto desarrollado como parte del programa de formación del **SENA** (Servicio Nacional de Aprendizaje) — Tecnólogo en Análisis y Desarrollo de Software.

| Integrante | Rol | Contribuciones principales |
|:-----------|:----|:---------------------------|
| **Andres Felipe Ospina** | Desarrollador | Dashboard/Estadísticas, UI/UX, Facturación, Portal Web, Refactorización general |
| **Juan Sar2107** | Desarrollador | Modelos, Controladores, CRUDs base, Base de datos, Lógica de negocio |
| **Diyer Diaz** | Desarrollador | Modelos iniciales, Estructura del proyecto, Commit inicial |

---

## 🚀 Mi Participación (Andres Felipe Ospina)

Como parte del equipo de desarrollo, mis contribuciones se enfocaron en la modernización de la interfaz, la implementación del dashboard de estadísticas y la mejora continua de la experiencia de usuario.

### 🎨 UI/UX y Diseño Visual

- Diseño e implementación de la paleta de colores dorado/azul marino con **FlatLaf (FlatDarkLaf)**
- Sidebar responsiva colapsable con secciones dinámicas según el rol del usuario
- Topbar profesional con badge de usuario/rol y botón de cierre de sesión
- Cabeceras de tabla con fondo oscuro y tipografía blanca
- Botones con estilos dorados, bordes redondeados y efectos hover
- Tablas con color de selección sólido (sin transparencia) para mejorar la legibilidad

### 📊 Dashboard y Analítica

- Panel de estadísticas completo con tarjetas resumen, gráficos de barras y tablas dinámicas
- Consultas SQL en tiempo real agrupadas por período (día, semana, mes, 12 meses)
- Alertas del día: check-ins, check-outs, habitaciones en mantenimiento, pagos pendientes
- Carga asíncrona con `SwingWorker` para mantener la interfaz receptiva durante las consultas
- Botón de refrescar con recarga de datos en segundo plano sin bloqueo de la UI

### 🧾 Módulo de Facturación

- Rediseño completo del diálogo de detalle de factura con secciones claramente diferenciadas
- Botón "Marcar como Pagada" con actualización inmediata del estado en base de datos
- Botón "Ver Factura" estilizado con icono en la tabla de facturas

### 📝 Mejoras en Formularios CRUD

- Selectores de cliente y habitación con datos cargados dinámicamente desde la base de datos
- Selector de fecha con calendario visual (`JSpinner DateEditor`)
- Formato de salario en pesos colombianos con filtro de entrada para evitar caracteres inválidos
- Validación visual con bordes rojos en campos requeridos
- Auto-creación de cuentas de usuario al registrar nuevos clientes y empleados

### 🔔 Sistema de Notificaciones

- Clase `ToastNotifier` con notificaciones tipo toast para acciones exitosas, errores y advertencias
- Diálogo de confirmación estilizado para acciones destructivas (eliminación de registros)
- Integración del sistema de notificaciones en todos los formularios CRUD del sistema

### 🌐 Portal Web Complementario

- Desarrollo del portal web responsivo que forma parte del ecosistema Hotel Gales
- Diseño premium alineado con la identidad visual corporativa del hotel
- Selector de idioma y componentes modernos para la experiencia de navegación

---

## 📈 Estado del Proyecto

```
🟢  Desarrollo activo
```

| Dimensión | Estado |
|:----------|:------:|
| Autenticación y roles | ✅ |
| CRUD completos | ✅ |
| Dashboard con estadísticas | ✅ |
| Facturación automática | ✅ |
| Portal web complementario | ✅ |
| Exportación PDF / Excel | 🚧 |
| Calendario visual de reservas | 🔲 |
| Notificaciones en tiempo real | 🔲 |

---

## 🚀 Próximas Mejoras

- [ ] Módulo de limpieza y mantenimiento con asignación de tareas
- [ ] Reportes exportables a PDF y Excel
- [ ] Gestión de consumos y servicios adicionales
- [ ] Calendario visual de reservas (vista mensual/semanal)
- [ ] Notificaciones en tiempo real (check-ins, check-outs)
- [ ] Módulo de comentarios y valoraciones de clientes
- [ ] Integración con pasarela de pagos
- [ ] Modo oscuro/claro configurable por usuario
- [ ] Historial de cambios y auditoría
- [ ] Multilenguaje (Español/Inglés) — base ya implementada en portal web

---

## 📄 Licencia

Este proyecto fue desarrollado con fines académicos durante la formación como Tecnólogo en Análisis y Desarrollo de Software del SENA. Puede utilizarse como referencia para fines educativos respetando los créditos de los autores.

---

<p align="center">
  <strong>Hotel Gales</strong><br>
  <sub>Sistema de Gestión Hotelera de Escritorio</sub><br><br>
  Versión de escritorio del ecosistema Hotel Gales.<br>
  Proyecto hermano de la <a href="https://github.com/anfeospa999-oss/hotel-management-system-web">versión web</a>.<br><br>
  Hotel Gales forma parte de un ecosistema compuesto por una<br>
  aplicación web y una aplicación de escritorio que comparten<br>
  la misma identidad visual y funcional.<br><br>
  Desarrollado con ❤️ para el SENA — Colombia 🇨🇴
</p>
