package view;

import databasen.DatabaseHandler2;

import javax.swing.*;

/**
 * How to use:
 * new ClassChooser2(frame,response -> resp = response);
 * där resp är en medlemsvariabel
 */
public class ClassChooser2 extends MyChooser {
    public ClassChooser2(JFrame frame, ChooserListener asker) {
        super(DatabaseHandler2.getClasses(),frame,"Välj klass:",true,asker);
        //setColors(Color.WHITE, MainHandler.GRÖN, null);
        start();
    }

}
