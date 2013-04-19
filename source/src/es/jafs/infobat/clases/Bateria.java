package es.jafs.infobat.clases;

import android.os.BatteryManager;


/**
 * Clase que almacena información sobre la batería del sistema.
 *
 * @author jafsantiago
 */
public final class Bateria {
	/** Valor de escala por defecto para el nivel de batería. */
	public static final int I_ESCALA = 100;
	/** Valor divisor de voltaje. */
	private static final int DIV_VOLTAJE = 1000;
	/** Indica el valor de porcentaje. */
	private static final int PORCENTAJE = 100;
	/** Valor divisor de temperatura. */
	private static final int DIV_TEMPERATURA = 10;
	
	public static final int NVL_DANGER = 10;
	public static final int NVL_WARNING = 30;

	/** Estado de salud de la batería. */
	private int iSalud = BatteryManager.BATTERY_HEALTH_UNKNOWN;
	/** Nivel de carga de la batería. */
	private int iNivel = 0;
	/** Valor que indica si está conectada. */
	private int iConectada = 0;
	/** Valor con la temperatura de la batería. */
	private float fTemperatura = 0;
	/** Valor con el voltaje de la batería. */
	private float fVoltaje = 0;
	/** Valor que indica si la batería está presente en el dispositivo. */
	private boolean bPresente = false;
	/** Estado actual de la batería. */
	private int iEstado = BatteryManager.BATTERY_STATUS_UNKNOWN;
	/** Tecnología utilizada por la batería. */
	private String sTecnologia = "";
	/** Indica si algún parámetro de la batería ha sido modificado. */
	private boolean bModificada = false;


	/**
	 * Establece un valor que indica el estado de conexión de la batería.
	 * @param iConect  Estado de conexión de la batería.
	 */
	public void setConectada(final int iConect) {
		if (iConect != iConectada) {
			bModificada = true;
			iConectada = iConect;
		}
	}


	/**
	 * Obtiene un valor que indica el estado de conexión de la batería.
	 * @return int
	 */
	public int getConectada() {
		return iConectada;
	}


	/**
	 * Establece el nivel de la batería, a partir de un valor y la escala.
	 * @param iActual  Valor del nivel de batería.
	 * @param iEscala  Escala en la que se basa.
	 */
	public void setNivel(final int iActual, final int iEscala) {
		int iTemporal = 0;

		if (iEscala > 0) {
			iTemporal = iActual * PORCENTAJE / iEscala;
		} else {
			iTemporal = iActual;
		}

		if (iNivel != iTemporal) {
			bModificada = true;
			iNivel = iTemporal;
		}
	}


	/**
	 * Obtiene el nivel de carga de la batería.
	 * @return int
	 */
	public int getNivel() {
		return iNivel;
	}


	/**
	 * Obtiene el nivel de la batería, a partir de un valor y la escala.
	 * @param iActual  Valor del nivel de batería.
	 * @param iEscala  Escala en la que se basa.
	 * @return Entero con el valor del nivel actual de la batería o -1.
	 */
	public static int getNivel(final int iActual, final int iEscala) {
		int iNivel = -1;

		if (iEscala > 0) {
			iNivel = iActual * PORCENTAJE / iEscala;
		} else {
			iNivel = iActual;
		}

		return iNivel;
	}


	/**
	 * Establece el valor de temperatura actual.
	 * @param iTemperatura  Valor de temperatura.
	 */
	public void setTemperatura(final int iTemperatura) {
		float fTemporal = Float.valueOf(iTemperatura) / DIV_TEMPERATURA;

		if (fTemporal != fTemperatura) {
			bModificada = true;
			fTemperatura = fTemporal;
		}
	}


	/**
	 * Obtiene el valor de temperatura actual.
	 * @return float
	 */
	public float getTemperatura() {
		return fTemperatura;
	}


	/**
	 * Establece el valor de voltaje actual.
	 * @param iVoltaje  Valor de voltaje en mV.
	 */
	public void setVoltaje(final int iVoltaje) {
		float fTemporal = Float.valueOf(iVoltaje) / DIV_VOLTAJE;

		if  (fTemporal != fVoltaje) {
			bModificada = true;
			fVoltaje = fTemporal;
		}
	}


	/**
	 * Obtiene el valor de voltaje actual.
	 * @return float
	 */
	public float getVoltaje() {
		return fVoltaje;
	}


	/**
	 * Establece el estado de salud de la batería.
	 * @param iEstSalud  Estado de salud de la batería.
	 */
	public void setSalud(final int iEstSalud) {
		if (iEstSalud != iSalud) {
			bModificada = true;
			iSalud = iEstSalud;
		}
	}


	/**
	 * Obtiene el estado de salud de la batería.
	 * @return int.
	 */
	public int getSalud() {
		return iSalud;
	}


	/**
	 * Establece el valor que indica si la batería está presente.
	 * @param bPresent  Valor que indica si la batería está presente.
	 */
	public void setPresente(final boolean bPresent) {
		if (bPresent != bPresente) {
			bModificada = true;
			bPresente = bPresent;
		}
	}


	/**
	 * Obtiene el valor que indica si la batería está presente.
	 * @return boolean
	 */
	public boolean isPresente() {
		return bPresente;
	}


	/**
	 * Establece el valor de estado de la batería.
	 * @param iEstad  Valor de estado de la batería.
	 */
	public void setEstado(final int iEstad) {
		if (iEstad != iEstado) {
			bModificada = true;
			iEstado = iEstad;
		}
	}


	/**
	 * Obtiene el valor de estado de la batería.
	 * @return int
	 */
	public int getEstado() {
		return iEstado;
	}


	/**
	 * Establece la tecnología de la batería.
	 * @param sTecno  Tecnología de la batería.
	 */
	public void setTecnologia(final String sTecno) {
		if (!sTecno.equals(sTecnologia)) {
			bModificada = true;
			sTecnologia = sTecno;
		}
	}


	/**
	 * Obtiene el nombre de la tecnología de la batería.
	 * @return String
	 */
	public String getTecnologia() {
		return sTecnologia;
	}


	/**
	 * Obtiene un valor que indica si alguno de los parámetros ha sido modificados.
	 * @return  boolean
	 */
	public boolean isModificada() {
		return bModificada;
	}


	/**
	 * Establece el objeto como no modificado.
	 */
	public void reiniciar() {
		bModificada = false;
	}
}
