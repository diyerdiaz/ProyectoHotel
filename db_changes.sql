-- db_changes.sql
-- Ejecutar en PostgreSQL para actualizar el esquema de la base de datos

-- 1. Añadir idcliente a la tabla reserva
ALTER TABLE reserva ADD COLUMN idcliente INT;

-- 2. Asegurarse de que habitacion sea un identificador (opcional, si antes era un string de numero, puede que queramos cambiarlo a INT)
-- Si 'habitacion' era un VARCHAR y guardaba el numero, podemos crear una columna 'idhabitacion' y borrar la otra
-- ALTER TABLE reserva ADD COLUMN idhabitacion INT;
-- ALTER TABLE reserva DROP COLUMN habitacion;
-- (Por simplicidad en el código Java, vamos a añadir idcliente y idhabitacion)
ALTER TABLE reserva ADD COLUMN idhabitacion INT;

-- Migración de datos (si aplica y si "habitacion" guardaba el número)
-- UPDATE reserva SET idhabitacion = (SELECT idhabitacion FROM habitaciones WHERE CAST(numerohabitacion AS VARCHAR) = reserva.habitacion);

-- Se sugiere hacer 'habitacion' deprecated y usar 'idhabitacion'.
