package pl.sdacademy.mg.inappbilling2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.mainActivitySkuDetailsButton)
    Button mDetailsButton;

    @BindView(R.id.mainActivitySkuDetailsTextView)
    TextView mDetailsTextView;

    @BindView(R.id.mainActivityPurchaseButton)
    Button mPurchaseButton;

    @BindView(R.id.mainActivityPurchaseTextView)
    TextView mPurchaseTextView;

    @BindView(R.id.mainActivityPurchasedItemsListButton)
    Button mListButton;

    @BindView(R.id.mainActivityPurchasedItemsListTextView)
    TextView mListTextView;

    IInAppBillingService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bindService();

        mDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSku();
            }
        });




    }

    private void getSku() {
        ArrayList<String> skuList = new ArrayList<String>();
        skuList.add("premiumUpgrade");
        skuList.add("gas");
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

        try {
            Bundle skuDetails = mService.getSkuDetails(3, getPackageName(), "inapp", querySkus);
            Log.i("SKU DETAILS", skuDetails.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Log.i("SKU",skuList.toString());
    }

    private void bindService() {
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }
}
