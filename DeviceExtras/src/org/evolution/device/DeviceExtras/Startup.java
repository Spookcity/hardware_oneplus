/*
* Copyright (C) 2013 The OmniROM Project
* Copyright (C) 2021-2022 The Evolution X Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package org.evolution.device.DeviceExtras;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import androidx.preference.PreferenceManager;

public class Startup extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";
    private static final String ONE_TIME_TUNABLE_RESTORE = "hardware_tunable_restored";

    private boolean mHBM = false;

    @Override
    public void onReceive(final Context context, final Intent bootintent) {

        DeviceExtras.restoreSliderStates(context);
        EarGainPreference.restore(context);
        org.evolution.device.DeviceExtras.doze.DozeUtils.checkDozeService(context);
        MicGainPreference.restore(context);
        VibratorCallStrengthPreference.restore(context);
        VibratorNotifStrengthPreference.restore(context);
        VibratorStrengthPreference.restore(context);

        boolean enabled = false;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_DC_SWITCH, false);
        if (enabled) {
        restore(DCModeSwitch.getFile(context), enabled);
               }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_DCI_SWITCH, false);
        if (enabled) {
        mHBM = false;
        restore(DCIModeSwitch.getFile(context), enabled);
               }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_HBM_SWITCH, false);
        if (enabled) {
        restore(HBMModeSwitch.getFile(context), enabled);
               }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_FPS_INFO, false);
        if (enabled) {
            context.startService(new Intent(context, FPSInfoService.class));
               }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_GAME_SWITCH, false);
        if (enabled) {
            restore(GameModeSwitch.getFile(context), enabled);
               }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_SRGB_SWITCH, false);
        if (enabled) {
        mHBM = false;
        restore(SRGBModeSwitch.getFile(context), enabled);
 	       }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_TP_EDGE_LIMIT_SWITCH, false);
        if (enabled) {
            restore(TPEdgeLimitModeSwitch.getFile(context), enabled);
               }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_USB2_SWITCH, false);
        if (enabled) {
        restore(USB2FastChargeModeSwitch.getFile(context), enabled);
               }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_OTG_SWITCH, false);
        if (enabled) {
        restore(OTGModeSwitch.getFile(context), enabled);
               }
        enabled = sharedPrefs.getBoolean(DeviceExtras.KEY_WIDE_SWITCH, false);
        if (enabled) {
        mHBM = false;
        restore(WideModeSwitch.getFile(context), enabled);
        }
    }

    private boolean hasRestoredTunable(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(ONE_TIME_TUNABLE_RESTORE, false);
    }

    private void setRestoredTunable(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(ONE_TIME_TUNABLE_RESTORE, true).apply();
    }

    private void restore(String file, boolean enabled) {
        if (file == null) {
            return;
        }
        if (enabled) {
            FileUtils.writeValue(file, "1");
        }
    }

    private void restore(String file, String value) {
        if (file == null) {
            return;
        }
        FileUtils.writeValue(file, value);
    }
}
