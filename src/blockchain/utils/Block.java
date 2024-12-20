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

import java.io.Serializable;

/**
 * Created on 22/08/2022, 09:23:49
 *
 * Block with consensus of Proof of Work
 *
 * @author IPT - computer
 * @version 1.0
 */
public class Block implements Serializable {

    String previousHash; // link to previous block
    String data;         // data in the block
    int nonce;           // proof of work 
    String currentHash;  // Hash of block
    String merkleRoot;

    /**
     *
     * @param previousHash
     * @param data
     * @param nonce
     */
    public Block(String previousHash, String data, int nonce, String merkleRoot) {
        this.previousHash = previousHash;
        this.data = data;
        this.nonce = nonce;
        this.currentHash = calculateHash();
        this.merkleRoot = merkleRoot;
    }

    /**
     *
     * @return
     */
    public String getData() {
        return data;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    /**
     *
     * @return
     */
    public String getPreviousHash() {
        return previousHash;
    }

    /**
     *
     * @return
     */
    public int getNonce() {
        return nonce;
    }

    /**
     *
     * @return
     */
    public String calculateHash() {
        return Hash.getHash(nonce + previousHash + data);
    }

    /**
     *
     * @return
     */
    public String getCurrentHash() {
        return currentHash;
    }

    /**
     *
     * @return
     */
    public String toString() {
        return // (isValid() ? "OK\t" : "ERROR\t")+
                String.format("[ %8s", previousHash) + " <- "
                + String.format("%-10s", data) + String.format(" %7d ] = ", nonce)
                + String.format("%8s", currentHash);

    }

    /**
     *
     * @return
     */
    public boolean isValid() {
        return currentHash.equals(calculateHash());
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202208220923L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2022  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////

}
