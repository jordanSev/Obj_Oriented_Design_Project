package acctMgr.controller;

import acctMgr.model.AccountList;
import acctMgr.model.Account;
import acctMgr.view.AccountSelectionView;
import acctMgr.view.AccountView;

import java.io.File;
import java.util.ArrayList;


public class AccountSelectionController extends AbstractController{

    public static AccountList createAcctList(File file) {
        AccountList accounts = new AccountList();
        return accounts;
    }
    public void buttonOperation(String opt, int index){
        ArrayList<Account> accounts = AccountList.getAccountList();
        ArrayList<Account> accountsEuro = AccountList.accountsEuro();
        ArrayList<Account> accountsYen = AccountList.accountsYen();

        if(opt == AccountView.Save) {
            AccountList.save();
        } else if(opt == AccountSelectionView.SaveAndExit) {
            AccountList.save();
            AccountSelectionView acView = (AccountSelectionView) getView();
            acView.dispose();
        } else if (opt == AccountSelectionView.EditInDollars) {
            accounts.get(index).setCurrencyType("Dollars");
            AccountView.accountView(accounts.get(index));
        }else if (opt == AccountSelectionView.EditInEuros) {
            accountsEuro.get(index).setCurrencyType("Euros");
            AccountView.accountView(accountsEuro.get(index));
        }else if (opt == AccountSelectionView.EditInYen) {
            accountsYen.get(index).setCurrencyType("Yen");
            AccountView.accountView(accountsYen.get(index));
        }
    }
}
