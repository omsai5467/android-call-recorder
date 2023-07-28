package com.github.axet.callrecorder.services;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.telecom.Call;
import android.telecom.CallScreeningService;

import com.github.axet.callrecorder.app.CallApplication;

@TargetApi(29)
public class PhoneCallScreening extends CallScreeningService {

    public static String ROLE = RoleManager.ROLE_CALL_SCREENING;

    public static boolean isEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= 29 && context.getApplicationInfo().targetSdkVersion >= 29) {
            RoleManager roleManager = (RoleManager) context.getSystemService(ROLE_SERVICE);
            return roleManager.isRoleHeld(ROLE);
        } else {
            return true;
        }
    }

    public static void request(Activity a, int id) {
        RoleManager roleManager = (RoleManager) a.getSystemService(ROLE_SERVICE);
        Intent intent = roleManager.createRequestRoleIntent(ROLE);
        a.startActivityForResult(intent, id);
    }

    @Override
    public void onScreenCall(@NonNull Call.Details details) {
        if (details.getCallDirection() == Call.Details.DIRECTION_INCOMING) {
            Intent intent = new Intent(RecordingService.CALLSCREEN);
            intent.setPackage(getPackageName());
            intent.putExtra("phone", details.getHandle().getSchemeSpecificPart());
            intent.putExtra("call", CallApplication.CALL_IN);
            sendBroadcast(intent);
        }
        if (details.getCallDirection() == Call.Details.DIRECTION_OUTGOING) {
            Intent intent = new Intent(RecordingService.CALLSCREEN);
            intent.setPackage(getPackageName());
            intent.putExtra("phone", details.getHandle().getSchemeSpecificPart());
            intent.putExtra("call", CallApplication.CALL_OUT);
            sendBroadcast(intent);
        }
    }
}
