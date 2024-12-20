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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 22/08/2022, 10:09:17
 *
 * @author IPT - computer
 * @version 1.0
 */
public class BlockChain implements Serializable {

    ArrayList<Block> chain = new ArrayList<>();

    /**
     * gets the last block hash of the chain
     *
     * @return last hash in the chain
     */
    public String getLastBlockHash() {
        //Genesis block
        if (chain.isEmpty()) {
            return String.format("%08d", 0);
        }
        //hash of the last in the list
        return chain.get(chain.size() - 1).currentHash;
    }

    /**
     * adds data to the blockChain
     *
     * @param data data to add in the block
     * @param dificulty dificulty of block to miners (POW)
     * @param merkleRoot MerkleTree root for aditional integrity and security
     */
    public void add(String data, int dificulty, String merkleRoot) throws InterruptedException {
        //hash of previous block
        String prevHash = getLastBlockHash();
        //mining block
        int cores = Runtime.getRuntime().availableProcessors();
        int max_nonce = (int) 1E9;
        int nonce = 0;

        Miner[] miner = new Miner[cores];

        for (int i = 0; i < cores; i++) {
            int ini = i * (max_nonce / cores);
            int fin = ini + (max_nonce / cores);
            miner[i] = new Miner(prevHash + data, dificulty, ini, fin, max_nonce);
            miner[i].start();
        }

        // Wait for all miners to finish
        for (int i = 0; i < cores; i++) {
            miner[i].join(); // Aguarda a conclusão de cada minerador
            //Verificar se o minerador encontrou o nonce
            if (miner[i].nonceFound != 0) {
                nonce = miner[i].nonceFound;
                break;
            }
        }

        //build new block
        Block newBlock = new Block(prevHash, data, nonce, merkleRoot);
        //add new block to the chain
        chain.add(newBlock);
    }

    /**
     *
     * @param index
     * @return
     */
    public Block get(int index) {
        return chain.get(index);
    }

    /**
     *
     * @return
     */
    public String toString() {
        StringBuilder txt = new StringBuilder();
        txt.append("Blochain size = " + chain.size() + "\n");
        for (Block block : chain) {
            txt.append(block.toString() + "\n");
        }
        return txt.toString();
    }

    /**
     *
     * @return
     */
    public List<Block> getChain() {
        return chain;
    }

    /**
     *
     * @param fileName
     * @throws Exception
     */
    public void save(String fileName) throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(chain);
        }
    }

    /**
     *
     * @param fileName
     * @throws Exception
     */
    public void load(String fileName) throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            this.chain = (ArrayList<Block>) in.readObject();
        }
    }

    /**
     *
     * @return
     */
    public boolean isValid() {
        //Validate blocks
        for (Block block : chain) {
            if (!block.isValid()) {
                return false;
            }
        }
        //validate Links
        //starts in the second block
        for (int i = 1; i < chain.size(); i++) {
            //previous hash !=  hash of previous
            if (chain.get(i).previousHash != chain.get(i - 1).currentHash) {
                return false;
            }
        }
        return true;
    }

    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    private static final long serialVersionUID = 202208221009L;
    //:::::::::::::::::::::::::::  Copyright(c) M@nso  2022  :::::::::::::::::::
    ///////////////////////////////////////////////////////////////////////////
}
