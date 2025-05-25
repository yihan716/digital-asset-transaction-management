package de.tum.in.ase.eist.Model;

import java.util.List;

public class MFT_DAO {

    protected final DatabaseHelper databaseHelper;
	protected final TransactionLog transactionLog;

    public MFT_DAO(DatabaseHelper databaseHelper, TransactionLog transactionLog) {
        this.databaseHelper = databaseHelper;
		this.transactionLog = transactionLog;
    }

    public int addMFT(int userId, MFT mft) {
        int ret = databaseHelper.insertMFT(mft);
		if (ret != -1) {
			transactionLog.insertTransaction(new Transaction(999, userId, mft.getId(), 0.0));
		}
		return ret;
    }

	public void updateMFT(MFT mft) {
		databaseHelper.updateMFT(mft);
	}

    public MFT selectMFTById(int id) {
        MFT mft = databaseHelper.selectMFTById(id);

        if (mft == null) {
            System.out.println("Couldn't find MFT with id: " + id);
        }

        return mft;
    }

	public List<MFT> selectMFTs() {
		return databaseHelper.selectAll();
	}

    // TODO L03P02 Model 1.5: Implement getOwnerId method in MFT_DAO
    // Implement the following method
    // - getOwnerId(MFT mft): int
    // This method should return the owner id of the given MFT
    // If the MFT is not found in the TransactionLog, return -1
    // DO NOT CHANGE THE METHOD SIGNATURE
	public int getOwnerId(MFT mft) {
        if (mft == null) return -1;

        Transaction current = transactionLog.begin();
        int mftId = mft.getId();
        int ownerId = -1;
        while (current != null) {
            if (current.getMFTId() == mftId) {
                ownerId = current.getBuyerId();
            }
            current = transactionLog.next(current);
        }
		return ownerId;
	}

    // TODO L03P02 Model 1.6: Implement recordTransaction method in MFT_DAO
    // Implement the following method
    // - recordTransaction(int Seller, int Buyer, int MFTId, double price): boolean
    // This method should record the transaction in the TransactionLog
    // If the transaction is successfully recorded, return true
    // DO NOT CHANGE THE METHOD SIGNATURE
	public boolean recordTransaction(int Seller, int Buyer, int MFTId, double price) {
        Transaction transaction = new Transaction(Seller, Buyer, MFTId, price);
        return transactionLog.insertTransaction(transaction);

	}
}
