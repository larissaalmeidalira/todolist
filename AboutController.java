package todolist.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutController {
	    @FXML
	    private Button btok;

	    @FXML
	    void okClique(ActionEvent event) {
	    	Stage stage = (Stage) btok.getScene().getWindow();
	    	stage.close();
	    }

	}

