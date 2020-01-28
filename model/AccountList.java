package acctMgr.model;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.io.File;

/**
 * The Class AccountList.
 */
public class AccountList extends AbstractModel {
    
    /** The Constant EuroExchange. */
    private final static BigDecimal EuroExchange = BigDecimal.valueOf(0.79);
    
    /** The Constant YenExchange. */
    private final static BigDecimal YenExchange = BigDecimal.valueOf(94.1);

    /** The account list. */
    public static ArrayList<Account> accountList = new ArrayList<>();
    
    /** The account list yen. */
    public static ArrayList<Account> accountListYen = new ArrayList<>();
    
    /** The account list euro. */
    public static ArrayList<Account> accountListEuro = new ArrayList<>();
    
    /** The file location. */
    public static File fileLocation;

    /**
     * Instantiates a new account list.
     */
    public AccountList() {
    	 accountList = new ArrayList<Account>();
    }
    
    /**
     * Load.
     *
     * @param fileLoc the file loc
     * @return the array list
     */
    public static ArrayList load(File fileLoc) {
        fileLocation = fileLoc;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileLoc));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(" ");

                Account newAccount = new Account(tokens[0], tokens[1], new BigDecimal(tokens[2]), "Dollars", false);
                accountList.add(newAccount);
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return accountList;
    }

    /**
     * Save.
     * saves the account list to a file
     */
    public static void save() {
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter outFile = new PrintWriter(bw);


        for (int i = 0; i < AccountList.getAccountList().size(); i++) {

            outFile.print(accountList.get(i));
        }

        outFile.close();
        System.out.println("Output file is in: " + fileLocation);
    }

    /**
     * Gets the account list.
     *
     * @return the account list
     */
    public static ArrayList<Account> getAccountList() {
        return accountList;
    }
   
    /**
     * Accounts yen.
     *
     * @return the array list
     */
    public static ArrayList<Account> accountsYen() {
        for (int i = 0; i < AccountList.getAccountList().size(); i++)
        {
            BigDecimal newValue = accountList.get(i).getBalance();
            newValue = newValue.multiply(YenExchange);
            Account newAccount = new Account(accountList.get(i).getName(), accountList.get(i).getID(), newValue, "Yen", false);
            accountListYen.add(newAccount);
        }
        return accountListYen;
    }
    
    /**
     * Accounts euro.
     *
     * @return the array list
     */
    public static ArrayList<Account> accountsEuro() {
        for (int i = 0; i < AccountList.getAccountList().size(); i++)
        {
            BigDecimal newValue = accountList.get(i).getBalance();
            newValue = newValue.multiply(EuroExchange);
            Account newAccount = new Account(accountList.get(i).getName(), accountList.get(i).getID(), newValue, "Euro" , false);
            accountListEuro.add(newAccount);
        }
        return accountListEuro;
    }
    
    /**
     * Accounts dollars.
     *
     * @param convertingAccount the converting account
     * @return the array list
     */
    public static ArrayList<Account> accountsDollars( ArrayList<Account> convertingAccount ){
        ArrayList<Account> dollarsArray = new ArrayList<>();
        if( convertingAccount.get(0).getCurrencyType() == "Yen"){
            for (int i = 0; i < AccountList.getAccountList().size(); i++)
            {
                BigDecimal newValue = accountList.get(i).getBalance();
                newValue = newValue.divide(YenExchange);
                Account newAccount = new Account(accountList.get(i).getName(), accountList.get(i).getID(), newValue, "Dollars", false);
                dollarsArray.add(newAccount);
            }
        }
        else if( convertingAccount.get(0).getCurrencyType() == "Euro"){
            for (int i = 0; i < AccountList.getAccountList().size(); i++)
            {
                BigDecimal newValue = accountList.get(i).getBalance();
                newValue = newValue.divide(EuroExchange);
                Account newAccount = new Account(accountList.get(i).getName(), accountList.get(i).getID(), newValue, "Dollars", false);
                dollarsArray.add(newAccount);
            }
        }
        else{
            for (int i = 0; i < AccountList.getAccountList().size(); i++)
            {
                BigDecimal newValue = accountList.get(i).getBalance();
                Account newAccount = new Account(accountList.get(i).getName(), accountList.get(i).getID(), newValue, "Dollars", false);
                dollarsArray.add(newAccount);
            }
        }
        return dollarsArray;
    }
}
