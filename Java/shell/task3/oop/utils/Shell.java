package oop.utils;

import oop.shell.IRegistry;
import oop.shell.IShell;
import oop.shell.ITerminal;

public class Shell implements IShell {
  ITerminal term; 
   String[] historique; //historique des commandes tout simplement
    int historyCpt = 0; //nb de commandes enregistres
  
  //la commande courante
    StringBuilder commande = new StringBuilder();
  int curseurPos = 0;
  
   // index histo, quand rien = historyCpt, pour naviagation commande
  int indexHistorique = 0;
  
  // env var
  Environment env = new Environment();
  String dollar = "$ ";
  
    // listener shell
  Listener shellListener;
  
   public static final int KEY_LEFT = 37;
  public static final int KEY_RIGHT = 39;
   public static final int KEY_UP = 38;
 public static final int KEY_DOWN = 40;
  public static final int KEY_BACKSPACE = 8;
   public static final int KEY_DELETE = 46;
  public static final int KEY_ENTER = 10;
  
  public Shell(int capacity){
    historique = new String[capacity];
    indexHistorique = historyCpt;
    // term est mis a jour plus via setTerminal()
  }
  
  public void setTerminal(ITerminal term){
    this.term = term;
    printDollar();
  }
  
  // affiche le prompt et met a jour le buffer edite
  private void printDollar(){
    commande.setLength(0);
    commande.append(dollar);
    if(term != null){
      int r = term.row();
      term.clear(r);
      for(char c : dollar.toCharArray()){
         term.insert(c); //on insert dans le termianl $
      }
    }
    curseurPos = dollar.length(); //la position du curseur est mtn apres le $
  }
  
  public void pressed(int keyCode, char keyChar){
    if(term==null)return;
    if(keyCode==KEY_LEFT){
      if(curseurPos > dollar.length()){
         curseurPos--;
         term.left();
      }
    } else if(keyCode==KEY_RIGHT){
      if(curseurPos < commande.length()){
         curseurPos++;
         term.right();
      }
    } else if(keyCode==KEY_BACKSPACE){
      if(curseurPos > dollar.length()){
         commande.deleteCharAt(curseurPos-1);
         curseurPos--;
          term.backspace();
      }
    } else if(keyCode==KEY_DELETE){
      if(curseurPos < commande.length()){
           commande.deleteCharAt(curseurPos); //supp dans l'env
         term.delete();   } //supp dans le terminal
    
    
      
      
      
    } else if(keyCode==KEY_ENTER){
    	    // On récupère la commande courante saisie par l'utilisateur (incluant le prompt)
    	    String commandeBrute = commande.toString();
    	    
    	    // Si la commande commence par le prompt, on le retire pour obtenir la commande pure
    	    if(commandeBrute.startsWith(dollar)){
    	         commandeBrute = commandeBrute.substring(dollar.length());
    	    }
    	    
    	    
    	    if(!commandeBrute.trim().equals("")){ // si la commande (apres suppression des espaces) n'est pas vide
    	         // on essaie d'exéc la commande en tant que commande built-in
    	         if(!executerCommande(commandeBrute)){
  
    	            if(shellListener != null){
    	               // on découpe la commande en tokens (commande et arguments)
    	               String[] parties = commandeBrute.split(" ");
    	               String cmd = parties[0];
    	               String[] args = new String[parties.length-1];
    	               for(int i=1;i<parties.length;i++){
    	                  args[i-1] = parties[i];
    	               }
    	               // on notifie le listener que la commande est entrée
    	               shellListener.entered(cmd, args);
    	            }
    	         }
    	         // on stock la commande dans l'historique
    	         if(historyCpt < historique.length){
    	            historique[historyCpt++] = commandeBrute;
    	         } else {
    	            //si l'historique est plein, je décale les cmds pour faire de la place
    	            for(int i=1;i<historique.length;i++){
    	               historique[i-1] = historique[i];
    	            }
    	            historique[historique.length-1] = commandeBrute;
    	         }
    	    }
    	    //ligne suivante 
    	    term.enter();
    	    commande.setLength(0);
    	    indexHistorique = historyCpt;
    	    printDollar();
    	}

    
    
    
    //on vérifie que l'historique n'est pas vide si possible on décrémente indexHistorique pour remonter dans l'historique
    //la commande correspondante est rappelée, le buffer est reset
    else if(keyCode==KEY_UP){
      if(historyCpt > 0){
         if(indexHistorique > 0)
            indexHistorique--;
         String commandeChoisie = historique[indexHistorique];
         commande.setLength(0);
         commande.append(dollar).append(commandeChoisie);
         curseurPos = commande.length();
         int r = term.row();
         term.clear(r);
         for(int i=0;i<commande.length();i++){
            term.insert(commande.charAt(i));
         }}
    } 
    
    
    
    //vice versa
    
    else if(keyCode==KEY_DOWN){
      if(historyCpt > 0){
         if(indexHistorique < historyCpt-1)
            indexHistorique++;
         else{
            commande.setLength(0);
            commande.append(dollar);
         }
         curseurPos = commande.length();
         int r = term.row();
         term.clear(r);
         for(int i=0;i<commande.length();i++){
            term.insert(commande.charAt(i));
         }
      }}}
  
