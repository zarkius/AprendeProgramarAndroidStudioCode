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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.integrity.IntegrityManager;
import com.google.android.play.core.integrity.IntegrityManagerFactory;
import com.google.android.play.core.integrity.IntegrityTokenRequest;
import com.google.android.play.core.integrity.IntegrityTokenResponse;

import java.util.Objects;

public class LauncherActivity extends AppCompatActivity {
    private static final String TAG = "LauncherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        checkPlayIntegrity();
    }

    private void checkPlayIntegrity() {
        IntegrityManager integrityManager = IntegrityManagerFactory.create(this);
        Task<IntegrityTokenResponse> integrityTokenResponse = integrityManager.requestIntegrityToken(
                IntegrityTokenRequest.builder()
                        .setCloudProjectNumber(123456789012L)
                        .build());
        integrityTokenResponse.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String integrityToken = task.getResult().token();
                Log.d(TAG, "Integrity token: " + integrityToken);

            } else {
                int errorCode = ((com.google.android.play.core.integrity.IntegrityServiceException) Objects.requireNonNull(task.getException())).getErrorCode();
                Log.e(TAG, "Error requesting integrity token: " + errorCode);
                Toast.makeText(LauncherActivity.this, "Error: " + errorCode, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
