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

package com.spotify.helios.agent;

import com.google.common.base.Optional;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.ImageInfo;
import com.spotify.helios.common.descriptors.Job;

/**
 * An interface to allow you to change the {@link HostConfig} and {@link ContainerConfig} objects
 * before they are sent to Docker.  This way you can set defaults, or intercept various things.
 *
 * See: {@link SyslogRedirectingContainerDecorator} for an example.
 */
public interface ContainerDecorator {

  void decorateHostConfig(Job job, Optional<String> dockerVersion, HostConfig.Builder hostConfig);

  void decorateContainerConfig(Job job, ImageInfo imageInfo, Optional<String> dockerVersion,
                               ContainerConfig.Builder containerConfig);
}
