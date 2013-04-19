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
	private SparseArray<String> mSalud = new SparseArray<String>(6);
	/** Mapa de cadenas de estado. */
	private SparseArray<String> mEstado = new SparseArray<String>(5);
	/** Mapa de cadenas de conexión. */
	private SparseArray<String> mConect = new SparseArray<String>(2);
	/** Indica que está desconectada. */
	private String sDesconectada = "Desconectada";


	/**
	 * Constructor privado para evitar instancias de la clase.
	 */
	private CadenasBateria() { }


	/**
	 * Carga las cadenas de la batería.
	 * @param contexto Contexto de la aplicación.
	 */
	public void cargar(Context contexto) {
		final String sIdioma = contexto.getResources().getConfiguration().locale.getLanguage();
		if (!TextUtils.isEmpty(sIdioma) && !sIdioma.equals(sActual)) {
			sActual = sIdioma;
			mSalud.put(BatteryManager.BATTERY_HEALTH_DEAD, contexto.getString(R.string.bat_sal_muerta));
			mSalud.put(BatteryManager.BATTERY_HEALTH_GOOD, contexto.getString(R.string.bat_sal_buena));
			mSalud.put(BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE, contexto.getString(R.string.bat_sal_voltaje));
			mSalud.put(BatteryManager.BATTERY_HEALTH_OVERHEAT, contexto.getString(R.string.bat_sal_caliente));
			mSalud.put(BatteryManager.BATTERY_HEALTH_UNKNOWN, contexto.getString(R.string.bat_sal_desconocida));
			mSalud.put(BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE, contexto.getString(R.string.bat_sal_fallo));

			mConect.put(BatteryManager.BATTERY_PLUGGED_AC, contexto.getString(R.string.bat_con_corrient));
			mConect.put(BatteryManager.BATTERY_PLUGGED_USB, contexto.getString(R.string.bat_con_usb));

			mEstado.put(BatteryManager.BATTERY_STATUS_CHARGING, contexto.getString(R.string.bat_est_cargando));
			mEstado.put(BatteryManager.BATTERY_STATUS_DISCHARGING, contexto.getString(R.string.bat_est_descargando));
			mEstado.put(BatteryManager.BATTERY_STATUS_FULL, contexto.getString(R.string.bat_est_llena));
			mEstado.put(BatteryManager.BATTERY_STATUS_NOT_CHARGING, contexto.getString(R.string.bat_est_no_cargando));
			mEstado.put(BatteryManager.BATTERY_STATUS_UNKNOWN, contexto.getString(R.string.bat_est_desconocido));

			sDesconectada = contexto.getString(R.string.bat_con_des);
		}
	}


	/**
	 * Obtiene la instancia de la clase.
	 * @return  CadBateria
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
	 * @return  String
	 */
	public String getEstado(final int iEstado) {
		String sCadena = mEstado.get(iEstado);
		if (null == sCadena) {
			sCadena = String.valueOf(iEstado);
		}
		return sCadena;
	}


	/**
	 * Obtiene la cadena de salud de la batería.
	 * @param   iSalud  Valor de salud de la batería.
	 * @return  String
	 */
	public String getSalud(final int iSalud) {
		String sCadena = mSalud.get(iSalud);
		if (null == sCadena) {
			sCadena = String.valueOf(iSalud);
		}
		return sCadena;
	}


	/**
	 * Obtiene la cadena de estado de conexión de la batería.
	 * @param  iEstado  Estado de conexión.
	 * @return String
	 */
	public String getConexion(final int iEstado) {
		String sCadena = mConect.get(iEstado);
		if (null == sCadena) {
			sCadena = sDesconectada;
		}
		return sCadena;
	}
}
