package Gestor;

import Enums.EstadoVehiculo;
import Enums.TipoVehiculos;
import static Enums.TipoVehiculos.AUTO;
import static Enums.TipoVehiculos.CAMIONETA;
import static Enums.TipoVehiculos.MOTO;
import Exceptions.PatenteRepetidaException;
import Interfaces.CRUD;
import Models.Auto;
import Models.Camioneta;
import Models.Moto;
import Models.Vehiculo;
import Utilities.CsvUtilities;
import Utilities.JsonUtilities;
import Utilities.TxtUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Collections;
import java.util.Comparator;
import Exceptions.ErrorEnElFiltradoException;

public class AdministradorVehiculos implements CRUD<Vehiculo>, Iterable<Vehiculo>{
    private ArrayList<Vehiculo> vehiculos;
    private ArrayList <Vehiculo> vehiculosFiltrados;
    

    // Constructor
    public AdministradorVehiculos(){
        this.vehiculos = new ArrayList<>();
        this.vehiculosFiltrados = new ArrayList<>();
    }

    //----------------------------------- IMPLEMENTACION DE CRUD ------------------------------------------------------------------------------------------------------------------------------------------------------
    // AGREGAR CON VALIDACIÓN DE PATENTE (LANZA EXCEPCIÓN SI YA EXISTE)
    @Override
    public void agregar(Vehiculo entidad) throws PatenteRepetidaException {
        if (this.vehiculos.contains(entidad)){
           throw new PatenteRepetidaException("Ya se encuentra un vehiculo con la misma patente!");
       }
       this.vehiculos.add(entidad);
    }
    
    // MODIFICAR (BUSCA POR PATENTE Y ACTUALIZA TODOS LOS ATRIBUTOS)
    @Override
    public void modificar(Vehiculo vehiculoNuevo) {
        for (int i = 0; i < this.vehiculos.size(); i++) {
            Vehiculo actual = this.vehiculos.get(i);
            
            // Comparar por PATENTE, no por equals()
            if (actual.getPatente().equals(vehiculoNuevo.getPatente())) {

                // Evita que haya otro vehículo con la misma patente (excepto el que estamos modificando)
                for (int j = 0; j < this.vehiculos.size(); j++) {
                    if (j != i && this.vehiculos.get(j).getPatente().equals(vehiculoNuevo.getPatente())) {
                        throw new PatenteRepetidaException("Ya existe un vehiculo con esta patente!.");
                    }
                }

                // Actualiza TODOS los atributos del vehículo actual
                actual.setEstadoVehiculo(vehiculoNuevo.getEstadoVehiculo());
                actual.setFechaAlquiler(vehiculoNuevo.getFechaAlquiler());
                actual.setKilometros(vehiculoNuevo.getKilometros());
                actual.setAñoFabricacion(vehiculoNuevo.getAñoFabricacion());
                actual.setTipo(vehiculoNuevo.getTipo());
                actual.setTipoCombustible(vehiculoNuevo.getTipoCombustible()); 
                actual.setPatente(vehiculoNuevo.getPatente());

                // Actualizar atributos específicos del vehículo ACTUAL
                if (actual instanceof Auto && vehiculoNuevo instanceof Auto) {
                    ((Auto) actual).setNumPuertas(((Auto) vehiculoNuevo).getNumPuertas());
                    ((Auto) actual).setMarca(((Auto) vehiculoNuevo).getMarca());
                } else if (actual instanceof Moto && vehiculoNuevo instanceof Moto) {
                    ((Moto) actual).setCilindrada(((Moto) vehiculoNuevo).getCilindrada());
                    ((Moto) actual).setMarca(((Moto) vehiculoNuevo).getMarca());
                } else if (actual instanceof Camioneta && vehiculoNuevo instanceof Camioneta) {
                    ((Camioneta) actual).setCampacidadCargaKg(((Camioneta) vehiculoNuevo).getCampacidadCargaKg());
                    ((Camioneta) actual).setMarca(((Camioneta) vehiculoNuevo).getMarca());
                }

                return; // Salir después de modificar
            }
        }

        throw new IllegalArgumentException("No se encontró un vehiculo para modificar con patente: " + vehiculoNuevo.getPatente());
    }

