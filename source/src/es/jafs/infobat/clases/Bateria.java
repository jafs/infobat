package es.jafs.infobat.clases;

import android.os.BatteryManager;



/**
 * Clase que almacena información sobre la batería del sistema.
 * @author  José Antonio Fuentes Santiago
 * @version 1.1
 */
public final class Bateria {
	/** Valor de escala por defecto para el nivel de batería. */
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
	public final void setConectada(final int iConect) {
		if (iConect != iConectada) {
			bModificada = true;
			iConectada = iConect;
		}
	}


	/**
	 * Obtiene un valor que indica el estado de conexión de la batería.
	 * @return Valor entero que indica el estado de conexión de la batería.
	 */
	public final int getConectada() {
		return iConectada;
	}


	/**
	 * Establece el nivel de la batería, a partir de un valor y la escala.
	 * @param  iActual  Valor del nivel de batería.
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
	 * Obtiene el nivel de carga de la batería.
	 * @return Valor entero con el nivel de batería.
	 */
	public final int getNivel() {
		return iNivel;
	}


	/**
	 * Obtiene una cadena con el nivel de batería.
	 * @return Cadena con el nivel de batería y el símbolo de porcentaje.
	 */
	public final String getNivelString() {
		return iNivel + "%";
	}


	/**
	 * Obtiene el nivel de la batería, a partir de un valor y la escala.
	 * @param  iActual  Valor del nivel de batería.
	 * @param  iEscala  Escala en la que se basa.
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
	 * @return Valor real con la temperatura de la batería.
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
	 * @return Valor real con el voltaje de la batería.
	 */
	public final float getVoltaje() {
		return fVoltaje;
	}


	/**
	 * Obtiene una cadena con el valor de voltaje actual.
	 * @return Cadena con el voltaje de la batería.
	 */
	public final String getVoltajeString() {
		return fVoltaje + "V";
	}


	/**
	 * Establece el estado de salud de la batería.
	 * @param  iEstSalud  Estado de salud de la batería.
	 */
	public final void setSalud(final int iEstSalud) {
		if (iEstSalud != iSalud) {
			bModificada = true;
			iSalud = iEstSalud;
		}
	}


	/**
	 * Obtiene el estado de salud de la batería.
	 * @return Entero con el estado de salud de la batería.
	 */
	public final int getSalud() {
		return iSalud;
	}


	/**
	 * Establece el valor que indica si la batería está presente.
	 * @param  bPresent  Valor que indica si la batería está presente.
	 */
	public final void setPresente(final boolean bPresent) {
		if (bPresent != bPresente) {
			bModificada = true;
			bPresente = bPresent;
		}
	}


	/**
	 * Obtiene el valor que indica si la batería está presente.
	 * @return Valor booleano que indica si está presente la batería.
	 */
	public final boolean isPresente() {
		return bPresente;
	}


	/**
	 * Establece el valor de estado de la batería.
	 * @param iEstad  Valor de estado de la batería.
	 */
	public final void setEstado(final int iEstad) {
		if (iEstad != iEstado) {
			bModificada = true;
			iEstado = iEstad;
		}
	}


	/**
	 * Obtiene el valor de estado de la batería.
	 * @return ENtero con el estado de la batería.
	 */
	public final int getEstado() {
		return iEstado;
	}


	/**
	 * Establece la tecnología de la batería.
	 * @param sTecno  Tecnología de la batería.
	 */
	public final void setTecnologia(final String sTecno) {
		if (null != sTecno && !sTecno.equals(sTecnologia)) {
			bModificada = true;
			sTecnologia = sTecno;
		}
	}


	/**
	 * Obtiene el nombre de la tecnología de la batería.
	 * @return Cadena con la tecnología de la batería.
	 */
	public final String getTecnologia() {
		return sTecnologia;
	}


	/**
	 * Obtiene un valor que indica si alguno de los parámetros ha sido modificados.
	 * @return Valor booleano que indica si algún parámetro ha sido modificado.
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
