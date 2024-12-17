package model;

import java.util.HashMap;
import java.util.LinkedList;

public class Instructions {

    private static final HashMap<String,String> infoMap = new HashMap<>();

    static {
        infoMap.put("Chokladhjul","""
            Ett lotterihjul för att lotta tex en belöning.
            När man startar en ny är senaste vinnaren borttagen.
            """);

        infoMap.put("Lotteri med alla","""        
            Alla elever i klassen deltar i lotteriet.
            Du kan dock välja bort namn i nästa steg innan lotteriet.
            Ingen statistik sparas.
            """);

        infoMap.put("Prioriterat lotteri","""        
            De elever som inte är bortvalda deltar.
            Du kan välja att en elev aldrig deltar under "Klasser och elever".
            Där kan du också se antal gånger en elev blivit lottad.
            Varje lotteri prioriteras elever i den ordning de blivit lottade,
            i stigande skala. Dvs de som blivit lottade minst antal gånger kommer först.
            """);

        infoMap.put("Prisutdelning","""        
            En slumpmässig elev lottas fram.
            När en elev fått belöning kommer inte det namnet igen förrän alla fått.
            """);

        infoMap.put("Kontrollfrågor","""        
            Används när man till exempel vill kolla om de gjort läxan.
            Man kan ställa in så en viss elev aldrig blir lottad.
            Ett namn i taget lottas fram. Elevens resultat sparas så här:
            Knappen "Nästa" är indelad i tre lika stora osynliga delar.
            Klick till vänster: Eleven svarade rätt.
            Klick i mitten: Eleven var frånvarande
            Klick till höger: Eleven svarade fel.
            De med minst antal rätt kommer först i lotteriet.
            """);

        infoMap.put("Bordsplacering","""        
            Ja denna fixar helt enkelt en placering av elever i klassrummet.
            Man kan göra en ny eller hämta en gammal som man sparat.
            OBS "Spara BG" betuýder spara bänkgrannar. Om du klickar på den sparas vilka alla sitter bredvid
            (ej med gångväg emellan såklart). Statistik kan ses under "Klasser och elever".
            """);

        infoMap.put("Gruppindelning alla","""        
            Gruppindelning för tex labbar och annat grupparbete.
            """);

        infoMap.put("Utse elevgrupp","""        
            Används för att lotta fram en enstaka grupp elever som ska göra något.
            Man kan spara vilka som valdes för att se till att det blir andra elever nästa gång.
            När alla blivit valda startar det om med alla.
            Vill man få lite mer effektfull (nåja...) lottning kan man välja "Starta med knapp" och minimera alla andra fönster.
            """);

        infoMap.put("Offlinekontroll","""        
            Med denna kan man övervaka att alla elever har stängt av sitt wifi.
            Varje elev måste då först ladda ner ett litet program som de startar.
            Kan vara bra vid till exempel prov eller andra tillfällen då de ska använda dator
            utan att ha tillgång till internet.
            """);

    }

    public static String getInfo(String chosenFeature) {
        return infoMap.get(chosenFeature);
    }

    public static LinkedList<String> getInstructionsNames() {
        return new LinkedList<>(infoMap.keySet());
    }


}
