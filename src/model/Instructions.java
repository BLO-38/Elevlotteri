package model;

public class Instructions {
    /*
    String[] lotteryModes = {"Lotteri med alla","Prioriterat lotteri","Slumpmässig belöning","Kontrollfrågor","Bordsplacering","Gruppindelning"};
     */
    private static final String[] infos = new String[7];
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
            """;

    }

    public static String getInfo(int index) {
        return infos[index];
    }

}
