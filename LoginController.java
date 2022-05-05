package todolist.controller;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class LoginController {
	 @FXML
	    private Button btLogin;
	 @FXML
	    private TextField tfUsuario;
	    @FXML
	    private PasswordField pfSenha;
	    
	 
	 @FXML
	    public void btLoginClick() {
		 if(tfUsuario.getText().equals("123") && pfSenha.getText().equals("123")) {
		 	
		 		try {
		 			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/todolist/view/Index.fxml"));
					Scene scene = new Scene(root, 820, 460);
					Stage stage = new Stage();
					stage.setScene(scene);
					stage.setTitle("To Do List");
					stage.getIcons().add(new Image(getClass().getResourceAsStream("/todolist/imagens/login.png")));
					stage.show();
					stage = (Stage) btLogin.getScene().getWindow();
			    	stage.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		 	
		 }else {
			 		JOptionPane.showMessageDialog(null, "Usuário ou Senha inválidos","Erro", JOptionPane.ERROR_MESSAGE);
			 	}
			 	
	    }
	 
}
