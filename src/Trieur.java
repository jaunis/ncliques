import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 */

/**
 * @author Jean AUNIS
 *
 */
public class Trieur 
{
	/**
	 * inverse l'ordre de listeSortes
	 */
	public static void reverseSortes(LinkedList<Sorte> listeSortes)
	{
		Collections.reverse(listeSortes);
	}
		
	
	/**
	 * Trie la liste de Sortes par ordre aléatoire (sens="rand"), croissant (sens="asc")
	 * ou d�croissant (sens="desc")
	 * @param sens
	 * @return 
	 * @throws InvalidParameterException
	 */
	public static void trierSortes(String sens, LinkedList<Sorte> listeSortes) throws InvalidParameterException
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
	 * peut être appelée avec une LinkedList contenant une et une seule Sorte.<br/>
	 * La Sorte précitée doit avoir été supprimée de listeSortes.<br/>
	 * On insère dans la liste amorcée, les sortes présentes dans listeSortes<br/>
	 * en fonction du critère de tri.
	 * @param listeAmorcee
	 * @param critere
	 * @throws InvalidParameterException
	 */
	protected static void trierSortes2(LinkedList<Sorte> listeAmorcee, String critere, LinkedList<Sorte> listeSortes) throws InvalidParameterException
	{
		//liste des critères acceptés
		LinkedList<String> listeCriteres = new LinkedList<String>();
		listeCriteres.add("doubleMin");
		listeCriteres.add("moyenne");
		listeCriteres.add("simpleMin");
		listeCriteres.add("doubleMinLimite");
		if(listeAmorcee.size() != 1) throw new InvalidParameterException("La première sorte doit être préalablement insérée.");
		else
		{
			Sorte min;
			while(!listeSortes.isEmpty())
			{
				/*
				 * en fonction du critère, on appelle la fonction de recherche qui va bien
				 */
				if(critere.equals("doubleMin")) min = sorteAInsererDoubleMin(listeAmorcee, listeSortes);
				else if(critere.equals("moyenne")) min = sorteAInsererMoyenne(listeAmorcee, listeSortes);
				else if(critere.equals("simpleMin")) min = sorteAInsererSimpleMin(listeAmorcee.getFirst(), listeSortes);
				else if(critere.equals("doubleMinLimite")) min = sorteAInsererDoubleMinLimite(listeAmorcee, listeSortes);
				else throw new InvalidParameterException("Le critère de tri doit être égal à " + listeCriteres);
				/*
				 * on insère la sorte trouvée dans la liste amorcée, et on la retire de listeSortes
				 */
				listeAmorcee.add(min);
				listeSortes.remove(min);
			}
			/*
			 * on fonctionne par effet de bord, donc on recopie
			 * listeAmorcee dans listeSorte
			 */
			listeSortes.clear();
			int taille = listeAmorcee.size();
			for(int i = 0; i < taille; i++)
			{
				listeSortes.add(listeAmorcee.removeFirst());
			}
		}
	}
	
	
	/**
	 * La condition initiale détermine la façon dont est choisie la première sorte:<br/>
	 * <b>minSommeRelations</b> = elle possède la plus petite somme de relations<br/>
	 * <b>minNbProcessus</b> = elle possède le nombre minimum de processus<br/>
	 * <b>minMinNbRelations</b> = le nombre de relations minimum qu'elle a avec les autres sortes est<br/>
	 *  le plus petit de tout le graphe.<br/><br/>
	 *  
	 *  Le crit�re de tri détermine la façon dont sont insérées les sortes suivantes:<br/>
	 *  <b>doubleMin</b> = de toutes les sortes, celles qui a le minimum de relations avec une des<br/>
	 *  sortes déjà insérées<br/>
	 *  <b>moyenne</b> = de toutes les sortes, celle dont la moyenne du nombre de relations est la plus faible<br/>
	 *  <b>simpleMin</b> = de toutes les sortes, celle qui a le moins de relations avec le premier élément
	 * @param conditionInitiale : minSommeRelations, minNbProcessus, minMinNbRelations
	 * @param critereTri : doubleMin, moyenne, simpleMin
	 * @return 
	 * @throws InvalidParameterException
	 */
	public static void trierSortesOptimal(String conditionInitiale, String critereTri, LinkedList<Sorte> listeSortes) throws InvalidParameterException
	{
		LinkedList<String> listeCI = new LinkedList<String>();
		listeCI.add("minSommeRelations");
		listeCI.add("minNbProcessus");
		listeCI.add("minMinNbRelations");
		
		LinkedList<Sorte> listeTemp = new LinkedList<>();
		Sorte min;
		/*
		 * on utilise l'ordre "naturel" (défini avec Sorte.compareTo) pour déterminer le minimum
		 */
		if(conditionInitiale.equals("minSommeRelations")) min = Collections.min(listeSortes);
		else if(conditionInitiale.equals("minNbProcessus"))
		{
			/*
			 * création d'un comparateur pour rechercher le minimum
			 */
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
			/*
			 * création d'un comparateur pour rechercher le minimum
			 */
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
		else throw new InvalidParameterException("Le critère doit étre égal à " + listeCI);
		listeTemp.add(min);
		listeSortes.remove(min);
		/*
		 * la première sorte a été choisie (condition initiale), on trie ensuite la liste
		 * en fonction de ce premier élément
		 */
		trierSortes2(listeTemp, critereTri, listeSortes);
	}
	
	/**
	 * renvoie la sorte à insérer dans l'arbre, quand on utilise le critère du double minimum
	 * @param listeTemp
	 * @return
	 */
	protected static Sorte sorteAInsererDoubleMin(LinkedList<Sorte> listeTemp, LinkedList<Sorte> listeSortes) 
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
	 * renvoie la sorte à insérer dans l'arbre, quand on utilise le critère du double minimum,<br/>
	 * appliqué uniquement sur les premières sortes
	 * @param listeTemp
	 * @return
	 */
	protected static Sorte sorteAInsererDoubleMinLimite(LinkedList<Sorte> listeTemp, LinkedList<Sorte> listeSortes) 
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
	 * renvoie la sorte à insérer dans l'arbre, quand on utilise le critère du minimum simple
	 * @param sorte
	 * @return
	 */
	protected static Sorte sorteAInsererSimpleMin(Sorte sorte, LinkedList<Sorte> listeSortes) 
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
	 * renvoie la sorte à insérer dans l'arbre, quand on utilise le critère de la moyenne
	 * @param listeTemp
	 * @return
	 */
	protected static Sorte sorteAInsererMoyenne(LinkedList<Sorte> listeTemp, LinkedList<Sorte> listeSortes) 
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
}
