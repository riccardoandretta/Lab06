package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	private MeteoDAO meteoDAO;
	private ArrayList<Citta> cities = null;

	private double punteggioMiglioreSoluzione;
	private ArrayList<SimpleCity> miglioreSoluzione = null;

	public Model() {
		meteoDAO = new MeteoDAO();

		cities = new ArrayList<>();
		for (String s : meteoDAO.getLocalita())
			cities.add(new Citta(s));
	}

	public String getUmiditaMedia(int mese) {
		if (mese < 1 || mese > 12)
			return "**ERRORE** Mese deve essere nel range 1-12\n";

		List<String> citta = meteoDAO.getLocalita();
		StringBuilder sb = new StringBuilder();

		for (String c : citta) {
			sb.append(c + ": " + meteoDAO.getAvgRilevamentiLocalitaMese(mese, c) + "\n");
		}
		return sb.toString().trim();
	}

	public String trovaSequenza(int mese) {
		
		if (mese < 1 || mese > 12)
			return "**ERRORE** Mese deve essere nel range 1-12\n";

		punteggioMiglioreSoluzione = Double.MAX_VALUE;
		miglioreSoluzione = new ArrayList<>();
		int livello = 0;
		
		recursive(livello, miglioreSoluzione);		

		return miglioreSoluzione.toString();
	}

	private void recursive(int livello, List<SimpleCity> parziale) {

		// condizione di terminazione
		if (livello >= NUMERO_GIORNI_TOTALI) {
			
			double score = this.punteggioSoluzione(parziale);
			
			if (score < this.punteggioMiglioreSoluzione) {

				this.punteggioMiglioreSoluzione = score;
				this.miglioreSoluzione = new ArrayList<>(parziale);
			}
		}

		for (Citta citta : cities) {

			SimpleCity sc = new SimpleCity(citta.getNome());
			parziale.add(sc);
			citta.increaseCounter();

			if (controllaParziale(parziale)) { // se rispetta le condizioni continuo ad esplorare l'albero
				recursive(livello + 1, parziale);
			}

			// altrimenti torno subito indietro
			parziale.remove(sc);
			citta.setCounter(citta.getCounter() - 1);
		}
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {
		// Ricevo una lista soluzioneCandidata ad avere minimo costo;
		// Per ogni citta nella lista controllo se mi sposto dalla citta (allora aumento
		// il costo fisso)
		// oppure se rimango in citta (allora aumento solo il costo variabile)

		// Controllo che la lista non sia nulla o vuota
		if (soluzioneCandidata == null || soluzioneCandidata.size() == 0)
			return Double.MAX_VALUE; // valore grande che mi dà certezza che non sia l'ottimo

		// Controllo che la soluzione contenga tutte le citta'
		for (Citta c : cities) {
			if (!soluzioneCandidata.contains(new SimpleCity(c.getNome())))
				return Double.MAX_VALUE;
		}

		double score = 0.0;
		SimpleCity previous = soluzioneCandidata.get(0); // prendo il primo

		for (SimpleCity sc : soluzioneCandidata) {
			if (!previous.equals(sc)) { // se il prossimo elemento della lista è una citta diversa..
				score += COST;
			}
			previous = sc;
			score += sc.getCosto();
		}

		return score;

	}

	private boolean controllaParziale(List<SimpleCity> parziale) {
		// Devo rispettare i vincoli: ricevo una soluzione parziale e verifico;

		if (parziale == null) { // se null non è valido
			return false;
		}
		if (parziale.size() == 0) { // ma se è vuota potrebbe essere valida
			return true;
		}

		for (Citta c : cities) { // controllo che non sia nella stessa citta piu del massimo
			if (c.getCounter() > NUMERO_GIORNI_CITTA_MAX) {
				return false;
			}
		}

		SimpleCity previous = parziale.get(0);
		int counter = 0;

		for (SimpleCity sc : parziale) {
			if (!previous.equals(sc)) {
				if (counter < NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
					return false;
				}
				counter = 1; // perche se sono entrato nell'if ho gia una citta diversa
				previous = sc; // quindi cambio la citta con cui confrontare
			} else {
				counter++;
			}
		}

		return true;

	}

	public List<Integer> getMesi() {
		List<Integer> mesi = new ArrayList<>();
		for (int i = 1; i < 13; i++) {
			mesi.add(i);
		}
		return mesi;
	}

}
