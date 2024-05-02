package model;

public class Instructions {
    /*
	String[] lotteryModes = {"Lotteri med alla", "Prioriterat lotteri", "Slumpmässig belöning", "Kontrollfrågor", "Bordsplacering", "Gruppindelning alla", "Utse elevgrupp","Offlinekontroll"};
                                0                       1                       2                       3               4                   5                       6               7
     */
    private static final String[] infos = new String[8];
    static {
        // Lotteri med alla
        infos[0] = """
            Alla elever i klassen deltar i lotteriet.
            Du kan dock välja bort namn i nästa steg innan lotteriet.
            Ingen statistik sparas.
            """;
        // Prioriterat lotteri
        infos[1] = """
            De elever som inte är bortvalda deltar.
            Du kan välja att en elev aldrig deltar under "Klasser och elever".
            Där kan du också se antal gånger en elev blivit lottad.
            Varje lotteri prioriteras elever i den ordning de blivit lottade,
            i stigande skala. Dvs de som blivit lottade minst antal gånger kommer först.
            """;
        // Slumpmässig belöning
        infos[2] = """
            En slumpmässig elev lottas fram.
            När en elev fått belöning kommer inte det namnet igen förrän alla fått.
            """;
        // Kontrollfrågor
        infos[3] = """
            Används när man till exempel vill kolla om de gjort läxan.
            Man kan ställa in så en viss elev aldrig blir lottad.
            Ett namn i taget lottas fram. Elevens resultat sparas så här:
            Knappen "Nästa" är indelad i tre lika stora osynliga delar.
            Klick till vänster: Eleven svarade rätt.
            Klick i mitten: Eleven var frånvarande
            Klick till höger: Eleven svarade fel.
            De med minst antal rätt kommer först i lotteriet.
            """;
        // Bordsplacering
        infos[4] = """
            Ja denna fixar helt enkelt en placering av elever i klassrummet.
            Man kan göra en ny eller hämta en gammal som man sparat.
            OBS "Spara BG" betuýder spara bänkgrannar. Om du klickar på den sparas vilka alla sitter bredvid
            (ej med gångväg emellan såklart). Statistik kan ses under "Klasser och elever".
            """;
        // Gruppindelning
        infos[5] = """
            Gruppindelning för tex labbar och annat grupparbete.
            """;
        infos[6] = """
            Används för att lotta fram en enstaka grupp elever som ska göra något.
            Man kan spara vilka som valdes för att se till att det blir andra elever nästa gång.
            När alla blivit valda startar det om med alla.
            Vill man få lite mer effektfull (nåja...) lottning kan man välja "Starta med knapp" och minimera alla andra fönster.
            """;
        infos[7] = """
            Med denna kan man övervaka att alla elever har stängt av sitt wifi.
            Varje elev måste då först ladda ner ett litet program som de startar.
            Kan vara bra vid till exempel prov eller andra tillfällen då de ska använda dator
            utan att ha tillgång till internet. 
            """;

    }

    public static String getInfo(int index) {
        return infos[index];
    }

}
