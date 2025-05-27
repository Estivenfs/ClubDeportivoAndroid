package com.deportes.clubdeportivo.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor

// Nombre de la base de datos
private const val BD_NOMBRE = "BaseDatos"
private const val BD_VERSION = 1

class BDatos(contexto: Context) : SQLiteOpenHelper(contexto, BD_NOMBRE, null, BD_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear todas las tablas necesarias
        db?.execSQL(crearTablaEmpleado())
        db?.execSQL(crearTablaCliente())
        db?.execSQL(crearTablaPagos())
        db?.execSQL(crearTablaActividades())
        db?.execSQL(crearTablaRegistroCliente())
        db?.execSQL(crearTablaInscripcion())
        db?.execSQL(crearTablaActividadPago())
        db?.execSQL(crearTriggerDeActualizacion())


        // Insertar usuario por defecto (admin / admin)
        val insertAdmin = """
        INSERT OR IGNORE INTO Usuario (nombre, clave) VALUES ('admin', 'admin')
    """.trimIndent()
        db?.execSQL(insertAdmin)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Aquí podrías manejar actualizaciones de esquema si cambian las tablas en nuevas versiones
    }

    companion object {
        fun crearTablaEmpleado(): String = """
            CREATE TABLE IF NOT EXISTS Empleado (
            id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre_usuario TEXT NOT NULL,
            contrasena_usuario TEXT NOT NULL
);
        """.trimIndent()

        fun crearTablaCliente(): String = """
            CREATE TABLE IF NOT EXISTS Cliente (
            id_cliente INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            apellido TEXT NOT NULL,
            dni TEXT NOT NULL UNIQUE,
            email TEXT,
            telefono TEXT,
            fecha_nacimiento DATE,
            cond_socio BOOLEAN NOT NULL,  -- TRUE si es socio, FALSE si no
            apto_fisico BOOLEAN NOT NULL,
            id_pago INTEGER,
            id_actividad INTEGER
            );
        """.trimIndent()

        fun crearTablaPagos(): String = """
            CREATE TABLE IF NOT EXISTS Pagos (
            id_pago INTEGER PRIMARY KEY AUTOINCREMENT,
            id_cliente INTEGER NOT NULL,
            monto REAL NOT NULL,
            cantidad_cuotas INTEGER,
            medio_pago TEXT NOT NULL,
            fecha_pago DATE NOT NULL,
            FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        );
        """.trimIndent()


        fun crearTablaActividades(): String = """
            CREATE TABLE IF NOT EXISTS Actividades (
            id_actividad INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre_actividad TEXT NOT NULL,
            precio_actividad REAL NOT NULL,
            cupo_actividad INTEGER NOT NULL,
            inscriptos INTEGER DEFAULT 0
        );
        """.trimIndent()

        fun crearTablaRegistroCliente(): String = """
            CREATE TABLE IF NOT EXISTS RegistroCliente (
            id_empleado INTEGER,
            id_cliente INTEGER,
            PRIMARY KEY (id_empleado, id_cliente),
            FOREIGN KEY (id_empleado) REFERENCES Empleado(id_usuario),
            FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
        );
        """.trimIndent()

        fun crearTablaInscripcion(): String = """
            CREATE TABLE IF NOT EXISTS Inscripcion (
            id_cliente INTEGER,
            id_actividad INTEGER,
            fecha_inscripcion DATE NOT NULL,
            PRIMARY KEY (id_cliente, id_actividad),
            FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente),
            FOREIGN KEY (id_actividad) REFERENCES Actividades(id_actividad)
        );
        """.trimIndent()

        fun crearTablaActividadPago(): String = """
            CREATE TABLE IF NOT EXISTS ActividadPago (
            id_actividad INTEGER,
            id_pago INTEGER,
            PRIMARY KEY (id_actividad, id_pago),
            FOREIGN KEY (id_actividad) REFERENCES Actividades(id_actividad),
            FOREIGN KEY (id_pago) REFERENCES Pagos(id_pago)
        );
        """.trimIndent()

        fun crearTriggerDeActualizacion(): String = """
            -- Trigger para aumentar en 1 la cantidad de inscriptos cuando un cliente se inscribe
            CREATE TRIGGER aumentar_inscriptos
            AFTER INSERT ON Inscripcion
            FOR EACH ROW
            BEGIN
                UPDATE Actividades
                SET inscriptos = inscriptos + 1
                WHERE id_actividad = NEW.id_actividad;
            END;

            -- Trigger para disminuir en 1 la cantidad de inscriptos cuando se elimina una inscripción
            CREATE TRIGGER disminuir_inscriptos
            AFTER DELETE ON Inscripcion
            FOR EACH ROW
            BEGIN
                UPDATE Actividades
                SET inscriptos = inscriptos - 1
                WHERE id_actividad = OLD.id_actividad;
            END;

        """.trimIndent()
    }

    // Función genérica para ejecutar SELECT
    fun ejecutarConsultaSelect(query: String, args: Array<String>? = null): List<Map<String, Any>> {
        val db = readableDatabase
        val resultado = mutableListOf<Map<String, Any>>()
        val cursor = db.rawQuery(query, args)

        if (cursor.moveToFirst()) {
            do {
                val fila = mutableMapOf<String, Any>()
                for (col in cursor.columnNames) {
                    val idx = cursor.getColumnIndexOrThrow(col)
                    when (cursor.getType(idx)) {
                        Cursor.FIELD_TYPE_INTEGER -> fila[col] = cursor.getInt(idx)
                        Cursor.FIELD_TYPE_FLOAT -> fila[col] = cursor.getFloat(idx)
                        Cursor.FIELD_TYPE_STRING -> fila[col] = cursor.getString(idx)
                        Cursor.FIELD_TYPE_BLOB -> fila[col] = cursor.getBlob(idx)
                        Cursor.FIELD_TYPE_NULL -> fila[col] = ""
                    }
                }
                resultado.add(fila)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return resultado
    }

    // Ejecutar consultas DML como INSERT, UPDATE, DELETE
    fun ejecutarConsultaDML(query: String, args: Array<String>): Long {
        val db = writableDatabase
        val stmt = db.compileStatement(query)
        args.forEachIndexed { i, arg -> stmt.bindString(i + 1, arg) }
        val resultado = stmt.executeInsert()
        db.close()
        return resultado
    }

    fun ejecutarConsultaRaw(query: String, args: Array<String>? = null): Boolean {
        val db = writableDatabase
        val stmt = db.compileStatement(query)
        args?.forEachIndexed { i, arg -> stmt.bindString(i + 1, arg) }
        return try {
            stmt.executeUpdateDelete() > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
    }

    // Métodos CRUD específicos para Usuario
    fun insertarUsuario(usuario: Usuario): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", usuario.nombre)
            put("clave", usuario.clave)
            put("rol", usuario.rol)
        }
        val resultado = db.insert("Usuario", null, valores)
        db.close()
        return resultado != -1L
    }

    fun obtenerUsuarios(): List<Usuario> {
        val usuarios = mutableListOf<Usuario>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Usuario", null)

        if (cursor.moveToFirst()) {
            do {
                val usuario = Usuario(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("clave")),
                    cursor.getString(cursor.getColumnIndexOrThrow("rol"))
                )
                usuarios.add(usuario)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return usuarios
    }

    fun actualizarUsuario(usuario: Usuario): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", usuario.nombre)
            put("clave", usuario.clave)
            put("rol", usuario.rol)
        }
        val resultado = db.update("Usuario", valores, "id = ?", arrayOf(usuario.id.toString()))
        db.close()
        return resultado > 0
    }

    fun eliminarUsuario(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("Usuario", "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }
}

// Modelo simple de Usuario
data class Usuario(var id: Int, var nombre: String, var clave: Int, var rol: String = "admin")
