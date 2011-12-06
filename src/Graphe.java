import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Graphe {

	protected LinkedList<Sorte> listeSortes = new LinkedList<Sorte>();
	protected LinkedList<Clique> listeCliques = new LinkedList<Clique>();
		
	public static void main(String[] args) 
	{
		Graphe g = new Graphe();
		g.chargerGraphe("src/graphes/tcrsig40_flat.ph");
		System.out.println("Graphe charg�. Calcul du HitlessGraph...");
		g.getHitlessGraph();
		System.out.println("HitlessGraph calcul�. Nettoyage...");
		g.nettoyerGraphe();
		System.out.println("HitlessGraph nettoyé. Suppression des listes de frappes...");
		g.supprimerHits();
		//System.out.println(g);
		System.out.println("Frappes supprimées. Tri...");
		g.trierSortes("rand");
		g.trierSortesOptimal("minMinNbRelations", "doubleMin");
		g.reverseSortes();
		System.out.println("Tri effectué. Recherche des n-cliques...");
		Date datedeb = new Date();
		g.rechercherCliques();
		Date datefin = new Date();
		long duree = datefin.getTime() - datedeb.getTime();
		System.out.println("cliques trouv�es en: " + duree);
		System.out.println(g.afficherCliques());
	}
	/**
	 * @return the listeSortes
	 */
	public LinkedList<Sorte> getListeSortes() {
		return listeSortes;
	}
	/**
	 * @param listeSortes the listeSortes to set
	 */
	public void setListeSortes(LinkedList<Sorte> listeSortes) {
		this.listeSortes = listeSortes;
	}
	/**
	 * @return the listeCliques
	 */
	public LinkedList<Clique> getListeCliques() {
		return listeCliques;
	}
	/**
	 * @param listeCliques the listeCliques to set
	 */
	public void setListeCliques(LinkedList<Clique> listeCliques) {
		this.listeCliques = listeCliques;
	}
	
	public String toString()
	{
		String res = "";
		for(Sorte s: listeSortes)
		{
			res += s.toString() + "\n";
			for(Processus p: s.getListeProcessus())
			{
				res += p.toString() + " :\n";
				for(Processus a: p.getListeAssociations())
				//for(Processus h: p.getListeHits())
				{
					res += "  " + a.toString() + "\n";
				}
			}
		}
		
		return res;
	}
	
	public String afficherCliques()
	{
		String res = "";
		for(Clique c: listeCliques)
		{
			res += c.toString() + "\n";
		}
		return res;
	}
	
	public void nettoyerGraphe()
	{
		boolean cont = true;
		while(cont)
		{
			cont = false;
			for(Sorte s: listeSortes)
			{
				cont = cont || s.nettoyerSorte();
			}
		}
		
	}
	
	public void rechercherCliques()
	{
		rechercherCliques(new LinkedList<Sorte>(this.listeSortes));
	}
	public void rechercherCliques(LinkedList<Sorte> liste)
	{
		if(liste.size() == 1)
		{
			for(Processus p: liste.getFirst().getListeProcessus())
			{
				LinkedList<Processus> l = new LinkedList<Processus>();
				l.add(p);
				listeCliques.add(new Clique(l));
			}
			
		}
		else
		{
			Sorte first = liste.removeFirst();
			rechercherCliques(liste);
			ajouterSorte(first);
		}
	}
	public void ajouterSorte(Sorte s)
	{
		//System.out.println(listeCliques.size());
		LinkedList<Clique> listeTemp = new LinkedList<Clique>();
		for(Processus p1: s.getListeProcessus())
		{
			for(Clique c: listeCliques)
			{
				boolean convient = true;
				for(Processus p2: c.getListeProcessus())
				{
					convient = convient && p2.getListeAssociations().contains(p1);
				}
				if(convient)
				{
					Clique cliqueAugmentee = new Clique(c);
					cliqueAugmentee.addProcessus(p1);
					listeTemp.add(cliqueAugmentee);
				}
			}
		}
		this.listeCliques = listeTemp;
	}
	public void chargerGraphe(String nomFichier)
	{
		/*try
		{
			InputStream ips=new FileInputStream(nomFichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while (!(ligne=br.readLine()).equals(""))
			{
				String nom = ligne.substring(0,1);
				int taille = Integer.parseInt(ligne.substring(2, 3));
				listeSortes.add(new Sorte(nom, this, taille));
			}
			while((ligne=br.readLine()) != null)
			{
				Sorte s1 = getSorteByNom(ligne.substring(0, 1));
				Sorte s2 = getSorteByNom(ligne.substring(4, 5));
				int numero1 = Integer.parseInt(ligne.substring(2, 3));
				int numero2 = Integer.parseInt(ligne.substring(6, 7));
				Processus p1 = s1.getProcessusByNumero(numero1);
				Processus p2 = s2.getProcessusByNumero(numero2);
				p1.addAssociation(p2);
				p2.addAssociation(p1);
			}
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}*/
		try
		{
			InputStream ips=new FileInputStream(nomFichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			Pattern p1 = Pattern.compile("^process\\s(.*)\\s(\\d*)$");
			Pattern p2 = Pattern.compile("^(.*)\\s(\\d*)\\s->\\s([^\\s]*)\\s(\\d*)\\s(\\d*)");
			while ((ligne=br.readLine())!=null)
			{
				Matcher m1 = p1.matcher(ligne.trim());
				Matcher m2 = p2.matcher(ligne.trim());
				if(m1.find())
				{
					String nom = m1.group(1);
					int taille = Integer.parseInt(m1.group(2)) + 1;
					listeSortes.add(new Sorte(nom, this, taille));
				}
				else if(m2.find())
				{
					String nom1 = m2.group(1);
					Sorte s1 = this.getSorteByNom(nom1);
					int numero1 = Integer.parseInt(m2.group(2));
					Processus proc1 = s1.getProcessusByNumero(numero1);
					
					String nom2 = m2.group(3);
					Sorte s2 = this.getSorteByNom(nom2);
					int numero2 = Integer.parseInt(m2.group(4));
					Processus proc2 = s2.getProcessusByNumero(numero2);
					
					proc1.addHit(proc2);
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public Sorte getSorteByNom(String nom)
	{
		boolean cont = true;
		int i=0;
		try
		{
			while(cont)
			{
				Sorte s = listeSortes.get(i);
				if(s.getNom().equals(nom)) cont = false;
				i++;
			}
			return listeSortes.get(i-1);
		}
		catch(IndexOutOfBoundsException e)
		{
			System.err.println("Nom: " + nom);
			e.printStackTrace();
			return null;
		}
	}
	
	public void getHitlessGraph()
	{
		int i=0;
		for(Sorte s1: listeSortes)
		{
			for(Processus p1: s1.getListeProcessus())
			{
				Iterator<Sorte> it = listeSortes.iterator();
				/*
				 * positionnement de l'it�rateur � l'indice i+1
				 */
				for(int j=0;j<i+1;j++)
				{
					it.next();
				}
				for(int j=i+1;j<listeSortes.size();j++)
				{
					Sorte s2 = it.next();
					if(s2 != s1)
					{
						for(Processus p2: s2.getListeProcessus())
						{
							
							if(!(p1.getListeHits().contains(p2)||p2.getListeHits().contains(p1)))
							{
								p1.addAssociation(p2);
								p2.addAssociation(p1);
							}
						}
					}
				}
			}
			i++;
		}
	}
	
	/**
	 * inverse l'ordre de listeSortes
	 */
	public void reverseSortes()
	{
		Collections.reverse(listeSortes);
	}
		
	
	/**
	 * Trie la liste de Sortes par ordre al�atoire (sens="rand"), croissant (sens="asc")
	 * ou d�croissant (sens="desc")
	 * @param sens
	 * @throws InvalidParameterException
	 */
	public void trierSortes(String sens) throws InvalidParameterException
	{
		if(!(sens.equals("asc")||sens.equals("desc")||sens.equals("rand"))) throw new InvalidParameterException("Entrez \"rand\", \"asc\" ou \"desc\" en param�tre.");
		if(sens.equals("rand")) Collections.shuffle(listeSortes);
		else
		{
			Collections.sort(listeSortes);
			if(sens.equals("desc")) Collections.reverse(listeSortes);
		}
	}
	
	/**
	 * peut �tre appel�e avec une LinkedList contenant une et une seule Sorte.<br/>
	 * La Sorte pr�cit�e doit avoir �t� supprim�e de listeSortes.
	 * @param listeAmorcee
	 * @param critere
	 * @throws InvalidParameterException
	 */
	protected void trierSortes2(LinkedList<Sorte> listeAmorcee, String critere) throws InvalidParameterException
	{
		LinkedList<String> listeCriteres = new LinkedList<String>();
		listeCriteres.add("doubleMin");
		listeCriteres.add("moyenne");
		listeCriteres.add("simpleMin");
		listeCriteres.add("doubleMinLimite");
		if(listeAmorcee.size() != 1) throw new InvalidParameterException("La premi�re sorte doit �tre pr�alablement ins�r�e.");
		else
		{
			Sorte min;
			while(!listeSortes.isEmpty())
			{
				if(critere.equals("doubleMin")) min = sorteAInsererDoubleMin(listeAmorcee);
				else if(critere.equals("moyenne")) min = sorteAInsererMoyenne(listeAmorcee);
				else if(critere.equals("simpleMin")) min = sorteAInsererSimpleMin(listeAmorcee.getFirst());
				else if(critere.equals("doubleMinLimite")) min = sorteAInsererDoubleMinLimite(listeAmorcee);
				else throw new InvalidParameterException("Le crit�re de tri doit �tre �gal � " + listeCriteres);
				listeAmorcee.add(min);
				listeSortes.remove(min);
			}
			listeSortes = listeAmorcee;
		}
	}
	
	
	/**
	 * La condition initiale d�termine la fa�on dont est choisie la premi�re sorte:<br/>
	 * <b>minSommeRelations</b> = elle poss�de la plus petite somme de relations<br/>
	 * <b>minNbProcessus</b> = elle poss�de le nombre minimum de processus<br/>
	 * <b>minMinNbRelations</b> = le nombre de relations minimum qu'elle a avec les autres sortes est<br/>
	 *  le plus petit de tout le graphe.<br/><br/>
	 *  
	 *  Le crit�re de tri d�termine la fa�on dont sont ins�r�es les sortes suivantes:<br/>
	 *  <b>doubleMin</b> = de toutes les sortes, celles qui a le minimum de relations avec une des<br/>
	 *  sortes d�j� ins�r�es<br/>
	 *  <b>moyenne</b> = de toutes les sortes, celle dont la moyenne du nombre de relations est la plus faible<br/>
	 *  <b>simpleMin</b> = de toutes les sortes, celle qui a le moins de relations avec le premier �l�ment
	 * @param conditionInitiale : minSommeRelations, minNbProcessus, minMinNbRelations
	 * @param critereTri : doubleMin, moyenne, simpleMin
	 * @throws InvalidParameterException
	 */
	public void trierSortesOptimal(String conditionInitiale, String critereTri) throws InvalidParameterException
	{
		LinkedList<String> listeCI = new LinkedList<String>();
		listeCI.add("minSommeRelations");
		listeCI.add("minNbProcessus");
		listeCI.add("minMinNbRelations");
		
		LinkedList<Sorte> listeTemp = new LinkedList<Sorte>();
		Sorte min;
		if(conditionInitiale.equals("minSommeRelations")) min = Collections.min(listeSortes);
		else if(conditionInitiale.equals("minNbProcessus"))
		{
			Comparator<Sorte> comp = new Comparator<Sorte>()
			{
				@Override
				public int compare(Sorte s1, Sorte s2) 
				{
					if(s1.getListeProcessus().size()==s2.getListeProcessus().size()) return 0;
					else if(s1.getListeProcessus().size()<s2.getListeProcessus().size()) return -1;
					else return 1;
				}
				
			};
			min = Collections.min(listeSortes, comp);
		}
		else if(conditionInitiale.equals("minMinNbRelations"))
		{
			Comparator<Sorte> comp = new Comparator<Sorte>()
			{
				@Override
				public int compare(Sorte s1, Sorte s2) 
				{
					if(s1.getNbAssociationsMin()==s2.getNbAssociationsMin()) return 0;
					else if(s1.getNbAssociationsMin()<s2.getNbAssociationsMin()) return -1;
					else return 1;
				}
				
			};
			min = Collections.min(listeSortes, comp);
		}
		else throw new InvalidParameterException("Le crit�re doit �tre �gal � " + listeCI);
		listeTemp.add(min);
		listeSortes.remove(min);
		trierSortes2(listeTemp, critereTri);
	}
	
	/**
	 * renvoie la sorte � ins�rer dans l'arbre, quand onutilise le crit�re du double minimum
	 * @param listeTemp
	 * @return
	 */
	protected Sorte sorteAInsererDoubleMin(LinkedList<Sorte> listeTemp) 
	{
		Sorte res = listeSortes.getFirst();
		int min = res.getNbAssociations(listeTemp.getFirst());
		for(Sorte s: listeSortes)
		{
			int minLocal = s.getNbAssociations(listeTemp.getFirst());
			for(Sorte s2: listeTemp)
			{
				if(minLocal > s.getNbAssociations(s2)) minLocal = s.getNbAssociations(s2);
			}
			if(minLocal == min)
			{
				if(s.getTotalAssociations() < res.getTotalAssociations()) res = s;
			}
			else if(minLocal<min)
			{
				min=minLocal;
				res = s;
			}
		}
		return res;
	}
	
	/**
	 * renvoie la sorte � ins�rer dans l'arbre, quand onutilise le crit�re du double minimum,<br/>
	 * appliqu� uniquement sur les premi�res sortes
	 * @param listeTemp
	 * @return
	 */
	protected Sorte sorteAInsererDoubleMinLimite(LinkedList<Sorte> listeTemp) 
	{
		int limite = Math.min(listeTemp.size(), (listeTemp.size() + listeSortes.size())/5);
		Sorte res = listeSortes.getFirst();
		int min = res.getNbAssociations(listeTemp.getFirst());
		for(Sorte s: listeSortes)
		{
			int minLocal = s.getNbAssociations(listeTemp.getFirst());
			Iterator<Sorte> i = listeTemp.iterator();
			int j = 0;
			while(j < limite)
			{
				Sorte s2 = i.next();
				if(minLocal > s.getNbAssociations(s2)) minLocal = s.getNbAssociations(s2);
				j++;
			}
			if(minLocal == min)
			{
				if(s.getTotalAssociations() < res.getTotalAssociations()) res = s;
			}
			else if(minLocal<min)
			{
				min=minLocal;
				res = s;
			}
		}
		return res;
	}
	
	/**
	 * renvoie la sorte � ins�rer dans l'arbre, quand onutilise le crit�re du minimum simple
	 * @param sorte
	 * @return
	 */
	protected Sorte sorteAInsererSimpleMin(Sorte sorte) 
	{
		Sorte res = listeSortes.getFirst();
		int min = res.getNbAssociations(sorte);
		for(Sorte s: listeSortes)
		{
			int nb = s.getNbAssociations(sorte);
			if(nb == min)
			{
				if(s.getTotalAssociations() < res.getTotalAssociations()) res = s;
			}
			else if(nb<min)
			{
				min=nb;
				res = s;
			}
		}
		return res;
	}
	
	/**
	 * renvoie la sorte � ins�rer dans l'arbre, quand onutilise le crit�re de la moyenne
	 * @param listeTemp
	 * @return
	 */
	protected Sorte sorteAInsererMoyenne(LinkedList<Sorte> listeTemp) 
	{
		Sorte res = listeSortes.getFirst();
		float min = res.getTotalAssociations();
		for(Sorte s: listeSortes)
		{
			float moyenne = 0;
			for(Sorte s2: listeTemp)
			{
				moyenne += s2.getNbAssociations(s);
			}
			moyenne = moyenne / listeTemp.size();
			if(moyenne == min)
			{
				if(s.getTotalAssociations() < res.getTotalAssociations()) res = s;
			}
			else if(moyenne<min)
			{
				min=moyenne;
				res = s;
			}
		}
		return res;
	}
	/**
	 * supprime les listes de frappe pour lib�rer de la m�moire
	 */
	public void supprimerHits()
	{
		for(Sorte s: listeSortes)
		{
			for(Processus p: s.getListeProcessus())
			{
				p.setListeHits(null);
			}
		}
	}
}
