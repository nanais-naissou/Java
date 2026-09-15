package test;

import oop.runtime.EventPump;
import oop.tasks.Task;
import oop.utils.ByteOutputStream;
import oop.utils.ByteInputStream;
import oop.streams.Stream;

/* 
 * ce test simule un producteur et un consommateur avec des délais différent
 * le producteur envoie des octets toutes les 5ms et le consommateur les lit tous les 100ms
 *  a la fin on vérifie que 50 octets sont traité. */

public class TestProducerConsumer {
    public static void main(String[] args) {
        EventPump ep = new EventPump();

        Runnable bootTask = new Runnable() {//tache de démarrage
      @Override
        public void run() {
        Task task = Task.task();//rcupère la tache courante 

        ByteOutputStream os = new ByteOutputStream(10); // capacité 10 octets
        ByteInputStream is = new ByteInputStream(os);
      
     //listener pour le flux de sortie
          os.set(new Stream.Listener() {
      @Override
            public void available(Stream s) {
             System.out.println("Listener: output stream available"); 
            }
         @Override
            public void closed(Stream s) {
             System.out.println("Listener: output streem closed"); 
            }
          });
      
     //listener pour le flux d'entrée
       is.set(new Stream.Listener() {
          @Override
         public void available(Stream s) {
            System.out.println("Listener: input stream available");
         }
         @Override
       public void closed(Stream s) {
         System.out.println("Listener: input stream closed");
       }
       });
      
   // tache du producteur qui envoi des octets
      task.post(new Runnable() {
      int i = 0;
        @Override
       public void run() {
           if(i < 50){  
             if(os.available()){
         os.write((byte)i);
               System.out.println("Prodcuced:" + i); 
                  i++;
             }
           task.post(this, 5);
           } else {
              os.close();//on ferme le flux
          System.out.println("Producer: Stream closed.");
           }
         } });
      
    // tache du consommateur qui lit des octets dispo
         task.post(new Runnable() {
         int count = 0;
         @Override
         public void run() {
             while(is.available()){// tant qu'il y a des octets dispo

      byte b = is.read();
        System.out.println("Consumed:" + b);
             count++;}
      // si le flux est fermé et plus d'octets alors fin du test 
            if(is.closed() && !is.available()){
       System.out.println("Total consumed = " +count);
           if(count == 50){
       System.out.println("OK: Test 1 validé");
           } else {
       System.out.println("Test échoué: count <> 50");
           }
      // Terminer la tâche et arrêter l'EventPump
       task.terminate();
         ep.shutdown();
            } else {
           task.post(this, 100); //sinon on replanifie la lecture après 100ms
            } }
         });
        }};
  
  // on démarre la pompe a evenements avec la tache bootTask
       ep.boot(bootTask);
    }
}
