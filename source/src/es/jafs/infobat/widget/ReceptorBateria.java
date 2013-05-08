package es.jafs.infobat.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



/**
 * Receptor de mensajes de estado de la bater�a.
 * @author  Jos� Antonio Fuentes Santiago
 * @version 1.0
 * @date    2013
 */
public class ReceptorBateria extends BroadcastReceiver {
	/**
	 * Recibe una notificaci�n de cambio en bater�a.
	 * @param objContexto  Contexto de la aplicaci�n.
	 * @param objIntent    Intent con la acci�n recibida.
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