    // ELIMINAR (BUSCA POR PATENTE Y ELIMINA)
    @Override
    public void eliminar(Vehiculo vehiculo) {
        // 1. Eliminar de la lista en memoria
        boolean existiaEnMemoria = this.vehiculos.remove(vehiculo);
        
        if (!existiaEnMemoria) {
            // Si no estaba en memoria, no hacer nada con los archivos
            return;
        }
        
        // Solo actualizar archivos si el vehículo podría haber estado guardado
        try {
            // Cargar la lista completa desde archivos
            ArrayList<Vehiculo> vehiculosEnArchivos = new ArrayList<>();
            
            // Cargar desde CSV para verificar si existia
            ArrayList<String> lineas = CsvUtilities.leerCSV();
            boolean existeEnArchivos = false;
            
            for (String linea : lineas) {
                String[] partes = linea.split(",");
                if (partes.length > 1 && partes[1].equals(vehiculo.getPatente())) {
                    existeEnArchivos = true;
                    break;
                }
            }
            
            // Solo actualizar archivos si el vehículo existia en ellos
            if (existeEnArchivos) {
                guardarCSV();
                guardarJSON();
                exportarListadoFiltradoTXT(this.vehiculos, "LISTADO COMPLETO DE VEHÍCULOS ACTUALIZADO");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar archivos después de eliminar: " + e.getMessage(), e);
        }
    }
    
    //FILTRADO POR TIPO Y ESTADO
    @Override
    public ArrayList<Vehiculo> buscarPorTipos(TipoVehiculos tipoVehiculo, EstadoVehiculo estado) {
        if (tipoVehiculo == null || estado == null) {
            throw new IllegalArgumentException("Tipo o estado de vehiculo invalido");
        }
        ArrayList<Vehiculo> filtrados = new ArrayList<>();
        for (Vehiculo vehiculoFor : vehiculos) {
            boolean coincideTipo = (tipoVehiculo == TipoVehiculos.TODOS || vehiculoFor.getTipo() == tipoVehiculo);
            boolean coincideEstado = (estado == EstadoVehiculo.TODOS || vehiculoFor.getEstadoVehiculo() == estado);

            if (coincideTipo && coincideEstado) {
                filtrados.add(vehiculoFor);
            }
        }
        return filtrados;
    }
    

    @Override
    public ArrayList<Vehiculo> listarTodo() {
        return this.vehiculos;
    }

    //----------------------------------- ITERADOR ------------------------------------------------------------------------------------------------------------------------------------------------------
        @Override
    public Iterator<Vehiculo> iterator() {
        return new VehiculoIterator();
    }
    
    // Clase interna para Iterator personalizado
    private class VehiculoIterator implements Iterator<Vehiculo> {
        private int currentIndex = 0;   // Índice actual en la lista
        
        @Override
        public boolean hasNext() {
            return currentIndex < vehiculos.size(); // Verifica si hay más elementos
        }
        
        @Override
        public Vehiculo next() {
            if (!hasNext()) {   // Verifica si hay un siguiente elemento
                throw new NoSuchElementException("No hay más vehículos en la colección");
            }
            return vehiculos.get(currentIndex++);
        }
        
        @Override
        public void remove() {
            if (currentIndex <= 0) {    // Verifica que next() haya sido llamado al menos una vez
                throw new IllegalStateException("No se puede eliminar antes de llamar next()");
            }
            vehiculos.remove(--currentIndex);   // Elimina el último elemento retornado por next()
        }
    }
    
    // Método que demuestra el uso del iterator
    public void mostrarTodosConIterator() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LISTADO CON ITERATOR PERSONALIZADO ===\n");
        sb.append("Patente\tTipo\tMarca\tAño\tEstado\tKm\n");
        
        for (Vehiculo vehiculo : this) { // Usa el iterator automáticamente
            String marca = "";
            if (vehiculo instanceof Auto) marca = ((Auto)vehiculo).getMarca().name();
            else if (vehiculo instanceof Moto) marca = ((Moto)vehiculo).getMarca().name();
            else if (vehiculo instanceof Camioneta) marca = ((Camioneta)vehiculo).getMarca().name();
            
            sb.append(String.format("%s\t%s\t%s\t%d\t%s\t%.2f\n",
                vehiculo.getPatente(),
                vehiculo.getTipo().name(),
                marca,
                vehiculo.getAñoFabricacion(),
                vehiculo.getEstadoVehiculo().name(),
                vehiculo.getKilometros()
            ));
        }
        
        TxtUtilities.guardarTexto(sb.toString());
    }
    
