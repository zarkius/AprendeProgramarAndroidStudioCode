/*
 * Copyright 2020 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.playconsole.aprendeprogramar2;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.integrity.IntegrityManager;
import com.google.android.play.core.integrity.IntegrityManagerFactory;
import com.google.android.play.core.integrity.IntegrityServiceException;
import com.google.android.play.core.integrity.IntegrityTokenRequest;
import com.google.android.play.core.integrity.IntegrityTokenResponse;
import com.google.android.play.core.integrity.model.IntegrityErrorCode;

import java.util.Objects;


public class LauncherActivity extends AppCompatActivity {

    private static void onComplete(Task<IntegrityTokenResponse> task) {
        if (task.isSuccessful()) {
            IntegrityTokenResponse response = task.getResult();
            PlayIntegrityIntegrityCheck integrityCheck = PlayIntegrityIntegrityCheck.create();
            int resultCode = Integer.parseInt(integrityCheck.check(response.token()));

            if (resultCode == IntegrityErrorCode.PLAY_STORE_NOT_FOUND)
                Log.e("PlayIntegrity", "Integrity check failed: Play Store not found");
            else if (resultCode == IntegrityErrorCode.NO_ERROR) {
                Log.d("PlayIntegrity", "Integrity check passed");
            } else {
                Log.e("PlayIntegrity", "Integrity check failed: " + resultCode);
            }
        } else {
            int errorCode = ((IntegrityServiceException) Objects.requireNonNull(task.getException())).getErrorCode();

            if (errorCode == IntegrityErrorCode.PLAY_STORE_NOT_FOUND) {
                Log.e("PlayIntegrity", "Integrity check failed: Play Store not found");
            } else {
                Log.e("PlayIntegrity", "Integrity check failed: " + errorCode);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        checkPlayIntegrity();
    }

    private void checkPlayIntegrity() {
        IntegrityManager integrityManager = IntegrityManagerFactory.create(this);
        IntegrityTokenRequest integrityTokenRequest = IntegrityTokenRequest.builder()
                .setCloudProjectNumber(1234567890)
                .setNonce("example_nonce")
                .build();
        Task<IntegrityTokenResponse> integrityTokenResponse = integrityManager.requestIntegrityToken(integrityTokenRequest);
        integrityTokenResponse.addOnCompleteListener(LauncherActivity::onComplete);
    }
}
