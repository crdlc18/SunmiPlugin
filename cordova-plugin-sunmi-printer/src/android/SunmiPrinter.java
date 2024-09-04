package com.chelseatravel.sunmi;

import android.content.Context;
import android.os.RemoteException;
import org.apache.cordova.*;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.SunmiPrinterService;

public class SunmiPrinter extends CordovaPlugin {
    private SunmiPrinterService sunmiPrinterService;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("bindPrinterService")) {
            bindPrinterService(callbackContext);
            return true;
        } else if (action.equals("printText")) {
            String text = args.getString(0);
            printText(text, callbackContext);
            return true;
        }
        return false;
    }

    private void bindPrinterService(CallbackContext callbackContext) {
        InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
            @Override
            protected void onConnected(SunmiPrinterService service) {
                sunmiPrinterService = service;
                callbackContext.success("Printer service connected");
            }

            @Override
            protected void onDisconnected() {
                sunmiPrinterService = null;
                callbackContext.error("Printer service disconnected");
            }
        };

        boolean result = InnerPrinterManager.getInstance().bindService(cordova.getContext(), innerPrinterCallback);
        if (!result) {
            callbackContext.error("Failed to bind printer service");
        }
    }

    private void printText(String text, CallbackContext callbackContext) {
        if (sunmiPrinterService != null) {
            try {
                sunmiPrinterService.printText(text, new InnerResultCallback());
                callbackContext.success("Printed: " + text);
            } catch (RemoteException e) {
                callbackContext.error("Error printing text: " + e.getMessage());
            }
        } else {
            callbackContext.error("Sunmi Printer Service is not connected");
        }
    }

    private class InnerResultCallback extends com.sunmi.peripheral.printer.InnerResultCallback.Stub {
        @Override
        public void onRunResult(boolean isSuccess) throws RemoteException {
            // Handle result
        }

        @Override
        public void onReturnString(String result) throws RemoteException {
            // Handle return string
        }

        @Override
        public void onRaiseException(int code, String msg) throws RemoteException {
            // Handle exception
        }

        @Override
        public void onPrintResult(int code, String msg) throws RemoteException {
            // Handle print result
        }
    }
}
