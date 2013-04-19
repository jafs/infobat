package es.jafs.infobat.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



/**
 * Receptor broadcast de estado de pantalla.
 * @author  jafsantiago
 * @version 1.0
 * @date    2013
 */
public class ReceptorPantalla extends BroadcastReceiver {
	/**
	 * Recibe cambios de estado de la pantalla.
	 * @param  objContexto  Contexto de la aplicación.
	 * @param  objIntent    Intent de la emisión broadcast.
	 */
	@Override
	public void onReceive(Context objContexto, Intent objIntent) {
		if (null != ServicioBateria.getInstancia()) {
			if (Intent.ACTION_SCREEN_ON.equals(objIntent.getAction())) {
				ServicioBateria.getInstancia().registrarBateria();
			} else if (Intent.ACTION_SCREEN_OFF.equals(objIntent.getAction())) {
				ServicioBateria.getInstancia().cancelarBateria();
			}
		}
	}
}
