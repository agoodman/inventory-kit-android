package com.migrant;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import static android.view.View.*;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.migrant.enrollmint.client.Client;
import com.migrant.ik.InventoryKit;

public class IKActivity extends Activity {

	private static final String MY_CONSUMABLE = "your.consumable.key";
	private static final String MY_NON_CONSUMABLE = "your.nonconsumable.key";

	private int quantityAvailable = 0;
	private boolean productActivated = false;

	private Button quantityMinusButton;
	private Button quantityPlusButton;
	private TextView quantityText;
	private CheckBox activationCheckBox;
	private Button activateProductButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// setup inventory-kit
		InventoryKit.registerWithPaymentQueue(this);
		InventoryKit.setApiClient(new Client("07f54d1b88", "com.gazellelab"));
		InventoryKit.setCustomerEmail("test@test.com",
				new InventoryKit.DefaultCustomerListener() {

					@Override
					public void customerError() {
						Log.e("IKExample", "Customer error");
						showAlert("Unable to load customer data","OK",new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
							
							
						},"Retry",new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
							
						});
					}

				});

		// quantity components
		this.quantityPlusButton = (Button) findViewById(R.id.quantityPlus);
		this.quantityMinusButton = (Button) findViewById(R.id.quantityMinus);
		this.quantityText = (TextView) findViewById(R.id.quantity);

		quantityAvailable = InventoryKit.quantityAvailable(MY_CONSUMABLE);
		quantityText.setText(new Integer(quantityAvailable).toString());

		quantityPlusButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InventoryKit.activateProduct(MY_CONSUMABLE, 1);
				quantityAvailable = InventoryKit
						.quantityAvailable(MY_CONSUMABLE);
				quantityText.setText(new Integer(quantityAvailable).toString());
			}

		});

		quantityMinusButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InventoryKit.consumeQuantity(MY_CONSUMABLE, 1);
				quantityAvailable = InventoryKit
						.quantityAvailable(MY_CONSUMABLE);
				quantityText.setText(new Integer(quantityAvailable).toString());
			}

		});

		// product activation components
		this.activateProductButton = (Button) findViewById(R.id.buyButton);
		this.activationCheckBox = (CheckBox) findViewById(R.id.productActivation);

		activateProductButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!productActivated) {
					showAlert("Are you sure you want to activate the product?",
							"OK",
							// ok
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									InventoryKit
											.activateProduct(MY_NON_CONSUMABLE);
									productActivated = InventoryKit
											.productActivated(MY_NON_CONSUMABLE);
									activationCheckBox.setChecked(true);
									activationCheckBox.setText("Purchased");
									activateProductButton.setEnabled(false);
									activateProductButton
											.setVisibility(INVISIBLE);

								}
							}, 
							"Cancel",
							// cancel
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int id) {

									activationCheckBox.setChecked(false);
									activationCheckBox.setText("");
									activateProductButton.setEnabled(true);
									activateProductButton
											.setVisibility(VISIBLE);

								}

							});
				}
			}

		});

	}

	private final void showAlert(String aMsg,
			String aOkTitle,
			DialogInterface.OnClickListener okListener,
			String aCancelTitle,
			DialogInterface.OnClickListener cancelListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(IKActivity.this);
		builder.setMessage(aMsg).setCancelable(true)
				.setPositiveButton(aOkTitle, okListener)
				.setNegativeButton(aCancelTitle, cancelListener);
		AlertDialog alert = builder.create();
		alert.show();
	}

}