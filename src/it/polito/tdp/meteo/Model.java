package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;


public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private MeteoDAO meteoDAO;

	public Model() {
		meteoDAO = new MeteoDAO();
	}

	public String getUmiditaMedia(int mese) {
		Set<Citta> citta = meteoDAO.getLocalita();
		String ris = "";
		for(Citta c : citta) {
			ris += c.getNome()+": " + meteoDAO.getAvgRilevamentiLocalitaMese(mese, c.getNome())+"\n";
		}
		return ris.trim();
	}

	public String trovaSequenza(int mese) {

		return "TODO!";
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {
		double score = 0.0;
		
		
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		return true;
	}

	public List<Integer> getMesi() {
		List<Integer> mesi = new ArrayList<>();
		for (int i=1; i<13; i++) {
			mesi.add(i);
		}
		return mesi;
	}

}
