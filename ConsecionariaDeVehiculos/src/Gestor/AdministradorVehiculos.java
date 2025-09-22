package Gestor;

import Enums.EstadoVehiculo;
import Enums.TipoVehiculos;
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

public class AdministradorVehiculos implements CRUD<Vehiculo>{
    private ArrayList<Vehiculo> vehiculos;
    private ArrayList <Vehiculo> vehiculosFiltrados;
    

    // Constructor
    public AdministradorVehiculos(){
        this.vehiculos = new ArrayList<>();
        this.vehiculosFiltrados = new ArrayList<>();
    }

    //----------------------------------- IMPLEMENTACION DE CRUD ------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void agregar(Vehiculo entidad) throws PatenteRepetidaException {
        if (this.vehiculos.contains(entidad)){
           throw new PatenteRepetidaException("Ya se encuentra un vehiculo con la misma patente!");
       }
       this.vehiculos.add(entidad);
    }
    
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

    //----------------------------------- ARCHIVOS ------------------------------------------------------------------------------------------------------------------------------------------------------
    
    // Guardar en CSV - NUEVO MÉTODO SIN VALIDACIÓN DE DUPLICADOS (usa lógica inteligente)
    public void guardarCSV() throws Exception {
        CsvUtilities.guardarVehiculosCSV(this.vehiculos);
    }

    // Cargar desde CSV - COMPLETO (limpia la lista)
    public void cargarCSV() throws Exception {
        ArrayList<String> lineas = CsvUtilities.leerCSV();
        ArrayList<Vehiculo> lista = new ArrayList<>();
        
        // Limpiar lista actual
        this.vehiculos.clear();
        
        for (String linea : lineas) {
            String[] partes = linea.split(",");
            if (partes.length < 1) continue;
            
            try {
                TipoVehiculos tipo = TipoVehiculos.valueOf(partes[0]);
                Vehiculo v = null;
                
                switch (tipo) {
                    case AUTO:
                        v = Auto.fromCSV(linea);
                        break;
                    case MOTO:
                        v = Moto.fromCSV(linea);
                        break;
                    case CAMIONETA:
                        v = Camioneta.fromCSV(linea);
                        break;
                    default:
                        continue;
                }
                
                if (v != null) {
                    lista.add(v);
                }
            } catch (Exception e) {
                throw new Exception("Error al procesar línea CSV: " + linea + " - " + e.getMessage(), e);
            }
        }
        
        this.vehiculos = lista;
    }

    // MÉTODO NUEVO: Cargar y mergear CSV (mantiene vehículos en memoria)
    public void cargarYMergeCSV() throws Exception {
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
        List<Map<String, String>> datos = JsonUtilities.cargarVehiculosJSON();
        
        // Limpiar lista actual
        this.vehiculos.clear();
        
        for (Map<String, String> map : datos) {
            TipoVehiculos tipo = TipoVehiculos.valueOf(map.get("tipo"));
            Vehiculo v;
            
            switch (tipo) {
                case AUTO:
                    v = new Auto(map);
                    break;
                case MOTO:
                    v = new Moto(map);
                    break;
                case CAMIONETA:
                    v = new Camioneta(map);
                    break;
                default:
                    continue;
            }
            this.vehiculos.add(v);
        }
    }

    // MÉTODO NUEVO: Cargar y mergear JSON (mantiene vehículos en memoria)
    public void cargarYMergeJSON() throws Exception {
        List<Map<String, String>> datos = JsonUtilities.cargarVehiculosJSON();
        
        for (Map<String, String> map : datos) {
            TipoVehiculos tipo = TipoVehiculos.valueOf(map.get("tipo"));
            Vehiculo vehiculoCargado;
            
            switch (tipo) {
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