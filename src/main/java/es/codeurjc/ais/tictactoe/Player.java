package es.codeurjc.ais.tictactoe;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Player {
	
	@Id
	private int id;
	private String label;
	private String name;
	private int partidasGanadas;
	private int partidasPerdidas;
	private int partidasEmpatadas;
	private int puntos = 50;
	private int puntosApostados;

	public Player(int id, String label, String name) {
		this.id = id;
		this.label = label;
		this.name = name;
	}

	public Player(int id, String label, String name, int puntos) {
		this.id = id;
		this.label = label;
		this.name = name;
		this.puntosApostados = puntos;
	}
	
	public Player(int id, String label, String name, int gan, int per, int emp) {
		this.id = id;
		this.label = label;
		this.name = name;
		this.partidasGanadas = gan;
		this.partidasPerdidas = per;
		this.partidasEmpatadas = emp;
	}

	public String getLabel() {
		return label;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPartidasGanadas() {
		return partidasGanadas;
	}

	public void setPartidasGanadas(int partidasGanadas) {
		this.partidasGanadas = partidasGanadas;
	}

	public int getPartidasPerdidas() {
		return partidasPerdidas;
	}

	public void setPartidasPerdidas(int partidasPerdidas) {
		this.partidasPerdidas = partidasPerdidas;
	}

	public int getPartidasEmpatadas() {
		return partidasEmpatadas;
	}

	public void setPartidasEmpatadas(int partidasEmpatadas) {
		this.partidasEmpatadas = partidasEmpatadas;
	}
	
	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}
	
	public int getPuntosApostados() {
		return puntosApostados;
	}

	public void setPuntosApostados(int puntosApostados) {
		this.puntosApostados = puntosApostados;
	}
}