    // Método para probar que el iterator funciona
    public String probarIterator() {
        StringBuilder resultado = new StringBuilder();
        resultado.append("Probando Iterator Personalizado:\n");
        
        int contador = 0;
        for (Vehiculo v : this) {
            contador++;
            resultado.append(contador).append(". ").append(v.getPatente()).append(" - ").append(v.getTipo()).append("\n");
        }
        
        resultado.append("Total iterado: ").append(contador).append(" vehículos");
        return resultado.toString();
    }

    // ----------------------------------- COMPARABLE ------------------------------------------------------------------------------------------------------------------------------------------------------
    
    // Ordenar por criterio natural (patente) - usa Comparable
    public ArrayList<Vehiculo> ordenarPorCriterioNatural() {
        ArrayList<Vehiculo> ordenados = new ArrayList<>(this.vehiculos);
        Collections.sort(ordenados); // Usa el compareTo() de Vehiculo
        return ordenados;
    }

    // Ordenar por kilómetros - método manual
    public ArrayList<Vehiculo> ordenarPorKilometros() {
        ArrayList<Vehiculo> ordenados = new ArrayList<>(this.vehiculos);
        
        // Ordenamiento burbuja simple
        for (int i = 0; i < ordenados.size() - 1; i++) {
            for (int j = 0; j < ordenados.size() - 1 - i; j++) {
                if (ordenados.get(j).getKilometros() > ordenados.get(j + 1).getKilometros()) {
                    // Intercambiar posiciones
                    Vehiculo temp = ordenados.get(j);
                    ordenados.set(j, ordenados.get(j + 1));
                    ordenados.set(j + 1, temp);
                }
            }
        }
        return ordenados;
    }

    // Ordenar por año - método manual
    public ArrayList<Vehiculo> ordenarPorAño() {
        ArrayList<Vehiculo> ordenados = new ArrayList<>(this.vehiculos);
        
        // Ordenamiento burbuja simple
        for (int i = 0; i < ordenados.size() - 1; i++) {
            for (int j = 0; j < ordenados.size() - 1 - i; j++) {
                if (ordenados.get(j).getAñoFabricacion() > ordenados.get(j + 1).getAñoFabricacion()) {
                    // Intercambiar posiciones
                    Vehiculo temp = ordenados.get(j);
                    ordenados.set(j, ordenados.get(j + 1));
                    ordenados.set(j + 1, temp);
                }
            }
        }
        return ordenados;
    }

    // Ordenar por estado - método manual
    public ArrayList<Vehiculo> ordenarPorEstado() {
        ArrayList<Vehiculo> ordenados = new ArrayList<>(this.vehiculos);
        
        // Ordenamiento burbuja por nombre del estado
        for (int i = 0; i < ordenados.size() - 1; i++) {
            for (int j = 0; j < ordenados.size() - 1 - i; j++) {
                String estado1 = ordenados.get(j).getEstadoVehiculo().name();
                String estado2 = ordenados.get(j + 1).getEstadoVehiculo().name();
                
                if (estado1.compareTo(estado2) > 0) {
                    // Intercambiar posiciones
                    Vehiculo temp = ordenados.get(j);
                    ordenados.set(j, ordenados.get(j + 1));
                    ordenados.set(j + 1, temp);
                }
            }
        }
        return ordenados;
    }

    // Ordenar por tipo - método manual
    public ArrayList<Vehiculo> ordenarPorTipo() {
        ArrayList<Vehiculo> ordenados = new ArrayList<>(this.vehiculos);
        
        // Ordenamiento burbuja por nombre del tipo
        for (int i = 0; i < ordenados.size() - 1; i++) {
            for (int j = 0; j < ordenados.size() - 1 - i; j++) {
                String tipo1 = ordenados.get(j).getTipo().name();
                String tipo2 = ordenados.get(j + 1).getTipo().name();
                
                if (tipo1.compareTo(tipo2) > 0) {
                    // Intercambiar posiciones
                    Vehiculo temp = ordenados.get(j);
                    ordenados.set(j, ordenados.get(j + 1));
                    ordenados.set(j + 1, temp);
                }
            }
        }
        return ordenados;
    }

