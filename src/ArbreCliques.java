import java.security.InvalidParameterException;
import java.util.LinkedList;


public class ArbreCliques 
{
	//valeur du noeud
	protected Processus valeur = null;
	//liste des fils
	protected LinkedList<ArbreCliques> listeFils = new LinkedList<ArbreCliques>();
	//hauteur: mis � jour uniquement pour la racine de l'arbre
	protected int hauteur = 0;
	
	public ArbreCliques(Processus p) {
		this.valeur = p;
	}
	
	public ArbreCliques()
	{
		
	}
	public ArbreCliques(ArbreCliques a) {
		hauteur = a.getHauteur();
		valeur = a.getValeur();
		if(!a.estFeuille())
		{
			for(ArbreCliques f: a.getListeFils())
			{
				listeFils.add(new ArbreCliques(f));
			}
		}
	}

	/**
	 * @return the hauteur
	 */
	public int getHauteur() {
		return hauteur;
	}
	/**
	 * @param hauteur the hauteur to set
	 */
	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}
	/**
	 * @return the valeur
	 */
	public Processus getValeur() {
		return valeur;
	}
	/**
	 * @param valeur the valeur to set
	 */
	public void setValeur(Processus valeur) {
		this.valeur = valeur;
	}
	/**
	 * @return the listeFils
	 */
	public LinkedList<ArbreCliques> getListeFils() {
		return listeFils;
	}
	/**
	 * @param listeFils the listeFils to set
	 */
	public void setListeFils(LinkedList<ArbreCliques> listeFils) {
		this.listeFils = listeFils;
	}
	
	/**
	 * fonction r�cursive appel�e par toString()
	 * @param chaine
	 * @return
	 */
	protected String toString(String chaine)
	{
		if(estFeuille())
		{
			String res = chaine + ", " + valeur + "\n";
			return res.substring(2);
		}
		else
		{
			String local;
			if(valeur == null) local = chaine;
			else local = chaine + ", " + valeur;
			String res = "";
			for(ArbreCliques a: listeFils)
			{
				res += a.toString(local);
			}
			return res;
		}
	}
	
	/**
	 * renvoie la liste des cliques (1 clique par ligne)
	 */
	public String toString()
	{
		return toString("");
	}
	
	/**
	 * teste si l'�l�ment est une feuille
	 * @return
	 */
	public boolean estFeuille()
	{
		return listeFils.isEmpty();
	}
	
	/**
	 * fonction r�cursive calculant la profondeur maximum de l'arbre
	 * @return
	 */
	public int getProfondeurMax()
	{
		if(valeur==null && estFeuille()) return 0;
		else if(estFeuille()) return 1;
		else
		{
			int ajout = valeur == null? 0 : 1;
			int max = 0;
			int local;
			for(ArbreCliques a: listeFils)
			{
				local = a.getProfondeurMax();
				if(local>max) max = local;
			}
			return max + ajout;
		}
	}
	
	/**
	 * appelle addProcessus(Processus, int) pour ins�rer le processus au bon endroit dans l'arbre
	 * @param p
	 */
	public void addProcessus(Processus p)
	{
		if(hauteur > 0)
		{
			for(ArbreCliques a: listeFils)
			{
				a.addProcessus(p, hauteur - 1);
			}
		}
		else listeFils.add(new ArbreCliques(p));
	}
	
	/**
	 * fonction r�cursive ajoutant un processus dans l'arbre � une profondeur donn�e,<br/>
	 * en v�rifiant que le processus poss�de une association avec chaque noeud de la branche
	 * @param p
	 * @param profondeur
	 */
	protected void addProcessus(Processus p, int profondeur)
	{
		if(valeur.accepte(p))
		{
			if(profondeur == 0) listeFils.add(new ArbreCliques(p));
			else
			{
				for(ArbreCliques a: listeFils)
				{
					a.addProcessus(p, profondeur - 1);
				}
			}
		}
	}
	
	/**
	 * supprime les branches de profondeur inf�rieure � la valeur pass�e en param�tre
	 * @param profondeur
	 */
	protected void nettoyer(int profondeur)
	{
		if(!estFeuille())
		{
			ArbreCliques[] listeSuppression = new ArbreCliques[listeFils.size()];
			int i=0;
			for(ArbreCliques a: listeFils)
			{
				if(a.getProfondeurMax()<profondeur)
				{
					listeSuppression[i] = a;
					i++;
				}
				else a.nettoyer(profondeur - 1);
			}
			int j=0;
			for(j=0; j<i; j++)
			{
				listeFils.remove(listeSuppression[j]);
			}
		}
		
	}
	
	/**
	 * appelle nettoyer(int)
	 */
	public void nettoyer()
	{
		nettoyer(hauteur);
	}
	
	/**
	 * fusionner deux arbres, en supprimant les processus qui ne conviennent pas
	 * @param a
	 * @throws InvalidParameterException
	 */
	public void fusionner(ArbreCliques a) throws InvalidParameterException
	{
		if(a.getValeur()!=null) 
			throw new InvalidParameterException("Il faut passer un arbre complet en param�tre (racine nulle)");
		
		ArbreCliques copie = new ArbreCliques(a);
		boolean valide = true;
		if(valeur != null) valide = copie.accepte(valeur);
		if(valide)
		{
			if(estFeuille()) listeFils = copie.getListeFils();
			else
			{
				for(ArbreCliques f: listeFils)
				{
					f.fusionner(copie);
				}
			}
		}
	}
	
	/**
	 * teste si le processus pass� en param�tre peut �tre ins�r� dans l'arbre courant
	 * @param valeur2
	 * @return
	 * @throws InvalidParameterException
	 */
	protected boolean accepte(Processus valeur2) throws InvalidParameterException
	{
		if(valeur2 == null)
			throw new InvalidParameterException("Impossible de passer une valeur nulle en param�tre.");
		if(valeur == null && estFeuille()) return true;
		else if(estFeuille()) return (valeur.getListeAssociations().contains(valeur2));
		else
		{
			boolean b1 = true;
			if(valeur != null) b1 = valeur.getListeAssociations().contains(valeur2);
			if(!b1) return false;
			else
			{
				boolean res = false;
				LinkedList<ArbreCliques> listeSuppression = new LinkedList<ArbreCliques>();
				for(ArbreCliques a: listeFils)
				{
					if(a.accepte(valeur2)) res = true;
					else
					{
						listeSuppression.add(a);
						res |= false;
					}
				}
				for(ArbreCliques a: listeSuppression)
				{
					listeFils.remove(a);
				}
				return res;
			}
			
		}
	}
}
