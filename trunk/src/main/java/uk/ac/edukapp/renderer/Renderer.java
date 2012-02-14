package uk.ac.edukapp.renderer;

import javax.persistence.EntityManager;

import uk.ac.edukapp.model.Widgetprofile;

public class Renderer {

	public static String render(EntityManager em,Widgetprofile widgetprofile){
		
		//deduce whether is w3c or open social
		byte w3cOrOs = widgetprofile.getW3cOrOs();
		
		if (w3cOrOs==0){//is w3c
			return WidgetRenderer.getInstance().render(widgetprofile.getWidId());
		}else if (w3cOrOs==1){//is os
			return GadgetRenderer.render(widgetprofile.getWidId());
		}
		
		return null;
	}
}
