package es.jafs.infobat.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import es.jafs.infobat.R;
import es.jafs.infobat.clases.Bateria;
import es.jafs.infobat.clases.CadenasBateria;
import es.jafs.infobat.utiles.Utiles;


/**
 * Actividad principal que gestiona el estado de la bater�a.
 * @author  Jos� Antonio Fuentes Santiago
 * @version 1.0
 * @date    2013
 */
public class InfoActivity extends Activity {
	/** Receptor de informaci�n de bater�a. */
	private Receptor objReceptor = null;

	/** Tabla con informaci�n de la bater�a. */
	private TableLayout tlDatos = null;
	/** TextView que indica que no hay bater�a. */
	private TextView tvNoPresent = null;


	/**
	 * Controla la creaci�n de la actividad.
	 * @param savedInstanceState 
	 */
	@Override
	public final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Realiza la carga de las cadenas de bater�a.
		CadenasBateria.getInstancia().cargar(this);

		tlDatos = (TableLayout) findViewById(R.id.tlDatos);
		tvNoPresent = (TextView) findViewById(R.id.tvNoPresente);

		// Controla el clic de cerrar.
		Button btnCerrar = Button.class.cast(findViewById(R.id.btnCerrar));
		btnCerrar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}


	/**
	 * Registra el receptor al reanudar la actividad.
	 */
	@Override
	protected void onResume() {
		super.onResume();

		if (null == objReceptor) {
			objReceptor = new Receptor();
			registerReceiver(objReceptor, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		}
	}


	/**
	 * Desactiva el receptor broadcast al cerrar la actividad.
	 */
	@Override
	protected void onStop() {
		if (null != objReceptor) {
			unregisterReceiver(objReceptor);
		}
		super.onStop();
	}


	/**
	 * Establece el texto en el control recibido.
	 * @param  iControl  Identificador del control.
	 * @param  sTexto    Texto a establecer.
	 */
	private void setTexto(final int iControl, final String sTexto) {
		final TextView tvTexto = (TextView) findViewById(iControl);
		if (null != tvTexto) {
			tvTexto.setText(sTexto);
		}
	}


	/**
	 * Recibe una notificaci�n de cambio en la bater�a.
	 * @param  objBateria  Objeto con los datos de la bater�a.
	 */
	public final void onCambio(final Bateria objBateria) {
		if (null != objBateria && objBateria.isPresente()) {
			tlDatos.setVisibility(View.VISIBLE);
			tvNoPresent.setVisibility(View.INVISIBLE);
	
			// Campos sencillos
			// ----------------
			setTexto(R.id.tvEnchufada, CadenasBateria.getInstancia().getConexion(objBateria.getConectada()));
			setTexto(R.id.tvSalud, CadenasBateria.getInstancia().getSalud(objBateria.getSalud()));
			setTexto(R.id.tvEstado, CadenasBateria.getInstancia().getEstado(objBateria.getEstado()));
			setTexto(R.id.tvTecnologia, objBateria.getTecnologia());

			// Campos complejos
			// ----------------
			// Nivel de la bater�a.
			final StringBuilder objTexto = new StringBuilder();
			objTexto.append(objBateria.getNivel());
			objTexto.append('%');
			setTexto(R.id.tvNivel, objTexto.toString());

			// Temperatura de la bater�a.
			objTexto.setLength(0);
			objTexto.append(objBateria.getTemperatura());
			objTexto.append(" �C / ");
			objTexto.append(Utiles.celsiusToFarhenheit(objBateria.getTemperatura()));
			objTexto.append(" �F");
			setTexto(R.id.tvTemperatura, objTexto.toString());

			// Voltaje de la bater�a.
			objTexto.setLength(0);
			objTexto.append(objBateria.getVoltaje());
			objTexto.append(" V");
			setTexto(R.id.tvVoltaje, objTexto.toString());
		} else {
			tlDatos.setVisibility(View.INVISIBLE);
			tvNoPresent.setVisibility(View.VISIBLE);
		}
	}


	/**
	 * Clase encargada de recibir mensajes de broadcast con informaci�n sobre
	 * el estado de la bater�a del sistema y realizar el tratamiento de
	 * informaci�n.
	 * @author  Jos� Antonio Fuentes Santiago
	 * @version 1.0
	 * @date    2013
	 */
	class Receptor extends BroadcastReceiver {
		/** Objeto con la informaci�n de la bater�a. */
		private final Bateria objBateria = new Bateria();

		/** Escala de control de valores de bater�a. */
		private int iEscala = Bateria.I_ESCALA;
		/** Indica si se debe realizar la carga inicial. */
		private boolean bInicial = true;


		/**
		 * Controla la recepci�n de mensajes de estado de bater�a.
		 * @param  objContexto  Contexto de ejecuci�n.
		 * @param  objIntent    Intent con la informaci�n.
		 */
		@Override
		public void onReceive(final Context objContexto, final Intent objIntent) {
			if (bInicial) {
				cargaInicial(objIntent);
				bInicial = false;
			}
			obenerDatos(objIntent);
		}


		/**
		 * Carga los valores iniciales de la bater�a.
		 * @param objIntent  Objeto con la informaci�n.
		 */
		private void cargaInicial(final Intent objIntent) {
			if (null != objIntent) {
				objBateria.setTecnologia(objIntent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY));
				iEscala = objIntent.getIntExtra(BatteryManager.EXTRA_SCALE, Bateria.I_ESCALA);
			}
		}


		/**
		 * Extrae los datos de informaci�n de bater�a.
		 * @param objIntent  Objeto con la informaci�n.
		 */
		private void obenerDatos(final Intent objIntent) {
			if (null != objIntent) {
				objBateria.setPresente(objIntent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false));

				if (objBateria.isPresente()) {
					objBateria.setNivel(objIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0), iEscala);
					objBateria.setConectada(objIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0));
					objBateria.setSalud(objIntent.getIntExtra(BatteryManager.EXTRA_HEALTH, 1));
					objBateria.setTemperatura(objIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0));
					objBateria.setVoltaje(objIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0));
					objBateria.setEstado(objIntent.getIntExtra(BatteryManager.EXTRA_STATUS, 0));
				}
			} else {
				objBateria.setPresente(false);
			}

			if (objBateria.isModificada()) {
				onCambio(objBateria);
				objBateria.reiniciar();
			}
		}
	}
}
