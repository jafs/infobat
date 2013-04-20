package es.jafs.infobat.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



/**
 * Receptor de mensajes de estado de la batería.
 * @author  José Antonio Fuentes Santiago
 * @version 1.0
 * @date    2013
 */
public class ReceptorBateria extends BroadcastReceiver {
	/**
	 * Recibe una notificación de cambio en batería.
	 * @param objContexto  Contexto de la aplicación.
	 * @param objIntent    Intent con la acción recibida.
	 */
	@Override
	public void onReceive(final Context objContexto, final Intent objIntent) {
		if (null != ServicioBateria.getInstancia()) {
			if (Intent.ACTION_POWER_CONNECTED.equals(objIntent.getAction()) ||
				(Intent.ACTION_POWER_DISCONNECTED.equals(objIntent.getAction()))) {
				ServicioBateria.getInstancia().recibirBateria(null);
			} else {
				ServicioBateria.getInstancia().recibirBateria(objIntent);
			}
	
			ServicioBateria.getInstancia().actualizarWidgets(objContexto);
		}
	}
}
