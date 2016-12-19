/*-
 * -\-\-
 * Helios Services
 * --
 * Copyright (C) 2016 Spotify AB
 * --
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
 * -/-/-
 */

package com.spotify.helios.servicescommon.statistics;

import com.codahale.metrics.Timer.Context;

/**
 * Creation of this class marks the beginning of a call to some service. Either {@link #success()}
 * {@link #userError()} or {@link #failure()} should be called when the call completes to record the
 * duration of the call and increment the call counter.
 */
public class MetricsContextImpl implements MetricsContext {

  private final RequestMetrics metrics;
  private final Context timerContext;

  public MetricsContextImpl(RequestMetrics metrics) {
    this.metrics = metrics;
    this.timerContext = metrics.begin();
  }

  @Override
  public void success() {
    metrics.success(timerContext);
  }

  @Override
  public void failure() {
    metrics.failure(timerContext);
  }

  @Override
  public void userError() {
    metrics.userError(timerContext);
  }
}
