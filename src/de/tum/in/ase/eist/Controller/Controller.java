package de.tum.in.ase.eist.Controller;

import de.tum.in.ase.eist.Model.*;
import de.tum.in.ase.eist.View.*;


import java.util.List;

public class Controller {
	private final MFT_DAO mftDAO;

    // For now we only have a single client
    private View view;

    public Controller(DatabaseHelper databaseHelper, TransactionLog transactionLog) {
        this.mftDAO = new MFT_DAO(databaseHelper, transactionLog);
		this.view = null;
    }

    public void registerClient(View view) {
        this.view = view;
    }

    public int getMFTOwnerId(MFT mft) {
        return mftDAO.getOwnerId(mft);
    }

	public MFT getMFTById(int id) {
		return mftDAO.selectMFTById(id);
	}

    public List<MFT> getMFTs() {
        return mftDAO.selectMFTs();
    }

	// helper to add stock that doesn't belong to us
	public void addMFT(MFT mft) {
		mftDAO.addMFT(999, mft);
	}

    // TODO L03P02 Controller 2.1: Implement buyMFT method
    // Implement the following method
    // - buyMFT(MFT mft, int buyerID): void
    // This method should allow you to buy an MFT that you do not own already
    // Buying is only possible if you have enough money
    // If buying is possible, add the Transaction to the TransactionLog
    // DO NOT CHANGE THE METHOD SIGNATURE
    public void buyMFT(MFT mft, int buyerID) {
        try {
            if (mft == null) {
                throw new Exception("Invalid MFT.");
            }

            double current = view.getBalance();
            double price = mft.getPrice();
            if (current < price) {
                throw new Exception("Insufficient funds.");
            }

            int ownerId = getMFTOwnerId(mft);
            if (ownerId == buyerID) {
                throw new Exception("Own mft.");
            }
            boolean success = mftDAO.recordTransaction(ownerId, buyerID, mft.getId(), price);
            if (!success) {
                throw new Exception("Transaction failed.");
            }

            view.addMFT(mft);
            view.setBalance(current - price);
        } catch (Exception e) {
            System.out.println("Buy MFT failed: " + e.getMessage());
        }

    }

    // TODO L03P02 Controller 2.2: Implement changePrice method
    // Implement the following method
    // - changePrice(MFT mft, double newPrice): void
    // This method should allow you to change the price of an MFT that you own
    // DO NOT CHANGE THE METHOD SIGNATURE
    public void changePrice(MFT mft, double newPrice) {
        try {
            if (mft == null || newPrice < 0) {
                throw new Exception("Invalid input.");
            }

            int ownerId = getMFTOwnerId(mft);
            if (ownerId != view.getID()) {
                throw new Exception("Invalid owner.");
            }

            mft.setPrice(newPrice);

            mftDAO.updateMFT(mft);
            view.update();
        } catch (Exception e) {
            System.out.println("Change price failed: " + e.getMessage());
        }
    }
}
