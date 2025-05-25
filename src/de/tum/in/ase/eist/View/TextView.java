package de.tum.in.ase.eist.View;

import de.tum.in.ase.eist.Controller.Controller;
import de.tum.in.ase.eist.Model.MFT;


import java.util.List;
import java.util.Scanner;

public class TextView extends View {

    private ScannerWrapper scanner;

    public TextView(Controller controller, List<MFT> mftList) {
		super(controller, mftList);
        this.scanner = new ScannerWrapper(new Scanner(System.in));
    }

	public void run(){
		while (true) {
			System.out.println("\nStock: ");
            printStock(mfts);
			System.out.println("\nBalance: " + this.balance);
			System.out.println("\n> To buy a new MFT, type b.\n> To change the price of a MFT, type c.");
			String input = "";
            try {
                input = scanner.nextLine();
            } catch (Exception e) {
				System.out.println("Closing application.");
                return;
            }
			switch (input) {
				case "b":
					buy();
					break;
				case "c":
					changePrice();
					break;
				default:
					System.out.println("Invalid input\n");
			}
		}
	}

    private void printStock(List<MFT> mfts) {
        System.out.println("ID\tName\tDescription\tPrice\tOwner");
        for (MFT mft : mfts) {
            int ownerId = controller.getMFTOwnerId(mft);
            String owner = ownerId == ID ? "You" : "Other";

            String padding = mft.getDescription().length() < 8 ? "\t\t" : "\t";
            System.out.println(mft.getId() + "\t" + mft.getName() + "\t" + mft.getDescription() + padding + mft.getPrice() + "\t" + owner);
        }
    }

    // TODO L03P02 View 3.1: Finish buy method
    // Finish this method
    // Use the public methods of the controller to check if the MFT exists and then buy it
    // Remember that the controller can throw exceptions
    public void buy(){
        try {
            System.out.println("Beginning new transaction:");
            System.out.print("MFT ID: ");
            int mftID = scanner.nextInt();
		    scanner.nextLine();
            // EDIT BELOW THIS LINE
            MFT mft = controller.getMFTById(mftID);
            if (mft == null) {
                System.out.println("No MFT found with ID " + mftID);
                return;
            }
            controller.buyMFT(mft, this.ID);

            // STOP EDITING HERE
        } catch (Exception e) {
            System.out.println("Buy failed: " + e.getMessage());
        }
    }

    // TODO L03P02 View 3.2: Finish changePrice method
	public void changePrice() {
        try {
            System.out.println("Changing price of MFT");
            System.out.print("MFT ID: ");
            int mftID = scanner.nextInt();
            scanner.nextLine();

            System.out.print("New price (double): ");
            double newPrice = scanner.nextDouble();
            scanner.nextLine();

            // Finish this method
            // Use the public methods of the controller to check if the MFT exists and then change its price
            // Remember that the controller can throw exceptions
            // EDIT BELOW THIS LINE
            MFT mft =  controller.getMFTById(mftID);
            if (mft == null) {
                System.out.println("No MFT found with ID " + mftID);
                return;
            }
            controller.changePrice(mft, newPrice);
            // STOP EDITING HERE
        } catch (Exception e) {
            System.out.println("Change price operation failed: " + e.getMessage());
        }
    }

}
