package todolist.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import todolist.io.TarefaIO;
import todolist.model.StatusTarefa;
import todolist.model.tarefa;

public class IndexController implements Initializable, ChangeListener<tarefa> {
    @FXML
    private DatePicker dpdata;
    @FXML
    private TextField tfstatus;
    @FXML
    private TextField tftarefa;
    @FXML
    private TextArea tacomentario;
    @FXML
    private TextField tfcodigo;
    @FXML
    private TextField tfconcluida;
    @FXML
    private Label tflabelconcluida;
    @FXML
    private Button btsalvar;
    @FXML
    private Button btcalendario;
    @FXML
    private Button btlixeira;
    @FXML
    private Button btborracha;
    @FXML
    private Button btfinalizar;
    @FXML
    private Button btok;
    @FXML
    private TableView<tarefa> tvtarefa;
    @FXML 
    private TableColumn<tarefa, LocalDate> tcdata;
    @FXML
    private TableColumn<tarefa, String> tctarefa; 
    // VARIÁVEL PARA GUARDAR A TAREFA
    private tarefa tarefa;
    // VARIÁVEL PARA GUARDAR A LISTA DE TAREFAS
    private List<tarefa> tarefas;
    
    @FXML
    void aboutClick(ActionEvent event) {
    	try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/todolist/view/janela2.fxml"));
			Scene scene = new Scene(root, 470, 250);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Sobre o To Do List");
			// TIRANDO A BORDA DA JANELA
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void exportarClick(ActionEvent event) {
    	FileFilter filter = new FileNameExtensionFilter("Arquivos HTML", "html");
    	JFileChooser chooser = new JFileChooser();
    	chooser.setFileFilter(filter);
    	if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
    		File arqSelecionado = chooser.getSelectedFile();
    		arqSelecionado = new File(arqSelecionado+".html");
    		try {
				TarefaIO.exportHtml(tarefas, arqSelecionado);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Occoreu um erro ao exportar as tarefas","Erro", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
    	}
    }
    
    @FXML
    void sairClick(ActionEvent event) {
    	System.exit(0);
    }

    @FXML
    void calendarioClick(ActionEvent event) {
    	if(tarefa != null) {
    		int dias = Integer.parseInt(JOptionPane.showInputDialog(null, "Quantos dias você deseja adiar?", 
    																"Informe quantos dias",
    																JOptionPane.QUESTION_MESSAGE));	
    		LocalDate novaData = tarefa.getDataLimite().plusDays(dias);
    		tarefa.setDataLimite(novaData);
    		tarefa.setStatus(StatusTarefa.ADIADA);
    		
    		try {
				TarefaIO.saveTarefas(tarefas);
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyy");
				JOptionPane.showMessageDialog(null,"Tarefa adiada com sucesso\n Nova data: "+novaData.format(fmt));
				carregarTarefas();
				limparCampos();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Occoreu um erro ao atualizar as tarefas","Erro", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
    	}
    }

    @FXML
    void finalizarClick(ActionEvent event) {
    	if(tarefa != null) {
    		tarefa.setStatus(StatusTarefa.CONCLUIDA);
    		tarefa.setDataFinalizada(LocalDate.now());
			
    		try {
				TarefaIO.saveTarefas(tarefas);
				
				JOptionPane.showMessageDialog(null, "Tarefa concluída com sucesso");
				carregarTarefas();
				limparCampos();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Occoreu um erro ao Finalizar a tarefa","Erro", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
    		
    	}
    }

    @FXML
    void limparClick(ActionEvent event) {
    	limparCampos();
    }
   
    @FXML
    void lixeiraClick(ActionEvent event) {
    	if(tarefa != null) {
    		int resposta = 
    				JOptionPane.showConfirmDialog(null,
    					"Deseja realmente excluir a tarefa "+tarefa.getId()+"?",
    					"Confirmar exclusão", JOptionPane.YES_NO_OPTION,
    					JOptionPane.QUESTION_MESSAGE);
    		if (resposta == 0) {
    			tarefas.remove(tarefa);
    			try{
    				TarefaIO.saveTarefas(tarefas);
    				limparCampos();
    				carregarTarefas();
    			} catch(IOException e) {
    				JOptionPane.showMessageDialog(null, "Ocorreu um erro ao excluir a tarefa", "Erro", JOptionPane.ERROR_MESSAGE);
    				e.printStackTrace();
    			}
    		}
    	}
    }

    @FXML
    void salvarClick(ActionEvent event) {
    	// VALIDAÇÃO DE CAMPOS 
    	if(dpdata.getValue() == null) {
    		JOptionPane.showMessageDialog(null, "Informe a data limite",
    				"Informe", JOptionPane.ERROR_MESSAGE);
    		dpdata.requestFocus();
    		
    	}else if(tftarefa.getText().isEmpty()){
    		JOptionPane.showMessageDialog(null, "Informe a descrição da tarefa",
    				"Informe", JOptionPane.ERROR_MESSAGE);
    		tftarefa.requestFocus();
    	}else {
    		// VERIFICA SE A DATA INFORMADA NÃO É ANTERIOR À DATA ATUAL 
    		if(dpdata.getValue().isBefore(LocalDate.now())) {
    			JOptionPane.showMessageDialog(null, "já passou da data",
    					"Data Inválida",  JOptionPane.ERROR_MESSAGE);
    		}else {
    			// VERIFICA SE É UMA TAREFA NOVA
    			if(tarefa == null) {
    				// INSTANCIAR A TAREFA
        			tarefa = new tarefa();
        			tarefa.setDataCriacao(LocalDate.now());
        			tarefa.setStatus(StatusTarefa.ABERTA);
    			}
    			// POPULAR A TAREFA
    			tarefa.setDescricao(tftarefa.getText().replace(";",""));
    			tarefa.setComentario(tacomentario.getText().replace("\n","").replace(";",""));
    			tarefa.setDataLimite(dpdata.getValue());
    			
    			// SALVAR NO BANCO DE DADOS
    			try {
    				// SALVA UMA NOVA OU ATUALIZA A QUE JÁ EXISTE
    				if(tarefa.getId() == 0) {
    					TarefaIO.insert(tarefa);
    				}else {
    					TarefaIO.saveTarefas(tarefas);
    				}
					carregarTarefas();
	    			limparCampos();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Arquivo não encontrado: "+e.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
					
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Erro de I/O "+e.getMessage(),
							"Erro", JOptionPane.ERROR_MESSAGE);
				}   			
    		}	
    	}
    	carregarTarefas();
    }
    
   // LIMPAR OS CAMPOS DO FORMULÁRIO
    private void limparCampos() {
    	tarefa = null;
    	dpdata.setValue(null);
    	tacomentario.setText(null);
    	tftarefa.setText(null);
    	tfstatus.setText(null);
    	dpdata.requestFocus();
    	dpdata.setDisable(false);  
		btfinalizar.setDisable(true);
		btlixeira.setDisable(true);
		btcalendario.setDisable(true);
		tvtarefa.getSelectionModel().clearSelection();
		tfcodigo.clear();
		
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// DEFINE OS PARÂMETROS PARA AS COLUNAS DO TableView 
		tcdata.setCellValueFactory(new PropertyValueFactory<>("dataLimite"));
		tctarefa.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		// FORMATANDO A DATA NA COLUNA tcdata
		tcdata.setCellFactory(call -> {
			
			return new TableCell<tarefa, LocalDate>() {
				@Override
					protected void updateItem(LocalDate item, boolean empty) {
						// TODO Auto-generated method stub
						super.updateItem(item, empty);
						DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MMM/yyy");
						if(!empty) {
							setText(item.format(fmt));
						}else {
							setText("");
						}
					}
				};
			});
		tvtarefa.setRowFactory(call -> new TableRow<tarefa>() {
			protected void updateItem(tarefa item, boolean empty) {
				super.updateItem(item, empty);
				if(item == null) {
					setStyle("");
				}else if(item.getStatus() == StatusTarefa.CONCLUIDA) {
					setStyle("-fx-background-color :#ccffcc;");
				}else if(item.getDataLimite().isBefore(LocalDate.now())) {
					setStyle("-fx-background-color: #ffcccc;");
				}else if(item.getStatus() == StatusTarefa.ADIADA) {
					setStyle("-fx-background-color: #ffffcc;");
				}else {
					setStyle("-fx-background-color: #d0dbe4;");


				}
			};
		});
		
		// EVENTO DE SELEÇÃO DE UM ITEM NA TableView
		tvtarefa.getSelectionModel().selectedItemProperty().addListener(this);
		
		carregarTarefas();
		
	}
    
	private void carregarTarefas() {
		try {
			// BUSCANDO AS TAREFAS NO BD E GUARDANDO NA VARIÁVEL TAREFAS
			tarefas = TarefaIO.readTarefas();
			// CONVERTENDO A LISTA PARA ObservableList E ASSOCIANDO AO TableView
			tvtarefa.setItems(FXCollections.observableArrayList(tarefas));
			// ATUALIZA A TABELA
			tvtarefa.refresh();
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog
				(null, "Erro ao buscar as tarefas",
					"Erro", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	@Override
	public void changed(ObservableValue<? extends tarefa> observable, tarefa oldValue, tarefa newValue) {
		// PASSAR A REFERÊNCIA DA VARIÁVEL LOCAL PARA A TAREFA GLOBAL
		tarefa = newValue;
		// SE HOUVER UMA TAREFA SELECIONADA
		if(tarefa != null) {
			dpdata.setValue(tarefa.getDataLimite());
			tfstatus.setText(tarefa.getStatus().toString());
			tftarefa.setText(tarefa.getDescricao());
			tacomentario.setText(tarefa.getComentario());
			dpdata.setDisable(true);  
			dpdata.setOpacity(1);
			tfcodigo.setText(tarefa.getId()+"");
			
			// DESABILITAR OS BOTÕES E AS TEXTFIELD QUANDO A TAREFA ESTIVER CONCLUÍDA
			if(tarefa.getStatus() == StatusTarefa.CONCLUIDA) {
				System.out.println();
				btsalvar.setDisable(true);
				btfinalizar.setDisable(true);
				btcalendario.setDisable(true);
				tftarefa.setEditable(false);
				tacomentario.setEditable(false);
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyy");
				tfconcluida.setText(tarefa.getDataFinalizada().format(fmt)+"");
				tfconcluida.setVisible(true);
				tflabelconcluida.setVisible(true);
			}else {
				btsalvar.setDisable(false);
				btfinalizar.setDisable(false);
				btcalendario.setDisable(false);
				tftarefa.setEditable(true);
				tacomentario.setEditable(true);
				tfconcluida.setVisible(false);
				tflabelconcluida.setVisible(false);
			}
			btlixeira.setDisable(false);
			
			
		}	
	}
    
}


    