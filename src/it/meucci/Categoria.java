package it.meucci;


public class Categoria 
{
	private int idCategoria;
	private String descrizione;
	
	
	public Categoria(int idCategoria, String descrizione) 
	{
		super();
		this.idCategoria = idCategoria;
		this.descrizione = descrizione;
	}
	
	
	public int getIdCategoria() 
	{
		return idCategoria;
	}
	
	
	public void setIdCategoria(int idCategoria) 
	{
		this.idCategoria = idCategoria;
	}
	
	
	public String getDescrizione() 
	{
		return descrizione;
	}
	
	
	public void setDescrizione(String descrizione) 
	{
		this.descrizione = descrizione;
	}
}
