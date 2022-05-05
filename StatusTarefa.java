package todolist.model;

public enum StatusTarefa {
	ABERTA("Aberta"), 
	CONCLUIDA("Conclu�da"),
	ADIADA("Adiada");
	
	
	String descricao;
	
	private StatusTarefa(String desc) {
		this.descricao = desc;
	}
	
	public String toString() {
		return descricao;
	}
}
