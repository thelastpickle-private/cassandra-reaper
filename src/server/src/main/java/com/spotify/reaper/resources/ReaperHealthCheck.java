/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spotify.reaper.resources;

import com.spotify.reaper.AppContext;

/**
 * Provides an endpoint to check the health of the running Reaper instance.
 */
public final class ReaperHealthCheck extends com.codahale.metrics.health.HealthCheck {

  private final AppContext context;

  public ReaperHealthCheck(AppContext context) {
    this.context = context;
  }

  @Override
  protected Result check() throws Exception {
    // Should check some other pre-conditions here for a healthy Reaper instance?
    if (context.storage.isStorageConnected()) {
      return Result.healthy();
    }
    return Result.unhealthy("storage not connected");
  }
}
