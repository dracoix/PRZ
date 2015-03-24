/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prz;

import java.io.IOException;

/**
 *
 * @author David 'Q' Rathbun
 */
public class dev {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
           
           long x = 255;
           x = Long.rotateRight(x, 1);
           System.out.println(x);
           x = Long.rotateLeft(x, 2);
            System.out.println(x);
           
       }
}
