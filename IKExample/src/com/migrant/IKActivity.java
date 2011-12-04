package com.migrant;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import static android.view.View.*;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.migrant.ik.InventoryKit;

public class IKActivity extends Activity {
	
	private static final String MY_CONSUMABLE = "your.consumable.key";
	private static final String MY_NON_CONSUMABLE = "your.nonconsumable.key";
	
	private int quantityAvailable = 0;
	private boolean productActivated = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// setup inventory-kit
		InventoryKit.registerWithPaymentQueue(this);
		
		// quantity components
		Button tPlusButton = (Button) findViewById(R.id.quantityPlus);
		Button tMinusButton = (Button) findViewById(R.id.quantityMinus);
		final TextView tQuantityValue = (TextView) findViewById(R.id.quantity);

		quantityAvailable = InventoryKit.quantityAvailable(MY_CONSUMABLE);
		tQuantityValue.setText(new Integer(quantityAvailable).toString());

		tPlusButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InventoryKit.activateProduct(MY_CONSUMABLE, 1);
				quantityAvailable = InventoryKit.quantityAvailable(MY_CONSUMABLE);
				tQuantityValue.setText(new Integer(quantityAvailable).toString());
			}

		});
		
		tMinusButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InventoryKit.consumeQuantity(MY_CONSUMABLE, 1);
				quantityAvailable = InventoryKit.quantityAvailable(MY_CONSUMABLE);
				tQuantityValue.setText(new Integer(quantityAvailable).toString());
			}
			
		});
		
		// product activation components
		final Button tBuyButton = (Button) findViewById(R.id.buyButton);
		final CheckBox tCheckBox = (CheckBox) findViewById(R.id.productActivation);

		tBuyButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( !productActivated ) {
					AlertDialog.Builder builder = new AlertDialog.Builder(IKActivity.this);
					builder.setMessage("Are you sure you want to activate the product?")
							.setCancelable(true)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											
											InventoryKit.activateProduct(MY_NON_CONSUMABLE);
											productActivated = InventoryKit.productActivated(MY_NON_CONSUMABLE);
											tCheckBox.setChecked(true);
											tCheckBox.setText("Purchased");
											tBuyButton.setEnabled(false);
											tBuyButton.setVisibility(INVISIBLE);

										}
									})
							.setNegativeButton("Cancel", 
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int id) {
											
											tCheckBox.setChecked(false);
											tCheckBox.setText("Purchased");
											tBuyButton.setEnabled(true);
											tBuyButton.setVisibility(VISIBLE);
											
										}
										
									});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
			
		});
		
	}

}