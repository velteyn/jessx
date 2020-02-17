// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package Trobot;

public class TrobotLauncher
{
    private Robot robotTest;
    
    public TrobotLauncher() {
        (this.robotTest = new botTest(1)).start();
        System.out.println("robotTest lanc\u00e9");
    }
    
    public static void main(final String[] args) {
        new TrobotLauncher();
    }
}
