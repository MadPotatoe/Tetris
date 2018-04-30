
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Chal.ly
 */
public class BestScores implements Serializable{
    
    private String user;
    private int score;
    
    public BestScores(String user, int score){
        this.user = user;
        this.score = score;
    }
    
    public String getUser(){
        return user;
    }
    
    public int getScore(){
        return score;
    }
    
    @Override
    public String toString(){
        String s = user + ": " + score + " points";
        return s;
    }
    
}
