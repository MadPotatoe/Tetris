
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alu20472135l
 */
public class ControllerBestScores {

    private File file;
    private int counter = 0;
    private BestScores[] bScoresArray;
    private String path = this.getClass().getResource("BestScores.txt").toString().substring(5);
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    public ControllerBestScores() throws IOException, FileNotFoundException, ClassNotFoundException {
        bScoresArray = new BestScores[3];
        file = new File(path);
        try {
            in = new ObjectInputStream(new FileInputStream(file));

        } catch (IOException ex) {
            System.err.println("IO Error");
        }
        readFile();
    }

    public void put(BestScores score) throws IOException {
        int pos = getPosition(score);
        if (pos < bScoresArray.length) {
            for (int i = 2; i > pos; i--) {
                bScoresArray[i] = bScoresArray[i - 1];
            }
            bScoresArray[pos] = score;
        }
        writeFile();
    }

    public int getPosition(BestScores score) {
        int pos = 0;
        while (pos < bScoresArray.length && score.getScore() < bScoresArray[pos].getScore()) {
            pos++;
        }
        return pos;
    }

    public void writeFile() throws IOException {
        out = new ObjectOutputStream(new FileOutputStream(file));
        for (BestScores s : bScoresArray) {
            out.writeObject(s);
        }
    }

    public void readFile() throws FileNotFoundException, IOException, ClassNotFoundException {
        try {
            in = new ObjectInputStream(new FileInputStream(file));
            while (true) {
                BestScores score = (BestScores) in.readObject();
                if(score != null){
                    bScoresArray[counter] = score;
                }else {
                    bScoresArray[counter] = new BestScores("Nosabesnada",0);
                    counter++;
                }

            }
        } catch (EOFException ex) {
            System.err.println("End Of File");
        }
    }

    public BestScores returnScores(int index) {
        BestScores returner = bScoresArray[index];
        if(returner == null)
            returner = new BestScores("Preset", 0);
        return returner;
    }

}