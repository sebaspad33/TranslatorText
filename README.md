## Base de datos utilizado para este proyecto :

- Este documento describe cómo gestionar la tabla translations en PostgreSQL para almacenar y manejar traducciones con restricciones de 
  integridad de datos.


**Base de Datos Utilizada**
- Para ver las bases de datos disponibles en tu instancia de PostgreSQL, puedes utilizar el siguiente comando:

```
SELECT datname FROM pg_database;
```

**Eliminación de la Tabla**
- Si la tabla translations ya existe y deseas eliminarla antes de recrearla, ejecuta:

```
DROP TABLE translations;
```

**Creación de la Tabla translations**
- La tabla translations está diseñada para almacenar textos originales, sus traducciones y los códigos de idioma de origen y destino, con 
  restricciones que garantizan la calidad de los datos.

```
CREATE TABLE translations (
    id SERIAL PRIMARY KEY,
    request_text TEXT NOT NULL CHECK (length(request_text) <= 1000),
    translated_text TEXT NOT NULL CHECK (length(translated_text) <= 1000),
    from_lang TEXT NOT NULL CHECK (length(from_lang) = 2),
    to_lang TEXT NOT NULL CHECK (length(to_lang) = 2),
    status TEXT NOT NULL default 'A' CHECK (status IN ('A', 'I'))
);
 ```

**Explicación de las Restricciones**

```
a) NOT NULL: Asegura que los campos no puedan ser NULL. Esto es importante para garantizar que todos los registros tengan datos completos.
b) CHECK (length(request_text) <= 1000): Limita la longitud de los campos de texto a 1000 caracteres.
c) CHECK (length(from_lang) = 2): Almacena los códigos de idiomas (por ejemplo, en para Inglés).
d) CHECK (status IN ('A', 'I')): Restringe el campo a un conjunto especifico de valores ('A' para activo, 'I' para inactivo).
```


**Inserción de Datos en la Tabla**
- Para insertar una nueva traducción en la tabla translations, utiliza el siguiente comando SQL:

```
INSERT INTO translations (request_text, translated_text, from_lang, to_lang)
VALUES ('Hello, how are you?', 'Hola, ¿cómo estás?', 'en', 'es');
```

**Consultar Datos en la Tabla**
- Puedes consultar todos los registros almacenados en la tabla translations con el siguiente comando:

```
SELECT * FROM translations;
```
