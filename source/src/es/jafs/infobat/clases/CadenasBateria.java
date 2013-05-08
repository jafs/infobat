package es.jafs.infobat.clases;

import android.content.Context;
import android.os.BatteryManager;
import android.text.TextUtils;
import android.util.SparseArray;
import es.jafs.infobat.R;



/**
 * Gestor de cadenas de información sobre la batería.
 * @author  José Antonio Fuentes Santiago
 * @version 1.0
 * @date    2013
 */
public final class CadenasBateria {
	/** Idioma actual. */
	private static String sActual = null;
	/** Instancia de la clase. */
	private static CadenasBateria objInstancia = null;

	/** Mapa de cadenas de salud. */
	private final SparseArray<String> mSalud = new SparseArray<String>(6);
	/** Mapa de cadenas de estado. */
	private final SparseArray<String> mEstado = new SparseArray<String>(5);
	/** Mapa de cadenas de conexión. */
	private final SparseArray<String> mConect = new SparseArray<String>(2);

	/** Indica que está desconectada. */
	private String sDesconectada;



	/**
	 * Constructor privado para evitar instancias de la clase.
	 */
	private CadenasBateria() {
	}


	/**
	 * Carga las cadenas de la batería.
	 * @param objContexto Contexto de la aplicación.
	 */
	public void cargar(final Context objContexto) {
		final String sIdioma = objContexto.getResources().getConfiguration().locale.getLanguage();

		if (!TextUtils.isEmpty(sIdioma) && !sIdioma.equals(sActual)) {
			sActual = sIdioma;

			sDesconectada = objContexto.getString(R.string.bat_con_des);
			mSalud.put(BatteryManager.BATTERY_HEALTH_DEAD, objContexto.getString(R.string.bat_sal_muerta));
			mSalud.put(BatteryManager.BATTERY_HEALTH_GOOD, objContexto.getString(R.string.bat_sal_buena));
			mSalud.put(BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE, objContexto.getString(R.string.bat_sal_voltaje));
			mSalud.put(BatteryManager.BATTERY_HEALTH_OVERHEAT, objContexto.getString(R.string.bat_sal_caliente));
			mSalud.put(BatteryManager.BATTERY_HEALTH_UNKNOWN, objContexto.getString(R.string.bat_sal_desconocida));
			mSalud.put(BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE, objContexto.getString(R.string.bat_sal_fallo));

			mConect.put(BatteryManager.BATTERY_PLUGGED_AC, objContexto.getString(R.string.bat_con_corrient));
			mConect.put(BatteryManager.BATTERY_PLUGGED_USB, objContexto.getString(R.string.bat_con_usb));

			mEstado.put(BatteryManager.BATTERY_STATUS_CHARGING, objContexto.getString(R.string.bat_est_cargando));
			mEstado.put(BatteryManager.BATTERY_STATUS_DISCHARGING, objContexto.getString(R.string.bat_est_descargando));
			mEstado.put(BatteryManager.BATTERY_STATUS_FULL, objContexto.getString(R.string.bat_est_llena));
			mEstado.put(BatteryManager.BATTERY_STATUS_NOT_CHARGING, objContexto.getString(R.string.bat_est_no_cargando));
			mEstado.put(BatteryManager.BATTERY_STATUS_UNKNOWN, objContexto.getString(R.string.bat_est_desconocido));
		}
	}


	/**
	 * Obtiene la instancia de la clase.
	 * @return Instancia de la clase.
	 */
	public static CadenasBateria getInstancia() {
		if (null == objInstancia) {
			objInstancia = new CadenasBateria();
		}

		return objInstancia;
	}


	/**
	 * Obtiene el estado de la batería.
	 * @param   iEstado  Estado de la batería.
	 * @return  Cadena con el valor de estado.
	 */
	public String getEstado(final int iEstado) {
		return mEstado.get(iEstado);
	}


	/**
	 * Obtiene la cadena de salud de la batería.
	 * @param   iSalud  Valor de salud de la batería.
	 * @return  Cadena con el valor de salud.
	 */
	public String getSalud(final int iSalud) {
		return mSalud.get(iSalud);
	}


	/**
	 * Obtiene la cadena de estado de conexión de la batería.
	 * @param  iEstado  Estado de conexión.
	 * @return Cadena con el estado de conexión.
	 */
	public String getConexion(final int iEstado) {
		String sCadena = mConect.get(iEstado);

		if (null == sCadena) {
			sCadena = sDesconectada;
		}
		return sCadena;
	}
}
