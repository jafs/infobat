package es.jafs.infobat.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.widget.RemoteViews;
import es.jafs.infobat.R;


/**
 * Servicio encargado de actualizar el widget de batería.
 * @author  José Antonio Fuentes Santiago
 * @version 1.1.1
 */
public class ServicioBateria extends Service {
	/** Propiedad de imagen de fondo. */
	private static final String PROP_FONDO = "setBackgroundResource";

	/** Valor de escala por defecto para el nivel de batería. */
	public static final int I_ESCALA = 100;
	/** Nivel de peligro. */
	public static final int NVL_DANGER = 15;
	/** Nivel de advertencia. */
	public static final int NVL_WARNING = 30;
	/** Indica el valor de porcentaje. */
	private static final int PORCENTAJE = 100;

	/** Instancia de la clase. */
	private static ServicioBateria objInstancia;

	/** Receptor de estado de batería. */
	private ReceptorBateria mBatteryStateReceiver;
	/** Receptor de estado de pantalla. */
	private ReceptorPantalla mScreenStateReceiver;

	/** Indica si se está cargando. */
	private boolean bCargando = false;
	/** Nivel de batería. */
	private int iNivelBateria = -1;
	/** Indica cuando se debe realizar una inicialización. */
	private boolean bInicializar = true;
	/** Gestor de nombres de componentes. */
	private ComponentName objComponente = null;


	/**
	 * Devuelve la instancia de la clase.
	 * @return Instancia de la clase.
	 */
	public static ServicioBateria getInstancia() {
		return objInstancia;
	}


	/**
	 * Registra el receptor de batería.
	 */
	public void registrarBateria() {
		// Sólo en el caso de no estar previamente registrado, registra el receptor de batería.
		if (null == mBatteryStateReceiver) {
			mBatteryStateReceiver = new ReceptorBateria();

			final IntentFilter objFiltroIntent = new IntentFilter();
			objFiltroIntent.addAction(Intent.ACTION_BATTERY_CHANGED);
			objFiltroIntent.addAction(Intent.ACTION_POWER_CONNECTED);
			objFiltroIntent.addAction(Intent.ACTION_POWER_DISCONNECTED);

			registerReceiver(mBatteryStateReceiver, objFiltroIntent);
		}
	}


	/**
	 * Registra el receptor de pantalla.
	 */
	public void registrarPantalla() {
		// Sólo en el caso de no estar previamente registrado, registra el receptor de pantalla.
		if (mScreenStateReceiver == null) {
			mScreenStateReceiver = new ReceptorPantalla();

			final IntentFilter objFiltroIntent = new IntentFilter();
			objFiltroIntent.addAction(Intent.ACTION_SCREEN_ON);
			objFiltroIntent.addAction(Intent.ACTION_SCREEN_OFF);

			registerReceiver(mScreenStateReceiver, objFiltroIntent);
		}
	}


	/**
	 * Cancela el receptor de batería.
	 */
	public void cancelarBateria() {
		// Sólo en el caso de estar previamente registrado, cancela el receptor de batería.
		if (null != mBatteryStateReceiver) {
			unregisterReceiver(mBatteryStateReceiver);
			mBatteryStateReceiver = null;
		}
	}


	/**
	 * Cancela el receptor de pantalla.
	 */
	public void cancelarPantalla() {
		// Sólo en el caso de estar previamente registrado, cancela el receptor de pantalla.
		if (mScreenStateReceiver != null) {
			unregisterReceiver(mScreenStateReceiver);
			mScreenStateReceiver = null;
		}
	}


	/**
	 * Establece el valor de batería.
	 * @param  objIntent  Intent con los datos de batería.
	 */
	public void recibirBateria(Intent objIntent) {
		// Si no se han recibido datos se fuerza una actualización.
		if (objIntent == null) {
			objIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		}

		// Obtiene el nivel de batería.
		iNivelBateria = getLevel(objIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0),
								objIntent.getIntExtra(BatteryManager.EXTRA_SCALE, I_ESCALA));

		// Obtiene el estado de conexión de la batería.
		bCargando = (objIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) > 0 && iNivelBateria < 100);
	}


	/**
	 * Actualiza los widgets existentes.
	 * @param  objContexto  Contexto de la aplicación.
	 */
	public void actualizarWidgets(final Context objContexto) {
		final AppWidgetManager objGestor = AppWidgetManager.getInstance(objContexto);
		final RemoteViews objVistas = new RemoteViews(objContexto.getPackageName(), R.layout.widget);

		// Cambia el fondo del widget dependiendo del estado.
		if (bCargando) {
			objVistas.setInt(R.id.llFondo, PROP_FONDO, R.drawable.wdg_carga);
		} else if (iNivelBateria <= NVL_DANGER) {
			objVistas.setInt(R.id.llFondo, PROP_FONDO, R.drawable.wdg_danger);
		} else if (iNivelBateria <= NVL_WARNING) {
			objVistas.setInt(R.id.llFondo, PROP_FONDO, R.drawable.wdg_warning);
		} else {
			objVistas.setInt(R.id.llFondo, PROP_FONDO, R.drawable.wdg_normal);
		}

		objVistas.setTextViewText(R.id.tvNivelWidget, Integer.toString(iNivelBateria) + "%");
		objGestor.updateAppWidget(objComponente, objVistas);
	}



	/**
	 * Llamado al crear el servicio.
	 */
	@Override
	public void onCreate() {
		onStartCommand(null, -1, -1);
		bInicializar = false;
	}


	/**
	 * Llamado cuando se detienen todas las instancias del servicio.
	 */
	@Override
	public void onDestroy() {
		cancelarPantalla();
		cancelarBateria();

		bInicializar = true;
		objInstancia = null;

		super.onDestroy();
	}


	/**
	 * Llamado cada vez que se inicia un servicio.
	 * @param  objIntent  Intent con el que se lanzó el servicio.
	 * @param  iFlags     Flags de arranque del servicio.
	 * @param  iParam     Parámetros a utilizar por el servicio.
	 */
	@Override
	public int onStartCommand(final Intent objIntent, final int iFlags, int iParam) {
		if (bInicializar) {
			objInstancia = this;
			objComponente = new ComponentName(getApplicationContext(), WidgetBateria.class);

			registrarPantalla();
			registrarBateria();
			actualizarWidgets(getApplicationContext());
		}

		bInicializar = true;

		return START_STICKY;
	}


	/**
	 * Llamado al hacer un bind sobre el servicio. No se utiliza.
	 * @param  objIntent  Intent con el que se realizó el bind.
	 * @return Binder creado.
	 */
	@Override
	public IBinder onBind(final Intent objIntent) {
		return null;
	}


	/**
	 * Gets the battery level, from current value a current scale.
	 * @param  current  Current level of battery.
	 * @param  scale    Scale which calculate the battery level.
	 * @return Integer with current battery level or -1 in case of error.
	 */
	private static int getLevel(final int current, final int scale) {
		int level = -1;

		if (scale > 0) {
			level = current * PORCENTAJE / scale;
		} else {
			level = current;
		}

		return level;
	}
}
