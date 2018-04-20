package it.polito.tdp.meteo;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MeteoController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<Integer> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;

	private Model model;

	@FXML
	void doCalcolaSequenza(ActionEvent event) {
		txtResult.clear();

		if (boxMese.getValue() != null) {
			txtResult.setText(model.trovaSequenza(boxMese.getValue()));
		} else {
			txtResult.setText("Selezionare un mese");
			return;
		}
	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {
		txtResult.clear();

		if (boxMese.getValue() != null) {
			txtResult.setText(model.getUmiditaMedia(boxMese.getValue()));
		} else {
			txtResult.setText("Selezionare un mese");
			return;
		}
	}

	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
	}
	
	public void setModel(Model model) {
		this.model = model;
		boxMese.setItems(FXCollections.observableArrayList(model.getMesi()));
	}
}
