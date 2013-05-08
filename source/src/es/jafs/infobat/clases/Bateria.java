package es.jafs.infobat.clases;

import android.os.BatteryManager;



/**
 * Clase que almacena informaci�n sobre la bater�a del sistema.
 * @author  Jos� Antonio Fuentes Santiago
 * @version 1.1
 */
public final class Bateria {
	/** Valor de escala por defecto para el nivel de bater�a. */
	public static final int I_ESCALA = 100;
	/** Nivel de peligro. */
	public static final int NVL_DANGER = 15;
	/** Nivel de advertencia. */
	public static final int NVL_WARNING = 30;

	/** Valor divisor de voltaje. */
	private static final int DIV_VOLTAJE = 1000;
	/** Indica el valor de porcentaje. */
	private static final int PORCENTAJE = 100;
	/** Valor divisor de temperatura. */
	private static final int DIV_TEMPERATURA = 10;

	/** Estado de salud de la bater�a. */
	private int iSalud = BatteryManager.BATTERY_HEALTH_UNKNOWN;
	/** Nivel de carga de la bater�a. */
	private int iNivel = 0;
	/** Valor que indica si est� conectada. */
	private int iConectada = 0;
	/** Valor con la temperatura de la bater�a. */
	private float fTemperatura = 0;
	/** Valor con el voltaje de la bater�a. */
	private float fVoltaje = 0;
	/** Valor que indica si la bater�a est� presente en el dispositivo. */
	private boolean bPresente = false;
	/** Estado actual de la bater�a. */
	private int iEstado = BatteryManager.BATTERY_STATUS_UNKNOWN;
	/** Tecnolog�a utilizada por la bater�a. */
	private String sTecnologia = "";
	/** Indica si alg�n par�metro de la bater�a ha sido modificado. */
	private boolean bModificada = false;


	/**
	 * Establece un valor que indica el estado de conexi�n de la bater�a.
	 * @param iConect  Estado de conexi�n de la bater�a.
	 */
	public final void setConectada(final int iConect) {
		if (iConect != iConectada) {
			bModificada = true;
			iConectada = iConect;
		}
	}


	/**
	 * Obtiene un valor que indica el estado de conexi�n de la bater�a.
	 * @return Valor entero que indica el estado de conexi�n de la bater�a.
	 */
	public final int getConectada() {
		return iConectada;
	}


	/**
	 * Establece el nivel de la bater�a, a partir de un valor y la escala.
	 * @param  iActual  Valor del nivel de bater�a.
	 * @param  iEscala  Escala en la que se basa.
	 */
	public final void setNivel(final int iActual, final int iEscala) {
		final int iTemporal = getNivel(iActual, iEscala);

		if (iNivel != iTemporal) {
			bModificada = true;
			iNivel = iTemporal;
		}
	}


	/**
	 * Obtiene el nivel de carga de la bater�a.
	 * @return Valor entero con el nivel de bater�a.
	 */
	public final int getNivel() {
		return iNivel;
	}


	/**
	 * Obtiene una cadena con el nivel de bater�a.
	 * @return Cadena con el nivel de bater�a y el s�mbolo de porcentaje.
	 */
	public final String getNivelString() {
		return iNivel + "%";
	}


	/**
	 * Obtiene el nivel de la bater�a, a partir de un valor y la escala.
	 * @param  iActual  Valor del nivel de bater�a.
	 * @param  iEscala  Escala en la que se basa.
	 * @return Entero con el valor del nivel actual de la bater�a o -1.
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
	 * @param  iTemperatura  Valor de temperatura.
	 */
	public final void setTemperatura(final int iTemperatura) {
		float fTemporal = Float.valueOf(iTemperatura) / DIV_TEMPERATURA;

		if (fTemporal != fTemperatura) {
			bModificada = true;
			fTemperatura = fTemporal;
		}
	}


	/**
	 * Obtiene el valor de temperatura actual.
	 * @return Valor real con la temperatura de la bater�a.
	 */
	public final float getTemperatura() {
		return fTemperatura;
	}


	/**
	 * Establece el valor de voltaje actual.
	 * @param  iVoltaje  Valor de voltaje en mV.
	 */
	public final void setVoltaje(final int iVoltaje) {
		float fTemporal = Float.valueOf(iVoltaje) / DIV_VOLTAJE;

		if  (fTemporal != fVoltaje) {
			bModificada = true;
			fVoltaje = fTemporal;
		}
	}


	/**
	 * Obtiene el valor de voltaje actual.
	 * @return Valor real con el voltaje de la bater�a.
	 */
	public final float getVoltaje() {
		return fVoltaje;
	}


	/**
	 * Obtiene una cadena con el valor de voltaje actual.
	 * @return Cadena con el voltaje de la bater�a.
	 */
	public final String getVoltajeString() {
		return fVoltaje + "V";
	}


	/**
	 * Establece el estado de salud de la bater�a.
	 * @param  iEstSalud  Estado de salud de la bater�a.
	 */
	public final void setSalud(final int iEstSalud) {
		if (iEstSalud != iSalud) {
			bModificada = true;
			iSalud = iEstSalud;
		}
	}


	/**
	 * Obtiene el estado de salud de la bater�a.
	 * @return Entero con el estado de salud de la bater�a.
	 */
	public final int getSalud() {
		return iSalud;
	}


	/**
	 * Establece el valor que indica si la bater�a est� presente.
	 * @param  bPresent  Valor que indica si la bater�a est� presente.
	 */
	public final void setPresente(final boolean bPresent) {
		if (bPresent != bPresente) {
			bModificada = true;
			bPresente = bPresent;
		}
	}


	/**
	 * Obtiene el valor que indica si la bater�a est� presente.
	 * @return Valor booleano que indica si est� presente la bater�a.
	 */
	public final boolean isPresente() {
		return bPresente;
	}


	/**
	 * Establece el valor de estado de la bater�a.
	 * @param iEstad  Valor de estado de la bater�a.
	 */
	public final void setEstado(final int iEstad) {
		if (iEstad != iEstado) {
			bModificada = true;
			iEstado = iEstad;
		}
	}


	/**
	 * Obtiene el valor de estado de la bater�a.
	 * @return ENtero con el estado de la bater�a.
	 */
	public final int getEstado() {
		return iEstado;
	}


	/**
	 * Establece la tecnolog�a de la bater�a.
	 * @param sTecno  Tecnolog�a de la bater�a.
	 */
	public final void setTecnologia(final String sTecno) {
		if (null != sTecno && !sTecno.equals(sTecnologia)) {
			bModificada = true;
			sTecnologia = sTecno;
		}
	}


	/**
	 * Obtiene el nombre de la tecnolog�a de la bater�a.
	 * @return Cadena con la tecnolog�a de la bater�a.
	 */
	public final String getTecnologia() {
		return sTecnologia;
	}


	/**
	 * Obtiene un valor que indica si alguno de los par�metros ha sido modificados.
	 * @return Valor booleano que indica si alg�n par�metro ha sido modificado.
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
