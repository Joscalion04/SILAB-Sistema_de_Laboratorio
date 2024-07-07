# SILAB-Sistema_de_Laboratorio
Este repositorio almacena un proyecto Java Desktop con MVC para una aplicacion de gestion de inventario de instrumetnos de laboratorio

## Características

- **View:** Utilizando el patron MVC en desktop con Java Swing para crear una interfaz intuitiva y simple, contando con validaciones de comportamiento.
- **Modelo:** Implementado en Java el modelo de la aplicacion y logica necesaria, contando con el POO aplicado en la practica.
- **Persistencia de Datos:** Se utiliza una persistencia en XML para almacenar los datos en cambio para proteger su integridad relacional.

## Funcionalidades

## 1. Categoría de Instrumentos

**Operaciones para Categoría de Instrumentos:**
- **Crear Categoría de Instrumentos:** Permite al usuario crear una nueva categoría de instrumentos y asociar una nueva unidad de medición.
- **Buscar Categoría de Instrumentos:** Permite al usuario buscar las categorías de instrumentos existentes.
- **Actualizar Categoría de Instrumentos:** Permite al usuario modificar una categoría de instrumentos existente.
- **Eliminar Categoría de Instrumentos:** Permite al usuario eliminar una categoría de instrumentoscon sus validaciones necesarias para la proteccion de integridad de datos.

## 2. Instrumentos

**Operaciones para Instrumentos:**
- **Crear Instrumento:** Permite al usuario registrar un nuevo instrumento y asociarlo a una nueva o existente categoría de instrumentos.
- **Buscar Instrumento:** Permite al usuario buscar los instrumentos registrados.
- **Actualizar Instrumento:** Permite al usuario modificar un instrumento existente.
- **Eliminar Instrumento:** Permite al usuario eliminar un instrumento registrado con sus validaciones necesarias para la proteccion de integridad de datos.

## 3. Calibraciones

**Operaciones para Calibraciones:**
- **Crear Calibración:** Permite al usuario registrar una nueva calibración utilizando un instrumento previamente seleccionado.
- **Buscar Calibración:** Permite al usuario buscar las calibraciones registradas, incluyendo los detalles del instrumento asociado.
- **Actualizar Calibración:** Permite al usuario modificar una calibración existente.
- **Eliminar Calibración:** Permite al usuario eliminar una calibración registrada con sus validaciones necesarias para la proteccion de integridad de datos.

## 4. Mediciones

**Operaciones para Mediciones:**
- **Crear Medición:** Permite al usuario registrar una nueva medición en una calibración de un instrumento previamente seleccionado.
- **Buscar Medición:** Permite al usuario buscar las mediciones registradas en una calibració.
- **Actualizar Medición:** Permite al usuario modificar una medición existente en una calibración.
- **Eliminar Medición:** Permite al usuario eliminar una medición registrada en una calibración con sus validaciones necesarias para la proteccion de integridad de datos.

## Instalación
Para ejecutar este proyecto localmente en IntelliJIDEA se requiere descargar las dependendecias ejecutando el pom.xml

## Creditos
Este repositorio fue realizado como proyecto universitario.
Integrantes:
 - Alessandro Cambronero
 - Ignacio Arrieta
 - Domingo Camacho
 - Joseph Leon (Joscalion04)
