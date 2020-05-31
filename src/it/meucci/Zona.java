package it.meucci;


public class Zona 
{
	private int idZona;
	private String descrizione;
	private String htmlMappa;
	
	
	public Zona(int idZona, String descrizione, String htmlMappa) 
	{
		this.idZona = idZona;
		this.descrizione = descrizione;
		this.htmlMappa = htmlMappa;
	}


	public int getIdZona() 
	{
		return idZona;
	}


	public void setIdZona(int idZona) 
	{
		this.idZona = idZona;
	}


	public String getDescrizione() 
	{
		return descrizione;
	}


	public void setDescrizione(String descrizione) 
	{
		this.descrizione = descrizione;
	}


	public String getHtmlMappa() 
	{
		return htmlMappa;
	}


	public void setHtmlMappa(String htmlMappa) 
	{
		this.htmlMappa = htmlMappa;
	}
}