    // --------------------------------- WILDCARDS----------------------------------------------------------------------------------------------------------------------------------------------------------
    // WILDCARD CON LÍMITE SUPERIOR (? extends)
    // Acepta cualquier lista de vehículos o sus subclases (Auto, Moto, Camioneta)
    public void agregarDesdeColeccion(List<? extends Vehiculo> nuevosVehiculos) throws ErrorEnElFiltradoException {
        if (nuevosVehiculos == null || nuevosVehiculos.isEmpty()) {
            throw new ErrorEnElFiltradoException("La lista de vehículos no puede estar vacía o ser null");
        }
        
        StringBuilder errores = new StringBuilder();
        int vehiculosAgregados = 0;
        int vehiculosConError = 0;
        
        for (Vehiculo vehiculo : nuevosVehiculos) {
            try {
                agregarOActualizar(vehiculo);
                vehiculosAgregados++;
            } catch (Exception e) {
                vehiculosConError++;
                errores.append("Error con vehículo ").append(vehiculo.getPatente()).append(": ").append(e.getMessage()).append("\n");
            }
        }
        
        if (vehiculosConError > 0) {
            throw new ErrorEnElFiltradoException(String.format("Se procesaron %d vehículos correctamente, pero %d tuvieron errores:\n%s", vehiculosAgregados, vehiculosConError, errores.toString()));
        }
    }

    // WILDCARD CON LÍMITE INFERIOR (? super)
    // Acepta cualquier lista que pueda contener Vehiculos (Object, Vehiculo, etc.)
    public void copiarVehiculosA(List<? super Vehiculo> destino){
        if (destino == null) {
            throw new ErrorEnElFiltradoException("La lista de destino no puede ser null");
        }
        
        if (this.vehiculos.isEmpty()) {
            throw new ErrorEnElFiltradoException("No hay vehículos para copiar");
        }
        
        try {
            for (Vehiculo vehiculo : this.vehiculos) {
                destino.add(vehiculo);
            }
        } catch (Exception e) {
            throw new ErrorEnElFiltradoException("Error al copiar vehículos: " + e.getMessage());
        }
    }

    // MÉTODO GENÉRICO CON WILDCARD EXTENDS
    // Filtra vehículos por tipo específico (Auto, Moto, Camioneta)
    public <T extends Vehiculo> ArrayList<T> filtrarPorTipo(Class<T> tipoClase) throws ErrorEnElFiltradoException {
        if (tipoClase == null) {
            throw new ErrorEnElFiltradoException("El tipo de clase no puede ser null");
        }
        
        ArrayList<T> resultado = new ArrayList<>();
        
        try {
            for (Vehiculo vehiculo : this.vehiculos) {
                if (tipoClase.isInstance(vehiculo)) {
                    resultado.add(tipoClase.cast(vehiculo));
                }
            }
        } catch (Exception e) {
            throw new ErrorEnElFiltradoException("Error al filtrar por tipo " + tipoClase.getSimpleName() + ": " + e.getMessage());
        }
        
        return resultado;
    }

    // MÉTODO CON WILDCARD PARA COMPARACIÓN
    // Busca vehículos que coincidan con una lista de patentes
    public ArrayList<Vehiculo> buscarPorPatentes(List<? extends String> patentes) throws ErrorEnElFiltradoException {
        if (patentes == null || patentes.isEmpty()) {
            throw new ErrorEnElFiltradoException("La lista de patentes no puede estar vacía o ser null");
        }
        
        ArrayList<Vehiculo> encontrados = new ArrayList<>();
        StringBuilder patentesNoEncontradas = new StringBuilder();
        
        for (String patente : patentes) {
            if (patente == null || patente.trim().isEmpty()) {
                continue; // Saltar patentes nulas o vacías
            }
            
            Vehiculo vehiculo = buscarPorPatente(patente);
            if (vehiculo != null) {
                encontrados.add(vehiculo);
            } else {
                patentesNoEncontradas.append(patente).append(", ");
            }
        }
        
        // Si no se encontraron algunas patentes, lanzar excepción
        if (patentesNoEncontradas.length() > 0) {
            String patentesNoEnc = patentesNoEncontradas.toString();
            patentesNoEnc = patentesNoEnc.substring(0, patentesNoEnc.length() - 2); // Quitar última coma
            throw new ErrorEnElFiltradoException("No se encontraron vehículos con las patentes: " + patentesNoEnc);
        }
        
        return encontrados;
    }
    
