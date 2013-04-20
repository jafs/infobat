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
 * Actividad principal que gestiona el estado de la batería.
 * @author  José Antonio Fuentes Santiago
 * @version 1.0
 * @date    2013
 */
public class InfoActivity extends Activity {
	/** Receptor de información de batería. */
	private Receptor objReceptor = null;

	/** Tabla con información de la batería. */
	private TableLayout tlDatos = null;
	/** TextView que indica que no hay batería. */
	private TextView tvNoPresent = null;


	/**
	 * Controla la creación de la actividad.
	 * @param savedInstanceState 
	 */
	@Override
	public final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Realiza la carga de las cadenas de batería.
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
	 * Recibe una notificación de cambio en la batería.
	 * @param  objBateria  Objeto con los datos de la batería.
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
			// Nivel de la batería.
			final StringBuilder objTexto = new StringBuilder();
			objTexto.append(objBateria.getNivel());
			objTexto.append('%');
			setTexto(R.id.tvNivel, objTexto.toString());

			// Temperatura de la batería.
			objTexto.setLength(0);
			objTexto.append(objBateria.getTemperatura());
			objTexto.append(" ºC / ");
			objTexto.append(Utiles.celsiusToFarhenheit(objBateria.getTemperatura()));
			objTexto.append(" ºF");
			setTexto(R.id.tvTemperatura, objTexto.toString());

			// Voltaje de la batería.
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
	 * Clase encargada de recibir mensajes de broadcast con información sobre
	 * el estado de la batería del sistema y realizar el tratamiento de
	 * información.
	 * @author  José Antonio Fuentes Santiago
	 * @version 1.0
	 * @date    2013
	 */
	class Receptor extends BroadcastReceiver {
		/** Objeto con la información de la batería. */
		private final Bateria objBateria = new Bateria();

		/** Escala de control de valores de batería. */
		private int iEscala = Bateria.I_ESCALA;
		/** Indica si se debe realizar la carga inicial. */
		private boolean bInicial = true;


		/**
		 * Controla la recepción de mensajes de estado de batería.
		 * @param  objContexto  Contexto de ejecución.
		 * @param  objIntent    Intent con la información.
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
		 * Carga los valores iniciales de la batería.
		 * @param objIntent  Objeto con la información.
		 */
		private void cargaInicial(final Intent objIntent) {
			if (null != objIntent) {
				objBateria.setTecnologia(objIntent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY));
				iEscala = objIntent.getIntExtra(BatteryManager.EXTRA_SCALE, Bateria.I_ESCALA);
			}
		}


		/**
		 * Extrae los datos de información de batería.
		 * @param objIntent  Objeto con la información.
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
