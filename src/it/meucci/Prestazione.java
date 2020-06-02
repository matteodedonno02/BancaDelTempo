package it.meucci;

import java.sql.Date;

public class Prestazione 
{
	private int idPrestazione;
	private Date data;
	private int ore;
	private String descrizione;
	private int idCategoria;
	private int idErogatore;
	private int idFruitore;
	
	
	public Prestazione(int idPrestazione, Date data, int ore, String descrizione, int idCategoria, int idErogatore,int idFruitore) 
	{
		this.idPrestazione = idPrestazione;
		this.data = data;
		this.ore = ore;
		this.descrizione = descrizione;
		this.idCategoria = idCategoria;
		this.idErogatore = idErogatore;
		this.idFruitore = idFruitore;
	}


	public Prestazione(Date data, int ore, String descrizione) 
	{
		this.data = data;
		this.ore = ore;
		this.descrizione = descrizione;
	}
	

	public int getIdPrestazione() 
	{
		return idPrestazione;
	}


	public void setIdPrestazione(int idPrestazione) 
	{
		this.idPrestazione = idPrestazione;
	}


	public Date getData() 
	{
		return data;
	}


	public void setData(Date data) 
	{
		this.data = data;
	}


	public int getOre() 
	{
		return ore;
	}


	public void setOre(int ore) 
	{
		this.ore = ore;
	}


	public String getDescrizione() 
	{
		return descrizione;
	}


	public void setDescrizione(String descrizione) 
	{
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


	public int getIdErogatore() 
	{
		return idErogatore;
	}


	public void setIdErogatore(int idErogatore) 
	{
		this.idErogatore = idErogatore;
	}


	public int getIdFruitore() 
	{
		return idFruitore;
	}


	public void setIdFruitore(int idFruitore) 
	{
		this.idFruitore = idFruitore;
	}
}
