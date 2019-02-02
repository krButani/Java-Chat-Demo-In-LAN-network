/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package collagegroup;

import java.util.Random;

/**
 *
 * @author krButani
 */
public class Rand {
    Random r = new Random();
    
    int nextVal(){
        int val = 8;
        for(int i=0;i<3;i++) {
            int digit = r.nextInt(8) + 1;
            val = (val*10) + digit;
        }
        return val;
    }
}