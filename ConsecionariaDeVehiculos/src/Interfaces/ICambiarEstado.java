package Interfaces;

import Models.Vehiculo;
import java.time.LocalDate;

public interface ICambiarEstado{
    void realizarMatenimiento();
    void alquilarVehiculo();
    void disponerVehiculo();
}
