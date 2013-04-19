package es.jafs.infobat.widget;

import android.app.PendingIntent;
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
import es.jafs.infobat.activities.InfoActivity;
import es.jafs.infobat.clases.Bateria;


public class ServicioBateria extends Service {
	/** Propiedad de imagen de fondo. */
	private static final String PROP_FONDO = "setBackgroundResource";

	/** Instancia de la clase. */
	private static ServicioBateria objInstancia;

	/** Receptor de estado de batería. */
	private ReceptorBateria mBatteryStateReceiver;
	/** Receptor de estado de pantalla. */
	private ReceptorPantalla mScreenStateReceiver;

	/** Indica si se está cargando. */
	private boolean bConectado = false;
	/** Nivel de batería. */
	private int iNivelBateria = -1;
	private boolean bInicializar = true;


	/**
	 * Devuelve la instancia de la clase.
	 * @return
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
		final int iNivel = objIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		final int iEscala = objIntent.getIntExtra(BatteryManager.EXTRA_SCALE, Bateria.I_ESCALA);
		iNivelBateria = Bateria.getNivel(iNivel, iEscala);

		// Obtiene el estado de conexión de la batería.
		final int iEstado = objIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
		bConectado = (iEstado > 0 && iNivelBateria < 100);	
	}


	/**
	 * Actualiza los widget de la aplicación.
	 * @param  objContexto  Contexto de la aplicación.
	 * @param  iIdWidget    Identificador del widget.
	 */
	public static void actualizarWidget(final Context objContexto, final int iIdWidget) {
		if (objInstancia != null) {
			objInstancia.recibirBateria(null);
			objInstancia.actualizarWidget(objContexto, AppWidgetManager.getInstance(objContexto), iIdWidget);
		}
	}


	/**
	 * Actualiza los widgets de la aplicación.
	 * @param objContexto  Contexto de la aplicación.
	 * @param objManager   Administrador de widgets.
	 * @param iIdWidget    Identificador del widget.
	 */
	private void actualizarWidget(Context objContexto, AppWidgetManager objManager, int iIdWidget) {
		final RemoteViews objVistas = new RemoteViews(objContexto.getPackageName(), R.layout.widget);

		// Prepara el clic sobre el widget para lanza la actividad principal.
		final Intent objIntentLanza = new Intent();
		objIntentLanza.setClass(objContexto, InfoActivity.class);
		final PendingIntent objPendiente = PendingIntent.getActivity(objContexto, 0, objIntentLanza, 0);
		objVistas.setOnClickPendingIntent(R.id.llFondo, objPendiente);

		// Cambia el fondo del widget dependiendo del estado.
		// TODO revisar código para evitar que se dibuje innecesariamente.
		if (bConectado) {
			objVistas.setInt(R.id.llFondo, PROP_FONDO, R.drawable.wdg_carga);
		} else if (iNivelBateria < Bateria.NVL_WARNING) {
			objVistas.setInt(R.id.llFondo, PROP_FONDO, R.drawable.wdg_warning);
		} else if (iNivelBateria < Bateria.NVL_DANGER) {
			objVistas.setInt(R.id.llFondo, PROP_FONDO, R.drawable.wdg_danger);
		} else {
			objVistas.setInt(R.id.llFondo, PROP_FONDO, R.drawable.wdg_normal);
		}

		objVistas.setTextViewText(R.id.tvNivelWidget, Integer.toString(iNivelBateria));
		objManager.updateAppWidget(iIdWidget, objVistas);
	}


	/**
	 * Actualiza los widgets existentes.
	 * @param  objContexto  Contexto de la aplicación.
	 */
	public void actualizarWidgets(final Context objContexto) {
		final AppWidgetManager objGestor = AppWidgetManager.getInstance(objContexto);
		final int[] ariIds = objGestor.getAppWidgetIds(new ComponentName(objContexto, WidgetBateria.class));

		for (final int iActual : ariIds) {
			actualizarWidget(objContexto, objGestor, iActual);
		}
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
}
