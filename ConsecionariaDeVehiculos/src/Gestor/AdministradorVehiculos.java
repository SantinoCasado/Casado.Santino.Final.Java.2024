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
    public void modificar(Vehiculo vehiculo) {
        for (int i = 0; i < this.vehiculos.size(); i++) {
            Vehiculo actual = this.vehiculos.get(i);

            System.out.println("Comparando " + actual.getPatente() + " con " + vehiculo.getPatente());
            // Si encontramos el producto a modificar
            if (actual.equals(vehiculo)) {

                // Evita que haya otro producto igual (excepto el mismo que vamos a modificar)
                for (int j = 0; j < this.vehiculos.size(); j++) {
                    if (j != i && this.vehiculos.get(j).equals(vehiculo)) {
                        throw new PatenteRepetidaException("Ya existe un vehiculo con esta patente!.");
                    }
                }

                // Actualiza solo los atributos editables
                actual.setEstadoVehiculo(vehiculo.getEstadoVehiculo());
                actual.setFechaAlquiler(vehiculo.getFechaAlquiler());
                actual.setKilometros(vehiculo.getKilometros());
                actual.setAñoFabricacion(vehiculo.getAñoFabricacion());
                actual.setTipo(vehiculo.getTipo());


                if(vehiculo instanceof Auto auto){
                    auto.setNumPuertas(((Auto) vehiculo).getNumPuertas());
                    auto.setMarca(((Auto) vehiculo).getMarca());

                }else  if(vehiculo instanceof Moto moto){
                    moto.setCilindrada(((Moto) vehiculo).getCilindrada());
                    moto.setMarca(((Moto) vehiculo).getMarca());

                }else if(vehiculo instanceof Camioneta camioneta){
                    camioneta.setCampacidadCargaKg(((Camioneta) vehiculo).getCampacidadCargaKg());
                    camioneta.setMarca(((Camioneta) vehiculo).getMarca());
                }

                return;
            }
        }

        throw new IllegalArgumentException("No se encontró un vehiculo para modificar.");
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
            System.err.println("Error al actualizar archivos después de eliminar: " + e.getMessage());
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
    // Guardar en CSV
    public void guardarCSV() throws Exception {
        // 1. Leer vehículos existentes en el archivo
        ArrayList<String> lineasExistentes = CsvUtilities.leerCSV();
        ArrayList<String> patentesEnArchivo = new ArrayList<>();
        
        // 2. Extraer las patentes ya guardadas
        for (String linea : lineasExistentes) {
            String[] partes = linea.split(",");
            if (partes.length > 1) {
                patentesEnArchivo.add(partes[1]); // La patente está en la posición 1
            }
        }
        
        // 3. Verificar duplicados antes de guardar
        for (Vehiculo vehiculo : vehiculos) {
            if (patentesEnArchivo.contains(vehiculo.getPatente())) {
                throw new PatenteRepetidaException("Ya existe un vehículo con la patente " + 
                                                vehiculo.getPatente() + " en el archivo CSV. " +
                                                "Elimine el duplicado antes de guardar.");
            }
        }
        
        // 4. Si no hay duplicados, proceder con el guardado
        CsvUtilities.guardarVehiculosCSV(vehiculos);
    }

    // Cargar desde CSV
    public void cargarCSV() throws Exception {
        ArrayList<String> lineas = CsvUtilities.leerCSV();
        ArrayList<Vehiculo> lista = new ArrayList<>();
        for (String linea : lineas) {
            String[] partes = linea.split(",");
            if (partes.length < 1) continue;
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
            if (v != null) lista.add(v);
        }
        this.vehiculos = lista;
    }

    // Guardar en JSON
    public void guardarJSON() throws Exception {
        // 1. Cargar vehículos existentes del archivo JSON
        List<Map<String, String>> datosExistentes = JsonUtilities.cargarVehiculosJSON();
        ArrayList<String> patentesEnArchivo = new ArrayList<>();
        
        // 2. Extraer patentes ya guardadas
        for (Map<String, String> map : datosExistentes) {
            if (map.containsKey("patente")) {
                patentesEnArchivo.add(map.get("patente"));
            }
        }
        
        // 3. Verificar duplicados antes de guardar
        for (Vehiculo vehiculo : vehiculos) {
            if (patentesEnArchivo.contains(vehiculo.getPatente())) {
                throw new PatenteRepetidaException("Ya existe un vehículo con la patente " + 
                                                vehiculo.getPatente() + " en el archivo JSON. " +
                                                "Elimine el duplicado antes de guardar.");
            }
        }
        
        // 4. Si no hay duplicados, proceder con el guardado
        JsonUtilities.guardarVehiculosJSON(vehiculos);
    }

    // Cargar desde JSON
    public void cargarJSON() {
      List<Map<String, String>> datos = JsonUtilities.cargarVehiculosJSON();
      ArrayList<Vehiculo> lista = new ArrayList<>();
      
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
          lista.add(v);
      }
      this.vehiculos = lista;
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
}
