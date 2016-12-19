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

package com.spotify.helios.servicescommon;

import static com.google.common.collect.Iterables.get;
import static com.spotify.logging.LoggingConfigurator.Level.ALL;
import static com.spotify.logging.LoggingConfigurator.Level.DEBUG;
import static com.spotify.logging.LoggingConfigurator.Level.INFO;
import static java.util.Arrays.asList;

import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Strings;
import com.google.common.util.concurrent.AbstractIdleService;
import com.spotify.helios.common.LoggingConfig;
import com.spotify.logging.LoggingConfigurator;
import com.spotify.logging.LoggingConfigurator.Level;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.setup.Environment;
import io.dropwizard.validation.valuehandling.OptionalValidatedValueUnwrapper;
import java.io.File;
import javax.validation.Validation;
import javax.validation.Validator;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * Handles setting up proper logging for our services.
 */
public abstract class ServiceMain extends AbstractIdleService {

  protected ServiceMain(LoggingConfig loggingConfig, String sentryDsn) {
    super();
    setupLogging(loggingConfig, sentryDsn);
  }

  protected static Environment createEnvironment(final String name) {
    final Validator validator = Validation
        .byProvider(HibernateValidator.class)
        .configure()
        .addValidatedValueHandler(new OptionalValidatedValueUnwrapper())
        .buildValidatorFactory()
        .getValidator();
    return new Environment(name,
        Jackson.newObjectMapper(),
        validator,
        new MetricRegistry(),
        Thread.currentThread().getContextClassLoader());
  }

  protected static void setupLogging(LoggingConfig config, String sentryDsn) {
    if (config.getNoLogSetup()) {
      return;
    }

    // Hijack JUL
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();

    final int verbose = config.getVerbosity();
    final Level level = get(asList(INFO, DEBUG, ALL), verbose, ALL);
    final File logconfig = config.getConfigFile();

    if (logconfig != null) {
      LoggingConfigurator.configure(logconfig);
    } else {
      if (config.isSyslog()) {
        LoggingConfigurator.configureSyslogDefaults("helios", level);
      } else {
        LoggingConfigurator.configureDefaults("helios", level);
      }

      if (!Strings.isNullOrEmpty(sentryDsn)) {
        LoggingConfigurator.addSentryAppender(sentryDsn);
      }
    }
  }
}
