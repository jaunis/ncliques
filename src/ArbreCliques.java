import java.security.InvalidParameterException;
import java.util.LinkedList;


public class ArbreCliques 
{
	//valeur du noeud
	protected Processus valeur = null;
	//liste des fils
	protected LinkedList<ArbreCliques> listeFils = new LinkedList<>();
	//hauteur: mis à jour uniquement pour la racine de l'arbre
	protected int hauteur = 0;
	protected LinkedList<ArbreCliques> listePeres = new LinkedList<>();
	protected ArbreCliques accepte;
	protected boolean nettoye1;
	protected boolean nettoye2;
	
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
		listePeres = a.getListePeres();
	}

	/**
	 * @return the listePeres
	 */
	public LinkedList<ArbreCliques> getListePeres() {
		return listePeres;
	}

	/**
	 * @param listePeres the listePeres to set
	 */
	public void setListePeres(LinkedList<ArbreCliques> listePeres) {
		this.listePeres = listePeres;
	}

	/**
	 * @return the valide
	 */
	public ArbreCliques getAccepte() {
		return accepte;
	}

	/**
	 * @param valide the valide to set
	 */
	public void setAccepte(ArbreCliques accepte) {
		this.accepte = accepte;
	}

	/**
	 * @return the nettoye1
	 */
	public boolean isNettoye1() {
		return nettoye1;
	}

	/**
	 * @param nettoye1 the nettoye1 to set
	 */
	public void setNettoye1(boolean nettoye1) {
		this.nettoye1 = nettoye1;
	}

	/**
	 * @return the nettoye2
	 */
	public boolean isNettoye2() {
		return nettoye2;
	}

	/**
	 * @param nettoye2 the nettoye2 to set
	 */
	public void setNettoye2(boolean nettoye2) {
		this.nettoye2 = nettoye2;
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
		ArbreCliques nouveau = new ArbreCliques(p);
		if(hauteur > 0)
		{
			for(ArbreCliques a: listeFils)
			{
				a.addProcessus(nouveau, hauteur - 1);
			}
			nettoyer1(nouveau);
			reset1();
		}
		else listeFils.add(nouveau);
	}
	
	/**
	 * fonction récursive ajoutant un processus dans l'arbre à une profondeur donnée,<br/>
	 * en vérifiant que le processus possède une association avec chaque noeud de la branche
	 * @param p
	 * @param profondeur
	 */
	protected void addProcessus(ArbreCliques nouveau, int profondeur)
	{
		/*
		 * si l'arbre est déjà "marqué" par nouveau (accepte == nouveau)
		 * alors tout ce qui suit a déjà été validé donc on s'arrête
		 * 
		 * sinon, si le processus est accepté, on marque l'arbre
		 * et on valide récursivement
		 */
		if((accepte != nouveau) && valeur.accepte(nouveau.getValeur()))
		{
			
			accepte = nouveau;
			if(profondeur == 0)
			{
				listeFils.add(nouveau);
				nouveau.getListePeres().add(this);
			}
			else
			{
				for(ArbreCliques a: listeFils)
				{
					a.addProcessus(nouveau, profondeur - 1);
				}
			}
			
		}
		
	}
	
	protected void nettoyer1(ArbreCliques nouveau)
	{
		if((!nettoye1 && this != nouveau && accepte == nouveau) || valeur == null)
		{
			if(listePeres.size() > 1)
			{
				LinkedList<ArbreCliques> listePeresValidant = new LinkedList<>();
				LinkedList<ArbreCliques> listePeresInvalidant = new LinkedList<>();
				for(ArbreCliques p: listePeres)
				{
					if(p.getAccepte() == nouveau) listePeresValidant.add(p);
					else listePeresInvalidant.add(p);
				}
				listePeres = listePeresValidant;
				ArbreCliques dupliquat = new ArbreCliques(this);
				dupliquat.setListePeres(listePeresInvalidant);
				for(ArbreCliques p: listePeresInvalidant)
				{
					p.getListeFils().remove(this);
					p.getListeFils().add(dupliquat);
				}
				dupliquat.supprimer(nouveau);
			}
			nettoye1 = true;
			if(!estFeuille())
			{
				for(ArbreCliques f: listeFils)
				{
					f.nettoyer1(nouveau);
				}
			}
		}
		
	}
	
	private void supprimer(ArbreCliques nouveau) 
	{
		if(!estFeuille())
		{
			listeFils.remove(nouveau);
			if(!estFeuille())
			{
				for(ArbreCliques f: listeFils)
				{
					f.supprimer(nouveau);
				}
			}
		}
	}

	/**
	 * supprime les branches de profondeur inférieure à la valeur passée en paramètre
	 * @param profondeur
	 */
	protected void nettoyer2(int profondeur)
	{
		if(!estFeuille() && !nettoye2)
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
				else a.nettoyer2(profondeur - 1);
			}
			int j=0;
			for(j=0; j<i; j++)
			{
				listeFils.remove(listeSuppression[j]);
			}
			nettoye2 = true;
		}
		
	}
	
	/**
	 * appelle nettoyer(int)
	 */
	public void nettoyer2()
	{
		nettoyer2(hauteur);
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
	
	/**
	 * met à false toutes les occurences de nettoye1 dans l'arbre
	 */
	public void reset1()
	{
		if(nettoye1 || valeur == null)
		{
			nettoye1 = false;
			for(ArbreCliques f: listeFils)
			{
				f.reset1();
			}
		}
	}
	/**
	 * met à false toutes les occurences de nettoye2 dans l'arbre
	 */
	public void reset2()
	{
		if(nettoye2 || valeur == null)
		{
			nettoye2 = false;
			for(ArbreCliques f: listeFils)
			{
				f.reset2();
			}
		}
	}
}
