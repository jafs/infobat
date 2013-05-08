package es.jafs.infobat.utiles;



/**
 * Clase que contiene métodos con diversas utilidades.
 * @author  José Antonio Fuentes Santiago
 * @version 1.0
 * @date    2013
 */
public final class Utiles {
	/** Valor multiplicador para la conversión. */
	private static final int I_MULTI = 9;
	/** Valor divisor para la conversión. */
	private static final int I_DIV = 5;
	/** Valor sumatorio para la conversión. */
	private static final int I_SUM = 32;


	/**
	 * Constructor de la clase privado para evitar instancias.
	 */
	private Utiles() {
	}


	/**
	 * Convierte grados Celsius en grados Farhenheit.
	 * @param   fCelsius  Valor con grados Celsius.
	 * @return  float
	 */
	public static float celsiusToFarhenheit(final float fCelsius) {
		return (fCelsius * I_MULTI / I_DIV) + I_SUM;
	}
}
