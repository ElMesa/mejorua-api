package es.ua.dlsi.mejorua.api.persistance;

import java.util.HashMap;

import es.ua.dlsi.mejorua.api.business.IncidenciaBO;;

public class IncidenciaDAO {

	private static HashMap<Long, IncidenciaBO> incidencias = new HashMap<Long, IncidenciaBO>();
	
	public static void save(IncidenciaBO incidencia) {
				
		incidencias.put(incidencia.getId(), incidencia);
	}
	
	public static IncidenciaBO get(long id) {
		
		return incidencias.get(id);
	}
	
}
