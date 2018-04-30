/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Chal.ly
 */
public class Config {
    
    private static Config config = null;
    private int speed;
    private String userName;
    
    private Config(){
        speed = 0;
        userName = null;
    }
    
    public static Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        userName = name;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int newSpeed) {
        speed = newSpeed;
    } 
    
}