    // MÉTODOS CON WILDCARD SUPER PARA CADA TIPO DE VEHÍCULO
    // Copia todos los autos a una lista que acepta autos o superclases
    public void copiarAutosSolamente(List<? super Auto> destino) throws ErrorEnElFiltradoException {
        if (destino == null) {
            throw new ErrorEnElFiltradoException("La lista de destino no puede ser null");
        }
        
        int autosEncontrados = 0;
        
        try {
            for (Vehiculo vehiculo : this.vehiculos) {
                if (vehiculo instanceof Auto) {
                    destino.add((Auto) vehiculo);
                    autosEncontrados++;
                }
            }
        } catch (Exception e) {
            throw new ErrorEnElFiltradoException("Error al filtrar y copiar autos: " + e.getMessage());
        }
        
        if (autosEncontrados == 0) {
            throw new ErrorEnElFiltradoException("No se encontraron autos para copiar");
        }
    }

    // Copia todas las motos a una lista que acepta motos o superclases
    public void copiarMotosSolamente(List<? super Moto> destino) throws ErrorEnElFiltradoException {
        if (destino == null) {
            throw new ErrorEnElFiltradoException("La lista de destino no puede ser null");
        }
        
        int motosEncontradas = 0;
        
        try {
            for (Vehiculo vehiculo : this.vehiculos) {
                if (vehiculo instanceof Moto) {
                    destino.add((Moto) vehiculo);
                    motosEncontradas++;
                }
            }
        } catch (Exception e) {
            throw new ErrorEnElFiltradoException("Error al filtrar y copiar motos: " + e.getMessage());
        }
        
        if (motosEncontradas == 0) {
            throw new ErrorEnElFiltradoException("No se encontraron motos para copiar");
        }
    }

    // Copia todas las camionetas a una lista que acepta camionetas o superclases
    public void copiarCamionetasSolamente(List<? super Camioneta> destino) throws ErrorEnElFiltradoException {
        if (destino == null) {
            throw new ErrorEnElFiltradoException("La lista de destino no puede ser null");
        }
        
        int camionetasEncontradas = 0;
        
        try {
            for (Vehiculo vehiculo : this.vehiculos) {
                if (vehiculo instanceof Camioneta) {
                    destino.add((Camioneta) vehiculo);
                    camionetasEncontradas++;
                }
            }
        } catch (Exception e) {
            throw new ErrorEnElFiltradoException("Error al filtrar y copiar camionetas: " + e.getMessage());
        }
        
        if (camionetasEncontradas == 0) {
            throw new ErrorEnElFiltradoException("No se encontraron camionetas para copiar");
        }
    }

    // MÉTODO GENÉRICO PARA COPIAR CUALQUIER TIPO (MÁS ELEGANTE)
    public <T extends Vehiculo> void copiarVehiculosPorTipo(Class<T> tipoClase, List<? super T> destino) throws ErrorEnElFiltradoException {
        if (tipoClase == null) {
            throw new ErrorEnElFiltradoException("El tipo de clase no puede ser null");
        }
        
        if (destino == null) {
            throw new ErrorEnElFiltradoException("La lista de destino no puede ser null");
        }
        
        int vehiculosEncontrados = 0;
        
        try {
            for (Vehiculo vehiculo : this.vehiculos) {
                if (tipoClase.isInstance(vehiculo)) {
                    destino.add(tipoClase.cast(vehiculo));
                    vehiculosEncontrados++;
                }
            }
        } catch (Exception e) {
            throw new ErrorEnElFiltradoException("Error al filtrar y copiar vehículos de tipo " + tipoClase.getSimpleName() + ": " + e.getMessage());
        }
        
        if (vehiculosEncontrados == 0) {
            throw new ErrorEnElFiltradoException("No se encontraron vehículos de tipo " + tipoClase.getSimpleName() + " para copiar");
        }
    }

    // Filtrar por múltiples criterios con wildcards
    public ArrayList<Vehiculo> filtrarConMultiplesCriterios(
            List<? extends String> patentesPermitidas,
            List<? extends EstadoVehiculo> estadosPermitidos) throws ErrorEnElFiltradoException {
        
        if (patentesPermitidas == null || estadosPermitidos == null) {
            throw new ErrorEnElFiltradoException("Las listas de criterios no pueden ser null");
        }
        
        ArrayList<Vehiculo> resultado = new ArrayList<>();
        
        try {
            for (Vehiculo vehiculo : this.vehiculos) {
                boolean patentePermitida = patentesPermitidas.contains(vehiculo.getPatente());
                boolean estadoPermitido = estadosPermitidos.contains(vehiculo.getEstadoVehiculo());
                
                if (patentePermitida && estadoPermitido) {
                    resultado.add(vehiculo);
                }
            }
        } catch (Exception e) {
            throw new ErrorEnElFiltradoException("Error al aplicar múltiples criterios de filtrado: " + e.getMessage());
        }
        
        return resultado;
    }
    //----------------------------------- ARCHIVOS ------------------------------------------------------------------------------------------------------------------------------------------------------
    
