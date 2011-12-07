import java.util.LinkedList;


public class Sorte implements Comparable<Sorte>
{
	protected LinkedList<Processus> listeProcessus = new LinkedList<Processus>();
	protected Graphe graphe;
	protected String nom;
	
	public Sorte(String nom, Graphe graphe, int taille)
	{
		this.nom = nom;
		this.graphe = graphe;
		for(int i=0; i<taille; i++)
		{
			listeProcessus.add(new Processus(this, i));
		}
	}
	/**
	 * @return the listeProcessus
	 */
	public LinkedList<Processus> getListeProcessus() {
		return listeProcessus;
	}
	/**
	 * @param listeProcessus the listeProcessus to set
	 */
	public void setListeProcessus(LinkedList<Processus> listeProcessus) {
		this.listeProcessus = listeProcessus;
	}
	/**
	 * @return the graphe
	 */
	public Graphe getGraphe() {
		return graphe;
	}
	/**
	 * @param graphe the graphe to set
	 */
	public void setGraphe(Graphe graphe) {
		this.graphe = graphe;
	}
	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String toString()
	{
		String res = nom + " { ";
		for(Processus p: listeProcessus)
		{
			res += p.toString() + ", ";
		}
		res = res.substring(0, res.length()-2);
		res += "}";
		return res;
	}
	
	/**
	 * supprime les processus qui ne sont pas liés à toutes les autres sortes
	 * @return
	 */
	public boolean nettoyerSorte()
	{
		boolean res = false;
		Processus[] tableau = new Processus[listeProcessus.size()];
		int i=0;
		/*
		 * on supprime les processus inutiles � l'aide de 2 boucles for, pour �viter
		 * de lever une ConcurrentModificationException
		 */
		for(Processus p: listeProcessus)
		{
			LinkedList<Processus> listeAssoc = p.getListeAssociations();
			if(!(p.estValide()))
			{
				for(Processus assoc: listeAssoc)
				{
					assoc.getListeAssociations().remove(p);
				}
				tableau[i] = p;
				res = true;
			}
		}
		for(Processus p: tableau)
		{
			if(p != null) listeProcessus.remove(p);
		}
		return res;
	}
	
	/**
	 * 
	 * @param num le numéro du processus à renvoyer
	 * @return
	 */
	public Processus getProcessusByNumero(int num)
	{
		try{return listeProcessus.get(num);}
		catch(IndexOutOfBoundsException e)
		{
			System.err.println("Nom: " + this.nom + "\n" + "Num�ro essay�: " + num);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * renvoie la somme du nombre d'associations de chaque processus
	 * @return
	 */
	public int getTotalAssociations() 
	{
		int somme = 0;
		for(Processus p: listeProcessus)
		{
			somme += p.getListeAssociations().size();
		}
		return somme;
	}
	@Override
	public int compareTo(Sorte s) 
	{
		if(this.getTotalAssociations()==s.getTotalAssociations()) return 0;
		else if(this.getTotalAssociations() < s.getTotalAssociations()) return -1;
		else return 1;
	}
	
	/**
	 * nombre d'associations entre la sorte courante et la sorte pass�e en param�tre
	 * @param s
	 * @return
	 */
	public int getNbAssociations(Sorte s)
	{
		int res = 0;
		for(Processus p: listeProcessus)
		{
			for(Processus a: p.getListeAssociations())
			{
				if(a.getSorte() == s) res += 1;
			}
		}
		return res;
	}
	
	/**
	 * fait la somme des associations par sorte, et renvoie la plus petite somme
	 * @return
	 */
	public int getNbAssociationsMin()
	{
		LinkedList<Sorte> listeSortes = this.graphe.getListeSortes();
		Sorte s0 = listeSortes.getFirst() == this? listeSortes.getLast(): listeSortes.getFirst();
		int min = getNbAssociations(s0);
		for(Sorte s: listeSortes)
		{
			if(s != this)
			{
				int i = getNbAssociations(s);
				if(i<min) min = i;
			}
		}
		return min;
	}
}
