package oop.utils;

public class Environment {

    private Variable[] variables;
  private int cpt;  // nb var stockees

    public Environment() {
variables = new Variable[10]; //objet de la classe Variable contenant deux hamps
      cpt = 0;
    }

    // renvoi tous les noms des vars
  public String[] listNames() {
    String[] NOMS = new String[cpt];
   for (int i = 0; i < cpt; i++) {
      NOMS[i] = variables[i].nom;
   }
    return NOMS;
  }

    // recupere la val d'une var, null si pas trouvee
  public String get(String name) {
    for (int i=0;i<cpt;i++){
    	if (variables[i].nom.compareTo(name) == 0) {
    	    return variables[i].valeur;
    	}
    }
    return null;
  }

    // supprime la var et retourne sa val, sinon null
  public String del(String name) {
    for (int i = 0; i < cpt; i++) {
    	if (variables[i].nom.compareTo(name) == 0) {
        String val = variables[i].valeur;
         for (int j = i + 1; j < cpt; j++) {
            variables[j - 1] = variables[j];
         }
        cpt--;
        return val;
      }
    }
    return null;
  }

    // cree ou modifie une var
  public void put(String name, String val) {
    // cherche si var existe return nada
    for (int i = 0; i < cpt; i++) {
    	if (variables[i].nom.compareTo(name) == 0) {
        variables[i].valeur = val;
        return;
      }
    }
    //le tableau est redimensionné quand il n'ya pas plus de place pour affecter une nouv var
    if(cpt == variables.length){
      Variable[] temp = new Variable[variables.length*2];
      for (int i = 0; i < cpt; i++) {
        temp[i] = variables[i];
      }
      variables = temp;
    }
    variables[cpt++] = new Variable(name, val);
  }

    // classe interne pour representer une var (vc nom de var + son contenu, un dictionnaire)
  public static class Variable {
        public String nom;
       public String valeur;
        public Variable(String name, String value) {
         this.nom = name;
          this.valeur = value;
        }
  }
}