    // Guardar en CSV - NUEVO MÉTODO SIN VALIDACIÓN DE DUPLICADOS (usa lógica inteligente)
    public void guardarCSV() throws Exception {
        CsvUtilities.guardarVehiculosCSV(this.vehiculos);
    }

    // Cargar desde CSV - COMPLETO (limpia la lista)
    public void cargarCSV() throws Exception {
        ArrayList<String> lineas = CsvUtilities.leerCSV();
        
        for (String linea : lineas) {
            String[] partes = linea.split(",");
            if (partes.length < 1) continue;
            
            try {
                TipoVehiculos tipo = TipoVehiculos.valueOf(partes[0]);
                Vehiculo vehiculoCargado = null;
                
                switch (tipo) {
                    case AUTO:
                        vehiculoCargado = Auto.fromCSV(linea);
                        break;
                    case MOTO:
                        vehiculoCargado = Moto.fromCSV(linea);
                        break;
                    case CAMIONETA:
                        vehiculoCargado = Camioneta.fromCSV(linea);
                        break;
                    default:
                        continue;
                }
                
                if (vehiculoCargado != null) {
                    // Usar método que permite actualizar
                    agregarOActualizar(vehiculoCargado);
                }
            } catch (Exception e) {
                throw new Exception("Error al procesar línea CSV para merge: " + linea + " - " + e.getMessage(), e);
            }
        }
    }

    // Guardar en JSON - SIN validación de duplicados
    public void guardarJSON() throws Exception {
        JsonUtilities.guardarVehiculosJSON(this.vehiculos);
    }

    // Cargar desde JSON - COMPLETO (limpia la lista)
    public void cargarJSON() throws Exception {
        List<Map<String, String>> datos = JsonUtilities.cargarVehiculosJSON();  // Lista de mapas
        
        for (Map<String, String> map : datos) {     // Iterar sobre cada mapa
            TipoVehiculos tipo = TipoVehiculos.valueOf(map.get("tipo"));
            Vehiculo vehiculoCargado;
            
            switch (tipo) {     // Crear instancia según tipo
                case AUTO:
                    vehiculoCargado = new Auto(map);
                    break;
                case MOTO:
                    vehiculoCargado = new Moto(map);
                    break;
                case CAMIONETA:
                    vehiculoCargado = new Camioneta(map);
                    break;
                default:
                    continue;
            }
            
            // Usar método que permite actualizar
            agregarOActualizar(vehiculoCargado);
        }
    }

    // Exportar listado filtrado a TXT
    public void exportarListadoFiltradoTXT(ArrayList<Vehiculo> listaFiltrada, String encabezado) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(encabezado).append("\n");
        sb.append("Patente\tTipo\tMarca\tAño\tEstado\tKm\n");
        for (Vehiculo v : listaFiltrada) {
            String marca = "";
            if (v instanceof Auto) marca = ((Auto)v).getMarca().name();
            else if (v instanceof Moto) marca = ((Moto)v).getMarca().name();
            else if (v instanceof Camioneta) marca = ((Camioneta)v).getMarca().name();
            sb.append(String.format("%s\t%s\t%s\t%d\t%s\t%.2f\n",
                v.getPatente(),
                v.getTipo().name(),
                marca,
                v.getAñoFabricacion(),
                v.getEstadoVehiculo().name(),
                v.getKilometros()
            ));
        }
        TxtUtilities.guardarTexto(sb.toString());
    }

    // MÉTODO NUEVO: Agregar o actualizar (sin excepciones de duplicados)
    public boolean agregarOActualizar(Vehiculo vehiculo) {
        // Buscar si ya existe un vehículo con la misma patente
        for (int i = 0; i < this.vehiculos.size(); i++) {
            if (this.vehiculos.get(i).getPatente().equals(vehiculo.getPatente())) {
                // Si existe, actualizar (reemplazar)
                this.vehiculos.set(i, vehiculo);
                return true;
            }
        }
        
        // Si no existe, agregar nuevo
        this.vehiculos.add(vehiculo);
        return true;
    }

    // MÉTODO AUXILIAR: Buscar por patente
    public Vehiculo buscarPorPatente(String patente) {
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getPatente().equals(patente)) {
                return vehiculo;
            }
        }
        return null;
    }
}