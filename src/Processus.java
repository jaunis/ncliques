import java.util.Iterator;
import java.util.LinkedList;


public class Processus 
{
	protected LinkedList<Processus> listeAssociations = new LinkedList<Processus>();
	protected LinkedList<Processus> listeHits = new LinkedList<Processus>();
	protected Sorte sorte;
	protected int numero;
	protected Processus accepte;
	protected Processus rejette;
	
	/**
	 * @return the accepte
	 */
	public Processus getAccepte() {
		return accepte;
	}

	/**
	 * @param accepte the accepte to set
	 */
	public void setAccepte(Processus accepte) {
		this.accepte = accepte;
	}

	/**
	 * @return the rejette
	 */
	public Processus getRejette() {
		return rejette;
	}

	/**
	 * @param rejette the rejette to set
	 */
	public void setRejette(Processus rejette) {
		this.rejette = rejette;
	}

	/**
	 * @return the listeHits
	 */
	public LinkedList<Processus> getListeHits() {
		return listeHits;
	}

	/**
	 * @param listeHits the listeHits to set
	 */
	public void setListeHits(LinkedList<Processus> listeHits) {
		this.listeHits = listeHits;
	}

	public Processus(Sorte s, int i) 
	{
		this.numero = i;
		this.sorte = s;
	}

	public String toString()
	{
		return sorte.getNom() + " " + numero;
	}

	/**
	 * @return the listeAssociations
	 */
	public LinkedList<Processus> getListeAssociations() {
		return listeAssociations;
	}

	/**
	 * @param listeAssociations the listeAssociations to set
	 */
	public void setListeAssociations(LinkedList<Processus> listeAssociations) {
		this.listeAssociations = listeAssociations;
	}

	/**
	 * @return the sorte
	 */
	public Sorte getSorte() {
		return sorte;
	}

	/**
	 * @param sorte the sorte to set
	 */
	public void setSorte(Sorte sorte) {
		this.sorte = sorte;
	}

	/**
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	/**
	 * ajouter une association dans la liste des associations
	 * @param p
	 */
	public void addAssociation(Processus p)
	{
		listeAssociations.add(p);
	}
	
	/**
	 * ajouter une frappe dans la liste des frappes
	 * @param p
	 */
	public void addHit(Processus p)
	{
		listeHits.add(p);
	}
	
	/**
	 * teste si le processus a au moins un lien avec chacune des sortes
	 * @return
	 */
	public boolean estValide()
	{
		int tailleGraphe = sorte.getGraphe().getListeSortes().size();
		if(listeAssociations.size()<tailleGraphe-1) return false;
		else
		{
			LinkedList<Sorte> listeSortesLiees = new LinkedList<Sorte>();
			Iterator<Processus> i = listeAssociations.iterator();
			boolean cont=true;
			int taille = 0;
			while(cont)
			{
				Processus p = i.next();
				Sorte s = p.getSorte();
				if(!listeSortesLiees.contains(s))
				{
					listeSortesLiees.add(s);
					taille++;
					if(taille==tailleGraphe-1) cont=false;
				}
				cont &= i.hasNext();
			}
			return (taille==tailleGraphe - 1); 
		}
	}
	
	/**
	 * teste si le processus en cours est lié avec le processus passé en paramètre.<br/>
	 * (optimisé, de façon à ne pas faire un contains(p) à chaque fois.
	 * @param p
	 * @return
	 */
	public boolean accepte(Processus p)
	{
		if(accepte == p) return true;
		else if(rejette == p) return false;
		else
		{
			if(listeAssociations.contains(p))
			{
				accepte = p;
				return true;
			}
			else
			{
				rejette = p;
				return false;
			}
		}
	}
}
