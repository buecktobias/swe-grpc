/*
 * Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.acme.kunde;

// import module java.base;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Objects;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;

/// Banner als String-Konstante für den Start des Servers.
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
@SuppressWarnings({
    "AccessOfSystemProperties",
    "CallToSystemGetenv",
    "UtilityClassCanBeEnum",
    "UtilityClass",
    "ClassUnconnectedToPackage"
})
@SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
final class Banner {

    // http://patorjk.com/software/taag/#p=display&f=Slant&t=kunde%202024.10.1
    private static final String FIGLET = """
            __                   __        ___   ____ ___  __ __   _______   ___
           / /____  ______  ____/ /__     |__ \\ / __ \\__ \\/ // /  <  / __ \\ <  /
          / //_/ / / / __ \\/ __  / _ \\    __/ // / / /_/ / // /_  / / / / / / /
         / ,< / /_/ / / / / /_/ /  __/   / __// /_/ / __/__  __/ / / /_/ / / /
        /_/|_|\\__,_/_/ /_/\\__,_/\\___/   /____/\\____/____/ /_/ (_)_/\\____(_)_/
        """;
    private static final String SERVICE_HOST = System.getenv("KUNDE_SERVICE_HOST");
    private static final String KUBERNETES = SERVICE_HOST == null
        ? "N/A"
        : "KUNDE_SERVICE_HOST=" + SERVICE_HOST + ", KUNDE_SERVICE_PORT=" + System.getenv("KUNDE_SERVICE_PORT");

    /// Banner für den Server-Start.
    static final String TEXT = """

        $figlet
        (C) Juergen Zimmermann, Hochschule Karlsruhe
        Version             2024.10.1
        Spring Boot         $springBoot
        Spring Framework    $spring
        Java                $java
        Betriebssystem      $os
        Rechnername         $rechnername
        IP-Adresse          $ip
        Heap: Size          $heapSize
        Heap: Free          $heapFree
        Kubernetes          $kubernetes
        Username            $username
        JVM Locale          $locale
        OpenAPI             /swagger-ui.html /v3/api-docs.yaml
        """
        .replace("$figlet", FIGLET)
        .replace("$springBoot", SpringBootVersion.getVersion())
        .replace("$spring", Objects.requireNonNull(SpringVersion.getVersion()))
        .replace("$java", Runtime.version() + " - " + System.getProperty("java.vendor"))
        .replace("$os", System.getProperty("os.name"))
        .replace("$rechnername", getLocalhost().getHostName())
        .replace("$ip", getLocalhost().getHostAddress())
        .replace("$heapSize", Runtime.getRuntime().totalMemory() / (1024L * 1024L) + " MiB")
        .replace("$heapFree", Runtime.getRuntime().freeMemory() / (1024L * 1024L) + " MiB")
        .replace("$kubernetes", KUBERNETES)
        .replace("$username", System.getProperty("user.name"))
        .replace("$locale", Locale.getDefault().toString());

    @SuppressWarnings("ImplicitCallToSuper")
    private Banner() {
    }

    private static InetAddress getLocalhost() {
        try {
            return InetAddress.getLocalHost();
        } catch (final UnknownHostException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
