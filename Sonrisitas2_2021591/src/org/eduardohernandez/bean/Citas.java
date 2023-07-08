
package org.eduardohernandez.bean;

import java.sql.Time;
import java.util.Date;


public class Citas {
    private int CodigoCita;
    private Date fechaCita;
    private Time horaCita;
    private String tratamiento;
    private String descripCondActual;
    private int codigoPaciente;
    private int numeroColegiado;

    public Citas() {
    }

    public Citas(int CodigoCita, Date fechaCita, Time horaCita, String tratamiento, String descripCondActual, int codigoPaciente, int numeroColegiado) {
        this.CodigoCita = CodigoCita;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.tratamiento = tratamiento;
        this.descripCondActual = descripCondActual;
        this.codigoPaciente = codigoPaciente;
        this.numeroColegiado = numeroColegiado;
    }

    public int getCodigoCita() {
        return CodigoCita;
    }

    public void setCodigoCita(int CodigoCita) {
        this.CodigoCita = CodigoCita;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Time getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(Time horaCita) {
        this.horaCita = horaCita;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getDescripCondActual() {
        return descripCondActual;
    }

    public void setDescripCondActual(String descripCondActual) {
        this.descripCondActual = descripCondActual;
    }

    public int getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public int getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(int numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }
}
