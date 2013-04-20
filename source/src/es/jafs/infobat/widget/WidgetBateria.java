package es.jafs.infobat.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;



/**
 * Controlador de widget de batería.
 * @author  José Antonio Fuentes Santiago
 * @version 1.0
 * @date    2013
 */
public class WidgetBateria extends AppWidgetProvider {
	/**
	 * Desactiva el servicio de actualización del widget.
	 * @param  objContexto  Contexto de la aplicación.
	 */
	@Override
	public void onDisabled(final Context objContexto) {
		objContexto.stopService(new Intent(objContexto, ServicioBateria.class));
	}


	/**
	 * Activa el servicio de actualización del widget.
	 * @param  objContexto       Contexto de la aplicación.
	 * @param  objWidgetManager  Administrador de widgets.
	 * @param  ariWidgetIds      Identificadores de widgets activos.
	 */
	@Override
	public void onUpdate(final Context objContexto, final AppWidgetManager objWidgetManager,
						final int[] ariWidgetIds) {
		objContexto.startService(new Intent(objContexto, ServicioBateria.class));
	}
}
