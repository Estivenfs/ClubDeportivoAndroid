package com.deportes.clubdeportivo.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor
import com.deportes.clubdeportivo.models.Actividad
import com.deportes.clubdeportivo.models.Cliente
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Nombre de la base de datos
private const val BD_NOMBRE = "ClubDeportivo"
private const val BD_VERSION = 4

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
        INSERT OR IGNORE INTO Usuario (nombre, clave, email, dni) VALUES ('administrador', 'admin', 'admin@admin.com', '12345678')
    """.trimIndent()
        db?.execSQL(insertAdmin)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Aquí podrías manejar actualizaciones de esquema si cambian las tablas en nuevas versiones
    }

    companion object {
        fun crearTablaEmpleado(): String = """
            CREATE TABLE Usuario (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            email TEXT NOT NULL UNIQUE,
            dni TEXT NOT NULL UNIQUE,
            nombre TEXT NOT NULL,
            clave TEXT NOT NULL,
            estado BOOLEAN DEFAULT 0 NOT NULL
);
        """.trimIndent()

        fun crearTablaCliente(): String = """
            CREATE TABLE Cliente (
            id_cliente INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            apellido TEXT NOT NULL,
            dni TEXT NOT NULL UNIQUE,
            email TEXT,
            telefono TEXT,
            fecha_nacimiento DATE,
            cond_socio BOOLEAN NOT NULL,  -- TRUE si es socio, FALSE si no
            apto_fisico BOOLEAN NOT NULL
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
            fecha_vencimiento DATE NOT NULL,
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


}


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
    // Función para INSERT
    fun insertar(query: String, args: Array<String>): Int {
        val db = writableDatabase
        var rowId: Int = -1
        try {
            val stmt = db.compileStatement(query)
            args.forEachIndexed { i, arg -> stmt.bindString(i + 1, arg) }
            rowId = stmt.executeInsert().toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
        return rowId
    }

    // Función para UPDATE o DELETE (devuelve el número de filas afectadas)
    fun actualizarOEliminar(query: String, args: Array<String>): Int {
        val db = writableDatabase
        var rowsAffected: Int = 0
        try {
            val stmt = db.compileStatement(query)
            args.forEachIndexed { i, arg -> stmt.bindString(i + 1, arg) }
            rowsAffected = stmt.executeUpdateDelete()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
        return rowsAffected
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

    fun obtenerActividades(): List<String> {
        val actividades = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT nombre_actividad FROM Actividades", null)

        if (cursor.moveToFirst()) {
            do {
                val actividad = cursor.getString(cursor.getColumnIndexOrThrow("nombre_actividad"))
                actividades.add(actividad)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return actividades
    }

    fun obtenerTodasLasActividades(): List<Actividad> {
        val lista = mutableListOf<Actividad>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id_actividad, nombre_actividad, precio_actividad, cupo_actividad, inscriptos FROM Actividades", null)

        if (cursor.moveToFirst()) {
            do {
                val id_actividad = cursor.getInt(cursor.getColumnIndexOrThrow("id_actividad"))
                val nombre_actividad = cursor.getString(cursor.getColumnIndexOrThrow("nombre_actividad"))
                val precio_actividad = cursor.getDouble(cursor.getColumnIndexOrThrow("precio_actividad"))
                val cupo_actividad = cursor.getInt(cursor.getColumnIndexOrThrow("cupo_actividad"))
                val inscriptos = cursor.getInt(cursor.getColumnIndexOrThrow("inscriptos"))


                lista.add(Actividad(
                    id_actividad,
                    nombre_actividad,
                    precio_actividad,
                    cupo_actividad,
                    inscriptos
                ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }



    fun obtenerSocios(): List<Cliente> {
        val lista = mutableListOf<Cliente>()
        val db = readableDatabase
        // Corrección: Usar la tabla 'Cliente' y la columna 'id_cliente'
        val cursor = db.rawQuery("SELECT id_cliente, nombre, apellido, dni FROM Cliente WHERE cond_socio = 1", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente")) // Obtener id_cliente
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
                val dni = cursor.getString(cursor.getColumnIndexOrThrow("dni"))



                lista.add(Cliente(
                    id,
                    nombre,
                    apellido,
                    dni,
                    email = null,
                    telefono = null,
                    fechaNacimiento = null,
                    condSocio = null,
                    aptoFisico = null
                ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    fun obtenerClientesConPagoMesAnterior(): List<Cliente> {
        val query = """
            SELECT Cliente.id_cliente, Cliente.nombre, Cliente.apellido, Cliente.dni
            FROM Cliente
            INNER JOIN Pagos ON Cliente.id_cliente = Pagos.id_cliente
            WHERE Pagos.fecha_vencimiento = ?
        """.trimIndent()

        val fechaHoy = Calendar.getInstance().toString()
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaFormateada = formatoFecha.format(Calendar.getInstance().time)

        val lista = mutableListOf<Cliente>()
        val db = readableDatabase
        // Corrección: Usar la tabla 'Cliente' y la columna 'id_cliente'
        val cursor = db.rawQuery(query, arrayOf(fechaFormateada))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente")) // Obtener id_cliente
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
                val dni = cursor.getString(cursor.getColumnIndexOrThrow("dni"))



                lista.add(Cliente(
                    id,
                    nombre,
                    apellido,
                    dni,
                    email = null,
                    telefono = null,
                    fechaNacimiento = null,
                    condSocio = null,
                    aptoFisico = null
                ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    fun obtenerTodosSocios(): List<Cliente> {
        val query = """
            SELECT Cliente.id_cliente, Cliente.nombre, Cliente.apellido, Cliente.dni
            FROM Cliente WHERE cond_socio = 1
        """.trimIndent()


        val lista = mutableListOf<Cliente>()
        val db = readableDatabase
        // Corrección: Usar la tabla 'Cliente' y la columna 'id_cliente'
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente")) // Obtener id_cliente
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
                val dni = cursor.getString(cursor.getColumnIndexOrThrow("dni"))



                lista.add(Cliente(
                    id,
                    nombre,
                    apellido,
                    dni,
                    email = null,
                    telefono = null,
                    fechaNacimiento = null,
                    condSocio = null,
                    aptoFisico = null
                ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    fun obtenerVencidos(): List<Cliente> {
        val query = """
        SELECT
            C.id_cliente,
            C.nombre,
            C.apellido,
            C.dni
        FROM
            Cliente AS C
        INNER JOIN (
            SELECT
                id_cliente,
                MAX(fecha_vencimiento) AS ultima_fecha_vencimiento
            FROM
                Pagos
            GROUP BY
                id_cliente
        ) AS P ON C.id_cliente = P.id_cliente
        WHERE
            P.ultima_fecha_vencimiento < ?
    """.trimIndent()

        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaFormateada = formatoFecha.format(Calendar.getInstance().time)

        val lista = mutableListOf<Cliente>()
        val db = readableDatabase
        // Corrección: Usar la tabla 'Cliente' y la columna 'id_cliente'
        val cursor = db.rawQuery(query, arrayOf(fechaFormateada))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente")) // Obtener id_cliente
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"))
                val dni = cursor.getString(cursor.getColumnIndexOrThrow("dni"))



                lista.add(Cliente(
                    id,
                    nombre,
                    apellido,
                    dni,
                    email = null,
                    telefono = null,
                    fechaNacimiento = null,
                    condSocio = null,
                    aptoFisico = null
                ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    fun obtenerSocioPorId(idSocio: Int): Cliente? {
        val query = """
            SELECT * FROM Cliente WHERE id_cliente = ?
        """.trimIndent()
        val resultado = this.ejecutarConsultaSelect(query, arrayOf(idSocio.toString()))
        return if (resultado.isNotEmpty()) {
            val clienteMap = resultado[0]
            Cliente(
                idCliente = clienteMap["id_cliente"] as Int,
                nombre = clienteMap["nombre"] as String,
                apellido = clienteMap["apellido"] as String,
                dni = clienteMap["dni"] as String,
                email = clienteMap["email"] as String,
                telefono = clienteMap["telefono"] as String,
                fechaNacimiento = clienteMap["fecha_nacimiento"] as String,
                condSocio = clienteMap["cond_socio"] == 1,
                aptoFisico = clienteMap["apto_fisico"] == 1,
                )
        } else {
            null
        }
    }

    fun obtenerComprobantePorId(idPago: Int): Map<String, String>? {
        val query = """
        SELECT 
            Cliente.nombre || ' ' || Cliente.apellido AS nombre_completo,
            Cliente.dni,
            CASE Cliente.cond_socio WHEN 1 THEN 'Socio' ELSE 'No Socio' END AS tipo_usuario,
            Pagos.fecha_pago,
            Pagos.monto,
            Pagos.medio_pago,
            Pagos.id_pago
        FROM Pagos
        JOIN Cliente ON Cliente.id_cliente = Pagos.id_cliente
        WHERE Pagos.id_pago = ?
    """.trimIndent()

        val resultado = this.ejecutarConsultaSelect(query, arrayOf(idPago.toString()))

        return if (resultado.isNotEmpty()) {
            // Convertimos Map<String, Any> → Map<String, String>
            resultado[0].mapValues { it.value.toString() }
        } else null
    }

    fun buscarClientePorDNI(dni: String): Map<String, Any>? {
        val db = readableDatabase
        var cursor: Cursor? = null

        return try {
            val query = """
            SELECT id_cliente, nombre, apellido, cond_socio, email, telefono 
            FROM Cliente 
            WHERE dni = ?
        """.trimIndent()

            cursor = db.rawQuery(query, arrayOf(dni))

            if (cursor.moveToFirst()) {
                val cliente = mutableMapOf<String, Any>()
                for (col in cursor.columnNames) {
                    when (cursor.getType(cursor.getColumnIndexOrThrow(col))) {
                        Cursor.FIELD_TYPE_INTEGER -> cliente[col] = cursor.getInt(cursor.getColumnIndexOrThrow(col))
                        Cursor.FIELD_TYPE_FLOAT -> cliente[col] = cursor.getFloat(cursor.getColumnIndexOrThrow(col))
                        Cursor.FIELD_TYPE_STRING -> cliente[col] = cursor.getString(cursor.getColumnIndexOrThrow(col))
                        Cursor.FIELD_TYPE_BLOB -> cliente[col] = cursor.getBlob(cursor.getColumnIndexOrThrow(col))
                        Cursor.FIELD_TYPE_NULL -> cliente[col] = ""
                    }
                }
                cliente
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            cursor?.close()
            db.close()
        }
    }


}



// Modelo simple de Usuario
data class Usuario(var id: Int, var nombre: String, var clave: Int, var rol: String = "admin")