  public void released(int keyCode, char keyChar){
  }
  
  public void typed(char keyChar){
    if(term==null)return;
    if(keyChar>=32){
      commande.insert(curseurPos, keyChar);
      curseurPos++;
      term.insert(keyChar);
    }
  }
  
  public String prompt(String prompt){ //pour modifer le prompt
    String old = dollar;
    dollar = prompt;
    return old;
  }
  
  public String prompt(){
    return dollar;
  }
  
  public String[] history(){
    String[] hist = new String[historyCpt];
    for(int i=0;i<historyCpt;i++){
       hist[i] = historique[i];
    }
    return hist;
  }
  
  public String line(){
    return commande.toString();
  }
  
  public String valueOf(String name){ //retourne le contenu d'une var de l'env
    return env.get(name);
  }
  
  public void set(Listener l){
    shellListener = l;
  }
  
  public void set(IRegistry reg){
  }
  
  // execute les commandes built-in
  // retourne true si command est built-in
  private boolean executerCommande(String command) {
    String[] parties = command.split(" ");
    String cmd = parties[0];
    //  efface l'ecran, mais pas l'histo
    if(cmd.equals("clear")){
      term.clear();
      return true;
    }
    //affiche l'historique
    else if(cmd.equals("history")){
    	  // pour chaque commande stock dans history,
    	  // on affiche [index] commande
    	  for(int i = 0; i < historyCpt; i++){
    	    term.enter();
    	    // construit la chaîne [i] ..
    	    String line = "[" + i + "] " + historique[i];
    	    // insere les carac
    	    for(char c : line.toCharArray()){
    	      term.insert(c);
    	    }
    	  }
    	  return true;
    	}

 
    // affiche les arguments en substituant les variables
    else if(cmd.equals("echo")){
      StringBuilder sb = new StringBuilder();
      for(int i=1;i<parties.length;i++){
        String arg = parties[i];
        if(arg.startsWith("$")){
          String varName = arg.substring(1);
          String val = env.get(varName);
          if(val == null)
            val = "";
          sb.append(val).append(" ");
        } else {
          sb.append(arg).append(" ");
        }
      }
      term.enter();
      for(char c : sb.toString().toCharArray()){
        term.insert(c);
      }
      return true;
    }
    // affiche toutes les variables d'env
    else if(cmd.equals("env")){
      String[] names = env.listNames();
      for(String n : names){
        String val = env.get(n);
        String line = n + " = " + val;
        term.enter();
        for(char c : line.toCharArray()){
          term.insert(c);
        }
      }
      return true;
    }
    // set name value...
    else if(cmd.equals("set")){
      if(parties.length >= 3){
        String varName = parties[1];
        StringBuilder value = new StringBuilder();
        for(int i=2;i<parties.length;i++){
          value.append(parties[i]).append(" ");
        }
        env.put(varName, value.toString().trim());
      }
      return true;
    }
    // unset
    else if(cmd.equals("unset")){
      for(int i=1;i<parties.length;i++){
        env.del(parties[i]);
      }
      return true;
    }
    return false; // commande non built in
  }
}
