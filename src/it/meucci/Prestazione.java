package it.meucci;

import java.sql.Date;

public class Prestazione 
{
	private int idPrestazione;
	private Date data;
	private String dataFormattata;
	private int ore;
	private String descrizione;
	private int statoPrestazione;
	private int idCategoria;
	private int idErogatore;
	private int idFruitore;
	private Categoria categoria;
	private Utente fruitore;
	private Utente erogatore;
	
	
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
	
	
	public Prestazione(int idPrestazione, Date data, int ore, String descrizione, int statoPrestazione, int idCategoria, int idErogatore,int idFruitore) 
	{
		this.idPrestazione = idPrestazione;
		this.data = data;
		this.ore = ore;
		this.descrizione = descrizione;
		this.statoPrestazione = statoPrestazione;
		this.idCategoria = idCategoria;
		this.idErogatore = idErogatore;
		this.idFruitore = idFruitore;
	}
	

	public Prestazione(int idPrestazione, String dataFormattata, int ore, String descrizione, int statoPrestazione, Categoria categoria, Utente fruitore,Utente erogatore) 
	{
		this.idPrestazione = idPrestazione;
		this.dataFormattata = dataFormattata;
		this.ore = ore;
		this.descrizione = descrizione;
		this.statoPrestazione = statoPrestazione;
		this.categoria = categoria;
		this.fruitore = fruitore;
		this.erogatore = erogatore;
	}
	
	
	public Prestazione(int idPrestazione, Date data, int ore, String descrizione, int statoPrestazione, Categoria categoria, Utente fruitore,Utente erogatore) 
	{
		this.idPrestazione = idPrestazione;
		this.data = data;
		this.ore = ore;
		this.descrizione = descrizione;
		this.statoPrestazione = statoPrestazione;
		this.categoria = categoria;
		this.fruitore = fruitore;
		this.erogatore = erogatore;
	}
	
	
	public Prestazione(int idPrestazione, Date data, int ore, String descrizione, Utente fruitore, Utente erogatore) 
	{
		this.idPrestazione = idPrestazione;
		this.data = data;
		this.ore = ore;
		this.descrizione = descrizione;
		this.fruitore = fruitore;
		this.erogatore = erogatore;
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


	public Categoria getCategoria() 
	{
		return categoria;
	}
	

	public void setCategoria(Categoria categoria) 
	{
		this.categoria = categoria;
	}


	public Utente getFruitore()
	{
		return fruitore;
	}


	public void setFruitore(Utente fruitore)
	{
		this.fruitore = fruitore;
	}


	public Utente getErogatore() 
	{
		return erogatore;
	}


	public void setErogatore(Utente erogatore) 
	{
		this.erogatore = erogatore;
	}


	public String getDataFormattata() 
	{
		return dataFormattata;
	}


	public void setDataFormattata(String dataFormattata) 
	{
		this.dataFormattata = dataFormattata;
	}


	public int getStatoPrestazione() 
	{
		return statoPrestazione;
	}


	public void setStatoPrestazione(int statoPrestazione)
	{
		this.statoPrestazione = statoPrestazione;
	}
}
