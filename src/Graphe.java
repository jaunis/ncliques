import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Graphe {

	protected LinkedList<Sorte> listeSortes = new LinkedList<Sorte>();
	/*
	 * contient la liste des cliques sous forme d'arbre
	 * (nombre de cliques = nombre de feuilles)
	 */
	protected ArbreCliques arbre = new ArbreCliques();
	protected LinkedList<Long> utilMemoire = new LinkedList<>();
		
	/**
	 * @return the utilMemoire
	 */
	public LinkedList<Long> getUtilMemoire() {
		return utilMemoire;
	}
	/**
	 * bons résultats avec moyenne
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Graphe g = new Graphe();
		g.chargerGraphe("src/graphes/egfr20_flat.ph");
		
		System.out.println("Graphe chargé. Calcul du HitlessGraph...");
		g.getHitlessGraph();
		
		System.out.println("HitlessGraph calculé. Nettoyage...");
		g.nettoyerGraphe();
		
		System.out.println("HitlessGraph nettoyé. Suppression des listes de frappes...");
		g.supprimerHits();
		
		System.out.println("Frappes supprimées. Tri...");
		//Trieur.trierSortes("rand", g.getListeSortes());
		Trieur.trierSortesOptimal("minSommeRelations", "moyenne", g.getListeSortes());
		System.out.println("Tri effectué. Recherche des n-cliques...");
		Date datedeb = new Date();
		g.rechercherCliques();
		Date datefin = new Date();
		long duree = datefin.getTime() - datedeb.getTime();
		System.out.println("cliques trouvées en: " + duree);
		long max = Collections.max(g.getUtilMemoire());
		System.out.println("Mémoire utilisée: " + max);
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
	 * @return the arbre
	 */
	public ArbreCliques getArbre() {
		return arbre;
	}
	/**
	 * @param arbre the arbre to set
	 */
	public void setArbre(ArbreCliques arbre) {
		this.arbre = arbre;
	}
	/**
	 * affiche l'arbre sous la forme suivante:<br/>
	 * nom_sorte{noms_processus}<br/>
	 * nom_processus:<br/>
	 * liste_associations
	 */
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
				{
					res += "  " + a.toString() + "\n";
				}
			}
		}
		
		return res;
	}
	/**
	 * affiche la liste des cliques (1 clique par ligne)
	 * @return
	 */
	public String afficherCliques()
	{
		return arbre.toString();
	}
	
	/**
	 * supprime du HitlessGraph les Processus n'ayant pas une association avec chaque Sorte<br/>
	 * <b>Pr�requis: </b> Le HitlessGraph a d�j� �t� calcul�
	 */
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
	/**
	 * Calcule la liste des cliques<br/>
	 * <b>Pr�requis:</b> le HitlessGraph a d�j� �t� nettoy�
	 */
	public void rechercherCliques()
	{
		rechercherCliques(new LinkedList<Sorte>(this.listeSortes));
	}
	
	/**
	 * m�thode appel�e par par rechercherCliques()
	 * @param liste
	 */
	protected void rechercherCliques(LinkedList<Sorte> liste)
	{
		if(!liste.isEmpty())
		{
			
			utilMemoire.add(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed());
			Sorte first = liste.removeFirst();
			ajouterSorte(first);
			//System.out.println(liste.size());
			rechercherCliques(liste);	
		}
		
	}
	/**
	 * ajoute les Processus d'une Sorte dans l'arbre des cliques
	 * @param s
	 */
	public void ajouterSorte(Sorte s)
	{
		for(Processus p: s.getListeProcessus())
		{
			arbre.addProcessus(p);
		}
		arbre.setHauteur(arbre.getHauteur() + 1);
		arbre.nettoyer();
	}
	
	/**
	 * charge le graphe contenu dans le fichier .ph pass� en param�tre
	 * @param nomFichier
	 */
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
			//expression régulière pour obtenir la liste des sortes
			Pattern p1 = Pattern.compile("^process\\s(.*)\\s(\\d*)$");
			//expression régulière pour obtenir les frappes de processus
			Pattern p2 = Pattern.compile("^(.*)\\s(\\d*)\\s->\\s([^\\s]*)\\s(\\d*)\\s(\\d*)");
			while ((ligne=br.readLine())!=null)
			{
				Matcher m1 = p1.matcher(ligne.trim());
				Matcher m2 = p2.matcher(ligne.trim());
				//m1 correspond, on crée une nouvelle sorte
				if(m1.find())
				{
					String nom = m1.group(1);
					int taille = Integer.parseInt(m1.group(2)) + 1;
					listeSortes.add(new Sorte(nom, this, taille));
				}
				//m2 correspond, on crée une nouvelle frappe
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
	/**
	 * récupère une Sorte à l'aide de son nom
	 * @param nom
	 * @return
	 */
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
	/**
	 * Calcule le HitlessGraph à partir de la liste des frappes
	 */
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
				while(it.hasNext())
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
	 * supprime les listes de frappe pour lib�rer de la mémoire
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
	
	/**
	 * appelle la fonction de recherche en diviser pour régner.<br/>
	 * méthode <b>très lente</b> (la fusion de deux arbres est extrêmement lente)
	 */
	public void rechercherCliquesDPR()
	{
		arbre = rechercherCliquesDPR(new LinkedList<Sorte>(listeSortes));
	}
	
	/**
	 * algo. de recherche des cliques, de type diviser pour régner
	 * @param liste
	 * @return
	 */
	protected ArbreCliques rechercherCliquesDPR(LinkedList<Sorte> liste)
	{
		/*
		 * cas de base
		 * la liste des cliques est égale à la liste des procesus de la sorte
		 */
		if(liste.size() == 1)
		{
			ArbreCliques a = new ArbreCliques();
			for(Processus p: liste.getFirst().getListeProcessus())
			{
				a.addProcessus(p);
			}
			a.setHauteur(1);
			return a;
		}
		else
		{
			/*
			 * comparateur utilisé pour choisir une sorte de référence
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
			Sorte s1 = Collections.min(liste, comp);
			liste.remove(s1);
			Sorte associe;
			LinkedList<Sorte> liste1 = new LinkedList<>(), liste2 = new LinkedList<>();
			liste1.add(s1);
			/*
			 * on a sélectionné une sorte de référence
			 * si la liste est assez grande, on lui associe la sorte
			 * qui a le moins de relations avec elle
			 */
			if(liste.size() > 1)
			{
				associe = liste.getFirst();
				int minLocal = s1.getNbAssociations(associe);
				for(Sorte s: liste)
				{
					int i = s1.getNbAssociations(s);
					if(i < minLocal)
					{
						minLocal = i;
						associe = s;
					}
				}
				liste.remove(associe);
				liste1.add(associe);
			}
			/*
			 * deuxième sorte de référence
			 */
			Sorte s2 = Collections.min(liste, comp);
			liste.remove(s2);
			liste2.add(s2);
			/*
			 * on a sélectionné une deuxième sorte de référence
			 * si la liste est assez grande, on lui associe la sorte
			 * qui a le moins de relations avec elle
			 */
			if(liste.size()>0)
			{
				associe = liste.getFirst();
				int minLocal = s2.getNbAssociations(associe);
				for(Sorte s: liste)
				{
					int i = s2.getNbAssociations(s);
					if(i < minLocal)
					{
						minLocal = i;
						associe = s;
					}
				}
				liste.remove(associe);
				liste2.add(associe);
			}
			
			/*
			 * en commentaire ci-dessous, une autre solution pour initialiser les 2 listes
			 */
			
			/*Sorte s1 = Collections.min(liste);
			liste.remove(s1);
			Sorte s2 = Collections.min(liste);
			liste.remove(s2);*/
			
			/*LinkedList<Sorte> liste1 = new LinkedList<Sorte>(), liste2 = new LinkedList<Sorte>();
			liste1.add(s1);
			liste2.add(s2);*/
			
			/*
			 * ensuite, on partage la liste des sortes selon qu'elles ont plus de relations
			 * avec s1 ou s2
			 */
			for(Sorte s: liste)
			{
				if(s.getNbAssociations(s1) < s.getNbAssociations(s2)) liste1.add(s);
				else liste2.add(s);
				
			}
			/*
			 * on recherche récursivement les cliques dans les deux listes créées
			 */
			ArbreCliques a1 = rechercherCliquesDPR(liste1);
			ArbreCliques a2 = rechercherCliquesDPR(liste2);
			/*
			 * on fusionne les deux arbres obtenus
			 */
			a1.fusionner(a2);
			a1.setHauteur(a1.getHauteur() + a2.getHauteur());
			/*
			 * on nettoie: suppression des cliques trop petites
			 */
			a1.nettoyer();
			return a1;
		}
		
	}
}
