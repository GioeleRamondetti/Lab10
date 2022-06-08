package it.polito.tdp.rivers.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


import it.polito.tdp.rivers.db.RiversDAO;

public class Model {
	private int NgiorniMancatoServizio;
	private double acquaBacinoMedia;
	private double capienzaTot;
	private double acquaNelBacino;
	private double flussoUscitaMin;
	private int contatore=0;
	private PriorityQueue<Evento> queue;
	private RiversDAO dao;
	public Model() {
		dao=new RiversDAO(); 
	}
	
	public List<River> getRiverList(){
		return dao.getAllRivers();
	}
	
	public int nmisure(River river) {
		return dao.getnmisurazioni(river.getId());
	}
	public String getfirstdata(River river) {
		return dao.getfirst(river.getId());
	}
	public String  getlastdata(River river) {
		return dao.getlast(river.getId());
	}
	public Double  fmed(River river) {
		List<Double> lista=dao.getlistaflusso(river.getId());
		double misure=0;
		for(Double d: lista) {
			misure=misure+d;
		}
		return misure/lista.size();
	}
	
	// simulazione
	public void simulazione(double fattorek,double fmed,int riverid) {
		// definisco la coda
		this.contatore=0;
		this.acquaBacinoMedia=0;
		this.NgiorniMancatoServizio=0;
		this.queue=new PriorityQueue<Evento>();
		List<Evento> valoriFiumeing=new ArrayList<Evento>( dao.getvalori(riverid));
		Map<Integer,Evento> mappaf=new HashMap<Integer,Evento>();
		for(Evento e :valoriFiumeing) {
			mappaf.put(e.getTempo(), e);
		}
		// definisco var globali
		this.capienzaTot=fattorek*fmed*30*(24*60*60);
		this.acquaNelBacino=capienzaTot/2;
		this.flussoUscitaMin=fmed*0.8*(24*60*60);
		// Quando 𝑓𝑖𝑛 > 𝑓𝑜𝑢t , il livello 𝐶 nel bacino salirà di una quantità legata alla differenza tra i due flussi.
		this.queue.addAll(valoriFiumeing);
		// output simulazione gia inizializzati sono solo da aggiornare
		
		// cambio stato del mondothis.queue.add(new Event(1,this.nazioneIniziale,this.nIniziareMigranti));
	}
	public int getNgiorniMancatoServizio() {
		return NgiorniMancatoServizio;
	}

	public double getAcquaBacinoMedia() {
		return acquaBacinoMedia;
	}

	public void run() {
		while(!this.queue.isEmpty()) {
			Evento e=this.queue.poll();
			ProcessEvent(e);
		}
		this.acquaBacinoMedia =this.acquaBacinoMedia/this.contatore;
	}

	private void ProcessEvent(Evento e) {
		// ho 5% prob di avere 10*fmin
		contatore++;
		System.out.println("acqua bacino "+this.acquaNelBacino);
		
		int x=(int)(Math.random()*100);
		int prob=1;
		if(x>=0 && x<5) {
			prob=10;
		}
		System.out.println("capienza tot "+capienzaTot);
		// cotrollo quanto si riempie il bacino e aggiorno
		double flussoin=e.getFlusso();
		if(flussoin>(flussoUscitaMin*prob)) {
			// bacino si riempie 
			this.acquaNelBacino=this.acquaNelBacino+e.getFlusso()-this.flussoUscitaMin*prob;
		}else {
			if((e.getFlusso()+this.acquaNelBacino)>(flussoUscitaMin*prob)) {
				this.acquaNelBacino=this.acquaNelBacino+e.getFlusso()-this.flussoUscitaMin*prob;
			}else {
				// NON GARANTISCO EROGAZIONE MINIMA
				this.NgiorniMancatoServizio++;
				// bacino si svuota
				this.acquaNelBacino=0;
			}
		}
		
		if(acquaNelBacino>capienzaTot) {
			// evento di tracimazione
			this.acquaNelBacino=this.capienzaTot;
		}
		this.acquaBacinoMedia=this.acquaBacinoMedia+this.acquaNelBacino;
		
	}
		
	
}
