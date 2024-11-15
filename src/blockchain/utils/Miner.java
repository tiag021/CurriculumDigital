//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2022   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package blockchain.utils;

/**
 * Created on 28/09/2022, 11:13:39
 *
 * @author IPT - computer
 * @version 1.0
 */
public class Miner extends Thread {

    public int max_nonce;
    public String data;
    public int difficulty;
    public int nonce, nonceFound;
    public int ini, fin;

    private static volatile boolean found = false;

    public Miner(String data, int difficulty, int ini, int fin, int max_nonce) {
        this.data = data;
        this.difficulty = difficulty;
        this.ini = ini;
        this.fin = fin;
        this.max_nonce = max_nonce;
    }

    public void run() {
        //String of zeros
        String zeros = String.format("%0" + difficulty + "d", 0);
        //starting nonce
        int nonce = 0;
        while (nonce < max_nonce) {
            //calculate hash of block
            String hash = Hash.getHash(nonce + data);
            //Nounce found
            if (hash.endsWith(zeros)) {
                nonceFound = nonce;
                found = true;
                break;
            }
            // Verify if another thread found the nonce alredy, using the shared boolean "found"
            if (found == true) {
                interrupt();
                break;
            }
            //next nounce
            nonce++;
        }
    }

    /**
     *
     * @param data
     * @param dificulty
     * @return
     */
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202209281113L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2022  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
